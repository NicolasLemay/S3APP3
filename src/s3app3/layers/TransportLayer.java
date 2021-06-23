package s3app3.layers;


import s3app3.exceptions.TransmissionErrorException;
import s3app3.packets.Packet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

/**
 * The transport layer serves to dismantle packets into smaller fragments. The size of the data in each fragment
 * corresponds to the the value of maxBytesPerFragment present in the NetConfig class.
 */
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

    /**
     * Fragments a packet for the next layer.
     * @param packet the packet received from the previous layer, generally the application layer.
     * @return the same packet, fragmented into smaller chunks, with headers.
     */
    @Override
    public Packet send(Packet packet) {
        System.out.println("Transport layer : send : IP");
        ArrayList<HeaderOption> header;
        byte[] data = packet.getData();
        int sequenceNumber = 0;

        //First fragment, with file name
        header = buildHeader(packet.getFileName(), sequenceNumber++, packet.getSourceIP(), packet.getTargetIP(), new byte[0]);
        packet.addFragment(headerToString(header).getBytes());

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

        return packet;
    }


    /**
     * Rebuilds data from the packet fragments
     * @param packet the packet received from the previous layer, generally the data link layer.
     * @return the same packet, with its data rebuilt from its fragments
     */
    @Override
    public Packet receive(Packet packet) {
        System.out.println("Transport layer : receive : IP");
        ArrayList<HeaderOption> header;
        byte[] fullData = new byte[0];
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

        int j = 0;
        for(byte[] fragment : packet.getFragments()) {

            header = stringToHeader(new String(fragment));

            Integer sequence = Integer.parseInt(getValue(header, "sequence"));
            String tempData = getValue(header, "data");

            try {
                if(!sequence.equals(sequenceNumber++)) throw new TransmissionErrorException("Wrong sequence transmission. Expeceted : " + (sequenceNumber - 1) + ", got: " + sequence);
            } catch (TransmissionErrorException e) {
                e.printStackTrace();
                //TODO acknowledge bad result
            }

            byte[] tempDataByte = Base64.getDecoder().decode(tempData);
            byte[] fullDataTemp = new byte[fullData.length + tempDataByte.length];
            System.arraycopy(fullData, 0, fullDataTemp, 0, fullData.length);
            System.arraycopy(tempDataByte, 0, fullDataTemp, fullData.length, tempDataByte.length);
            fullData = fullDataTemp;
        }

        packet.setData(fullData);

        return packet;
    }

    //Builds the full header for a packet
    private ArrayList<HeaderOption> buildHeader(String name, Integer sequenceNo, String sourceIp, String targetIp, byte[] data) {
        ArrayList<HeaderOption> header = new ArrayList<>();

        header.add(new HeaderOption("name", name));
        header.add(new HeaderOption("sequence", Integer.toString(sequenceNo)));
        header.add(new HeaderOption("sourceip", sourceIp));
        header.add(new HeaderOption("targetip", targetIp));
        header.add(new HeaderOption("data", Base64.getEncoder().encodeToString(data)));

        //Calculate header byte size (VERY UGLY)
        int nbBytes = headerToString(header).getBytes().length;
        HeaderOption sizeOption = new HeaderOption("size", Integer.toString(nbBytes));
        header.add(sizeOption);
        //Newly calculated size
        nbBytes = headerToString(header).getBytes().length;
        sizeOption.value = Integer.toString(nbBytes);

        return header;
    }

    //Turns header into a string, ready to parse for the next layer
    private String headerToString(ArrayList<HeaderOption> header) {
        StringBuilder stringHeader = new StringBuilder();
        for(HeaderOption ho : header) {
            stringHeader.append(ho.toString()).append(',');
        }
        //Removes last comma
        stringHeader.deleteCharAt(stringHeader.length() - 1);
        return stringHeader.toString();
    }

    //Turns a string back into a header, very fragile
    private ArrayList<HeaderOption> stringToHeader(String stringHeader) {
        ArrayList<HeaderOption> header = new ArrayList<>();
        String[] stringHeaderOptions = stringHeader.replaceAll("(^.*?\\[|\\]\\s*$)","").split("\\]\\s*,\\s*\\[");
        for(String stringHeaderOption : stringHeaderOptions) {
            String[] headerOption = stringHeaderOption.split(":", 2);
            if(headerOption.length < 2) continue;
            header.add(new HeaderOption(headerOption[0], headerOption[1]));
        }
        return header;
    }

    //Gets the value of a HeaderOption in a header
    private String getValue(ArrayList<HeaderOption> header, String fieldName) {
        for(HeaderOption option : header) {
            if(option.name.equals(fieldName)) return option.value;
        }
        return "";
    }
}
