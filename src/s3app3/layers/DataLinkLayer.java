package s3app3.layers;

import s3app3.packets.Packet;

public class DataLinkLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {
        System.out.println("Datalink layer : send");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Datalink layer : receive : fileName");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }
}
