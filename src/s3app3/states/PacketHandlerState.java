package states;

import interfaces.Handleable;
import layers.LayerHandler;
import packets.Packet;

public abstract class PacketHandlerState {

    public abstract Packet handleState(LayerHandler handler, Packet packet);
    public abstract LayerHandler getStartHandler(LayerHandler handler);
    public abstract LayerHandler getNextHandler(LayerHandler handler);
}
