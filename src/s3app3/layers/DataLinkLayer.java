package layers;

import packets.Packet;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class DataLinkLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {
        System.out.println("Datalink layer : send");
        System.out.println(packet.toString());
        System.out.println("");
        String msg = "Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!Hello World!";

        byte [] paquet = generateCheckSum(msg.getBytes());
        validateCheckSum(paquet);
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Datalink layer : receive : fileName");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    private byte[] generateCheckSum(byte [] msg){
        CRC32 crc = new CRC32();
        crc.update(msg);

        byte[] crcByte = Long.toHexString(crc.getValue()).getBytes();
        byte[] finalMsg = new byte[msg.length + crcByte.length];

        System.arraycopy(msg, 0, finalMsg, 0, msg.length);
        System.arraycopy(crcByte, 0, finalMsg, msg.length, 8);

        return finalMsg;
    }

    private boolean validateCheckSum(byte[] decypher) {
        CRC32 crc = new CRC32();
        byte[] checksum = new byte[8];
        byte[] newArray = new byte[decypher.length-8];

        System.arraycopy(decypher, decypher.length-8, checksum, 0, 8);
        System.arraycopy(decypher, 0, newArray, 0, decypher.length-8);

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
