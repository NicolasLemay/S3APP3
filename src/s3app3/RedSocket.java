import layers.ApplicationLayer;
import layers.DataLinkLayer;
import layers.LayerHandler;
import layers.TransportLayer;
import packets.Packet;
import states.PacketHandlerState;

public abstract class RedSocket extends Thread {
    private LayerHandler handler;

    public RedSocket(PacketHandlerState state) {
        ApplicationLayer applicationLayer = new ApplicationLayer();
        TransportLayer transportLayer = new TransportLayer();
        DataLinkLayer dataLinkLayer = new DataLinkLayer();

        applicationLayer.setNext(transportLayer);
        transportLayer.setNext(dataLinkLayer);

        handler = applicationLayer;
        handler.setState(state);
    }

    public LayerHandler getHandler() {
        return handler;
    }

}
