package s3app3.states;

import s3app3.interfaces.Handleable;
import s3app3.layers.LayerHandler;
import s3app3.packets.Packet;

public abstract class PacketHandlerState {

    public abstract Packet handleState(LayerHandler handler, Packet packet);
    public abstract LayerHandler getStartHandler(LayerHandler handler);
    public abstract LayerHandler getNextHandler(LayerHandler handler);
}
