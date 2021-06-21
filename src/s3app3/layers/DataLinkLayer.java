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
        generateCheckSum(msg.getBytes());
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Datalink layer : receive : fileName");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    private void generateCheckSum(byte [] msg){

        CRC32 crc = new CRC32();
        crc.update(msg);
        byte[] crcByte = Long.toHexString(crc.getValue()).getBytes();
        System.out.println(Long.toHexString(crc.getValue()).getBytes());

        System.out.println(msg.length+" "+  crcByte.length);

        byte[] finalMsg = new byte[msg.length + crcByte.length];

        System.out.println(finalMsg);
        crc.update(finalMsg);
        System.out.println(Long.toHexString(crc.getValue()));
    }

    private boolean validateCheckSum() {
        return true;
    }
}
