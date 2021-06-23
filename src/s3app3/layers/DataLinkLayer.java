package s3app3.layers;

import s3app3.packets.Packet;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * The data link layer initializes sockets to send fragments of a packet throughout the network and verifies the checksum of fragments.
 */
public class DataLinkLayer extends LayerHandler {

    final int CHECKSUM_SIZE = 8;
    private LogFile logFile;

    public DataLinkLayer() {
        logFile = LogFile.getInstance();
    }

    /**
     * Generates CRC for a fragment and sends to the server.
     * @param packet incoming fragmented packet.
     * @return outgoing sent packet.
     */
    @Override
    public Packet send(Packet packet) {
        System.out.println("Datalink layer : send");
        int amountPacketSent = 0;
        logFile.addLog("Client", "Transmission initialization");

        String str3 = "fin";

        try {
            DatagramSocket socket = new DatagramSocket();
            InetAddress address = InetAddress.getByName("localhost");
            DatagramPacket receivedPacket;

            while(amountPacketSent < packet.getFragments().size()){
                byte[] msg = packet.getFragments().get(amountPacketSent);
                byte[] bytesPaquet = generateCheckSum(msg);
                DatagramPacket paquet = new DatagramPacket(bytesPaquet, bytesPaquet.length, address, NetConfig.getPort());
                socket.send(paquet);
                amountPacketSent++;

                byte[] buffer = new byte[256];
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(receivedPacket);
            }

            byte[] msg3 = str3.getBytes();
            byte[] bytesPaquet3 = generateCheckSum(msg3);
            DatagramPacket paquet3 = new DatagramPacket(bytesPaquet3, bytesPaquet3.length, address, NetConfig.getPort());
            socket.send(paquet3);

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }


    /**
     * Reads incoming packet and checks their CRC
     * @param packet incoming packet from client.
     * @return outgoing fragmented packet.
     */
    @Override
    public Packet receive(Packet packet) {
        int packetsReceived = 0;

        System.out.println("Datalink layer : receive : fileName");

        byte[] msgReceived = null;
        try {
            DatagramSocket server = new DatagramSocket(NetConfig.getPort());
            InetAddress address = InetAddress.getByName("localhost");
            byte[] buffer = null;
            DatagramPacket receivedPacket;

            while(true){
                buffer = new byte[1024];
                receivedPacket = new DatagramPacket(buffer, buffer.length);
                server.receive(receivedPacket);

                msgReceived = new byte[receivedPacket.getLength()];
                System.arraycopy(receivedPacket.getData(), 0, msgReceived, 0, receivedPacket.getLength());

                if (isEndOfTransmission(msgReceived)){
                    server.close();
                    break;
                }

                if(validateCheckSum(msgReceived)) {
                    logFile.addLog("Server", "Validated packet (" + new String(msgReceived) + ")");

                    byte[] newArray = new byte[msgReceived.length - 8];
                    System.arraycopy(msgReceived, 0, newArray, 0, msgReceived.length-CHECKSUM_SIZE);

                    packet.addFragment(newArray);


                    byte[] acknowledgement = new String("ack").getBytes();
                    DatagramPacket paquet = new DatagramPacket(acknowledgement, acknowledgement.length, receivedPacket.getAddress(), receivedPacket.getPort());
                    server.send(paquet);

                } else {
                    logFile.addLog("Server", "Invalidated packet (" + new String(msgReceived) + ")");
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return packet;
    }

    private boolean isEndOfTransmission(byte[] msg) {
        if(msg.length <= 8) System.out.println(new String(msg));
        byte[] endArray = new byte[msg.length-8];
        System.arraycopy(msg, 0, endArray, 0, msg.length-CHECKSUM_SIZE);
        String endMessage = new String(endArray, StandardCharsets.UTF_8);
        if (endMessage.equals("fin")){
            logFile.addLog("Server", "End of transmission");
            return true;
        }

        return false;
    }

    private byte[] generateCheckSum(byte [] msg){
        CRC32 crc = new CRC32();
        crc.update(msg);

        long checksumValue = crc.getValue();
        byte[] crcByte = new byte[Long.BYTES];
        for(int i = Long.BYTES -1; i >= 0; i--){
            crcByte[i] = (byte) (checksumValue & 0xFF);
            checksumValue >>= Byte.SIZE;
        }

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
        long checksumValue = crc.getValue();
        byte[] newCRC = new byte[Long.BYTES];
        for(int i = Long.BYTES -1; i >= 0; i--){
            newCRC[i] = (byte) (checksumValue & 0xFF);
            checksumValue >>= Byte.SIZE;
        }

        for (int i = 0; i < newCRC.length; i++){
            if(newCRC[i] != checksum[i]){
                return false;
            }
        }
        return true;
    }
}