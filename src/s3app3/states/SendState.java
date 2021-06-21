package s3app3.states;

import s3app3.layers.LayerHandler;
import s3app3.packets.Packet;

public class SendState extends PacketHandlerState {

    @Override
    public Packet handleState(LayerHandler handler, Packet packet) {
        return handler.send(packet);
    }

    @Override
    public LayerHandler getStartHandler(LayerHandler handler) {
        LayerHandler first = handler;
        while(first.getPrevious() != null) {
            first = first.getPrevious();
        }
        return first;
    }

    @Override
    public LayerHandler getNextHandler(LayerHandler handler) {
        return handler.getNext();
    }
}
