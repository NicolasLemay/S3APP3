package s3app3.layers;

import s3app3.packets.Packet;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

public class DataLinkLayer extends LayerHandler {

    final int CHECKSUM_SIZE = 8;

    @Override
    public Packet send(Packet packet) {
        int amountPacketSent = 0;
        System.out.println("Datalink layer : send");
        System.out.println(packet.toString());
        System.out.println("");

        String str3 = "fin";

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");

            while(amountPacketSent < packet.getFragments().size()){
                byte[] msg = packet.getFragments().get(amountPacketSent);
                byte[] bytesPaquet = generateCheckSum(msg);
                DatagramPacket paquet = new DatagramPacket(bytesPaquet, bytesPaquet.length, address, 25001);
                socket.send(paquet);
                amountPacketSent++;
                //receivedPacket = new DatagramPacket(buffer, buffer.length);
            }

            byte[] msg3 = str3.getBytes();
            byte[] bytesPaquet3 = generateCheckSum(msg3);
            DatagramPacket paquet3 = new DatagramPacket(bytesPaquet3, bytesPaquet3.length, address, 25001);
            socket.send(paquet3);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        int packetsReceived = 0;

        System.out.println("Datalink layer : receive : fileName");
        System.out.println(packet.toString());
        System.out.println("");

        byte[] msgReceived = null;
        try {
            DatagramSocket server = new DatagramSocket(25001);
            InetAddress address = InetAddress.getByName("localhost");
            byte[] buffer = null;
            DatagramPacket receivedPacket;

            while(true){
                buffer = new byte[256];
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                server.receive(receivedPacket);

                msgReceived = new byte[receivedPacket.getLength()];
                System.arraycopy(receivedPacket.getData(), 0, msgReceived, 0, receivedPacket.getLength());

                if (isEndOfTransmission(msgReceived)){
                    server.close();
                    break;
                }

                if(validateCheckSum(msgReceived)) {
                    System.out.println("Pacquet validated");

                    byte[] newArray = new byte[msgReceived.length - 8];
                    System.arraycopy(msgReceived, 0, newArray, 0, msgReceived.length-CHECKSUM_SIZE);

                    packet.addFragment(newArray);
                    byte[] acknowledgement = new byte[0];
                    DatagramPacket paquet = new DatagramPacket(acknowledgement, acknowledgement.length, address, 25001);
                    server.send(paquet);
                } else {
                    System.out.println("Pacquet invalid");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return packet;
    }

    private boolean isEndOfTransmission(byte[] msg) {
        System.out.println("End of transmission");
        byte[] endArray = new byte[msg.length-8];
        System.arraycopy(msg, 0, endArray, 0, msg.length-CHECKSUM_SIZE);
        String endMessage = new String(endArray, StandardCharsets.UTF_8);
        System.out.println(endMessage);
        if (endMessage.equals("fin")){
            return true;
        }

        return false;
    }

    private byte[] generateCheckSum(byte [] msg){
        CRC32 crc = new CRC32();
        crc.update(msg);

        byte[] crcByte = Long.toHexString(crc.getValue()).getBytes();
        byte[] finalMsg = new byte[msg.length + crcByte.length];

        System.arraycopy(msg, 0, finalMsg, 0, msg.length);
        System.arraycopy(crcByte, 0, finalMsg, msg.length, CHECKSUM_SIZE);

        return finalMsg;
    }

    private boolean validateCheckSum(byte[] decypher) {


        CRC32 crc = new CRC32();
        byte[] checksum = new byte[CHECKSUM_SIZE];
        byte[] newArray = new byte[decypher.length - 8];

        System.arraycopy(decypher, decypher.length-CHECKSUM_SIZE, checksum, 0, CHECKSUM_SIZE);
        System.arraycopy(decypher, 0, newArray, 0, decypher.length-CHECKSUM_SIZE);

        crc.update(newArray);
        byte [] newCRC = Long.toHexString(crc.getValue()).getBytes();

        for (int i = 0; i < newCRC.length; i++){
            if(newCRC[i] != checksum[i]){
                System.out.println("invalidated");
                return false;
            }
        }
        System.out.println("Validated");
        return true;
    }
}
