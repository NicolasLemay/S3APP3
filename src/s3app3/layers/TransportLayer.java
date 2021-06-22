package layers;

import s3app3.NetConfig;
import s3app3.exceptions.TransmissionErrorException;
import s3app3.packets.Packet;

import java.util.ArrayList;
import java.util.Arrays;

public class TransportLayer extends LayerHandler {

    private class HeaderOption {
        public final String name;
        public String value;

        public HeaderOption(String name, String value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return '['+name+':'+value+']';
        }
    }

    @Override
    public Packet send(Packet packet) {
        System.out.println("Transport layer : send : IP");
        ArrayList<HeaderOption> header;
        byte[] data = packet.getData();
        int sequenceNumber = 0;

        //First fragment, with file name
        header = buildHeader(packet.getFileName(), sequenceNumber++, packet.getSourceIP(), packet.getTargetIP(), new byte[0]);
        packet.addFragment(headerToString(header).getBytes());

        System.out.println("Number of bytes : " + data.length);

        //Fragments of data
        for(int i = 0; i < data.length; i+= NetConfig.maxBytesPerFragment()) {
            int fragmentDataLength = Math.min(data.length - i, NetConfig.maxBytesPerFragment());
            byte[] fragmentData = Arrays.copyOfRange(data, i, i + fragmentDataLength);
            header = buildHeader("", sequenceNumber++, packet.getSourceIP(), packet.getTargetIP(), fragmentData);
            packet.addFragment(headerToString(header).getBytes());
        }

        //Last fragment, with file name, followed by .end
        header = buildHeader(packet.getFileName() + ".end", sequenceNumber++, packet.getSourceIP(), packet.getTargetIP(), new byte[0]);
        packet.addFragment(headerToString(header).getBytes());

        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Transport layer : receive : IP");
        ArrayList<HeaderOption> header;
        StringBuilder dataBuilder = new StringBuilder();
        int sequenceNumber = 0;

        byte[] firstFragment = packet.getFragments().get(0);
        byte[] lastFragment = packet.getFragments().get(packet.getSize()-1);

        ArrayList<HeaderOption> firstFragmentHeader = stringToHeader(new String(firstFragment));
        ArrayList<HeaderOption> lastFragmentHeader = stringToHeader(new String(lastFragment));

        try {
            String nameFirstFragment = getValue(firstFragmentHeader, "name");
            String nameLastFragment = getValue(lastFragmentHeader, "name");
            String sourceIp = getValue(firstFragmentHeader, "sourceip");
            String targetIp = getValue(firstFragmentHeader, "targetip");

            if(nameFirstFragment.equals("")) throw new TransmissionErrorException("Missing filename");
            else packet.setFilePath(nameFirstFragment);
            if(!nameLastFragment.equals(nameFirstFragment+".end")) throw new TransmissionErrorException("Incomplete final fragment");

            packet.setSourceIP(sourceIp);
            packet.setTargetIP(targetIp);

        } catch (TransmissionErrorException e) {
            //TODO send bad acknowledgement
            e.printStackTrace();
        }

        for(byte[] fragment : packet.getFragments()) {

            header = stringToHeader(new String(fragment));

            Integer sequence = Integer.parseInt(getValue(header, "sequence"));
            String tempData = getValue(header, "data");

            try {
                if(!sequence.equals(sequenceNumber++)) throw new TransmissionErrorException("Wrong sequence transmission");
            } catch (TransmissionErrorException e) {
                e.printStackTrace();
                //TODO acknowledge bad result
            }

            dataBuilder.append(tempData);
        }

        packet.setData(dataBuilder.toString().getBytes());

        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    private ArrayList<HeaderOption> buildHeader(String name, Integer sequenceNo, String sourceIp, String targetIp, byte[] data) {
        ArrayList<HeaderOption> header = new ArrayList<>();

        header.add(new HeaderOption("name", name));
        header.add(new HeaderOption("sequence", Integer.toString(sequenceNo)));
        header.add(new HeaderOption("sourceip", sourceIp));
        header.add(new HeaderOption("targetip", targetIp));
        header.add(new HeaderOption("data", new String(data)));

        //Calculate header byte size (VERY UGLY)
        int nbBytes = headerToString(header).getBytes().length;
        HeaderOption sizeOption = new HeaderOption("size", Integer.toString(nbBytes));
        header.add(sizeOption);
        //Newly calculated
        nbBytes = headerToString(header).getBytes().length;
        sizeOption.value = Integer.toString(nbBytes);

        return header;
    }

    private String headerToString(ArrayList<HeaderOption> header) {
        StringBuilder stringHeader = new StringBuilder();
        for(HeaderOption ho : header) {
            stringHeader.append(ho.toString()).append(',');
        }
        //Removes last comma
        stringHeader.deleteCharAt(stringHeader.length() - 1);
        return stringHeader.toString();
    }

    private ArrayList<HeaderOption> stringToHeader(String stringHeader) {
        ArrayList<HeaderOption> header = new ArrayList<>();
        String[] stringHeaderOptions = stringHeader.split("\\[(.*?)\\]");
        for(String stringHeaderOption : stringHeaderOptions) {
            String[] headerOption = stringHeaderOption.split(":", 2);
            if(headerOption.length < 2) continue;
            header.add(new HeaderOption(headerOption[0], headerOption[1]));
        }
        return header;
    }

    private String getValue(ArrayList<HeaderOption> header, String fieldName) {
        for(HeaderOption option : header) {
            if(option.value.equals(fieldName)) return option.value;
        }
        return "";
    }
}
