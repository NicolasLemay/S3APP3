package layers;

import packets.Packet;
import layers.LayerHandler;

public class TransportLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {



        System.out.println("Transport layer : send : IP");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {



        System.out.println("Transport layer : receive : IP");
        System.out.println(packet.toString());
        System.out.println("");
        return packet;
    }
}
