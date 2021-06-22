package s3app3.layers;


import s3app3.interfaces.ChainHandler;
import s3app3.packets.Packet;
import s3app3.states.PacketHandlerState;

public abstract class LayerHandler implements ChainHandler {
    private LayerHandler nextHandler;
    private LayerHandler previousHandler;
    private PacketHandlerState state;

    public final LayerHandler getNext() {
        return nextHandler;
    }

    public final void setNext(LayerHandler nextHandler) {
        this.nextHandler = nextHandler;
        nextHandler.setPrevious(this);
    }

    public final LayerHandler getPrevious() {
        return previousHandler;
    }

    private void setPrevious(LayerHandler previousHandler) {
        this.previousHandler = previousHandler;
    }

    public final PacketHandlerState getState() {
        return state;
    }

    public final void setState(PacketHandlerState state) {
        this.state = state;

        //Sets the whole chain to the same state
        if(getPrevious() != null && getPrevious().getState() != state) getPrevious().setState(state);
        if(getNext() != null && getNext().getState() != state) getNext().setState(state);
    }

    public LayerHandler getNextToHandle() {
        return getState().getNextHandler(this);
    }

    @Override
    public final Packet handle(Packet packet) {
        return getState().getStartHandler(this).handleLayer(packet);
    }

    private Packet handleLayer(Packet packet) {
        Packet result = getState().handleState(this, packet);
        LayerHandler next = getNextToHandle();
        if(next != null) {
            return next.handleLayer(result);
        }
        return packet;
    }
}
