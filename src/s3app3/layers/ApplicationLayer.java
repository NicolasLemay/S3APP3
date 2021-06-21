package s3app3.layers;

import s3app3.packets.Packet;

public class ApplicationLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {
        System.out.println("Application layer : send : Filename");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Application layer : receive : IP");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }
}
