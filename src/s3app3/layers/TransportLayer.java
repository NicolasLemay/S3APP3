package s3app3.layers;

import s3app3.packets.Packet;

public class TransportLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {
        System.out.println("Transport layer : send : IP");
        System.out.println(packet.toString());
        System.out.println("Changing IP to 192.168.0.2 and sending to next layer...");
        System.out.println("");
        packet.setSourceIP("192.168.0.2");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Transport layer : receive : IP");
        System.out.println(packet.toString());
        System.out.println("Changing IP to 192.168.0.4 and sending to next layer...");
        System.out.println("");
        packet.setSourceIP("192.168.0.4");
        return packet;
    }
}
