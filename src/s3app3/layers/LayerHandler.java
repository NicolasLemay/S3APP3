package s3app3.layers;


import s3app3.interfaces.ChainHandler;
import s3app3.packets.Packet;
import s3app3.states.PacketHandlerState;

/**
 * Abstract class representing a network layer.
 */
public abstract class LayerHandler implements ChainHandler {
    private LayerHandler nextHandler;
    private LayerHandler previousHandler;
    private PacketHandlerState state;

    /**
     * @return next layer in line.
     */
    public final LayerHandler getNext() {
        return nextHandler;
    }

    /**
     * Sets next layer in line
     * @param nextHandler next layer in line.
     */
    public final void setNext(LayerHandler nextHandler) {
        this.nextHandler = nextHandler;
        nextHandler.setPrevious(this);
    }

    /**
     * @return previous layer in line.
     */
    public final LayerHandler getPrevious() {
        return previousHandler;
    }

    private void setPrevious(LayerHandler previousHandler) {
        this.previousHandler = previousHandler;
    }

    /**
     * @return state of the layer, usually receive or send mode.
     */
    public final PacketHandlerState getState() {
        return state;
    }

    /**
     * Sets the state of the layer.
     * @param state the state of the layer, usually receive or send mode.
     */
    public final void setState(PacketHandlerState state) {
        this.state = state;

        //Sets the whole chain to the same state
        if(getPrevious() != null && getPrevious().getState() != state) getPrevious().setState(state);
        if(getNext() != null && getNext().getState() != state) getNext().setState(state);
    }

    /**
     * @return next layer handler that is to be handled, depending on the state.
     */
    public LayerHandler getNextToHandle() {
        return getState().getNextHandler(this);
    }

    /**
     * Handle the packet through the layer, and passes to the next layer to be handled.
     * @param packet the packet to be handled.
     * @return the handled packet.
     */
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
