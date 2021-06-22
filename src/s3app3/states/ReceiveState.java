package states;

import layers.LayerHandler;
import packets.Packet;

import java.io.IOException;

public class ReceiveState extends PacketHandlerState {

    @Override
    public Packet handleState(LayerHandler handler, Packet packet) {
        return handler.receive(packet);
    }

    @Override
    public LayerHandler getStartHandler(LayerHandler handler) {
        LayerHandler last = handler;
        while(last.getNext() != null) {
            last = last.getNext();
        }
        return last;
    }

    @Override
    public LayerHandler getNextHandler(LayerHandler handler) {
        return handler.getPrevious();
    }
}
