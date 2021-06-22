package s3app3;

import s3app3.layers.ApplicationLayer;
import s3app3.layers.DataLinkLayer;
import s3app3.layers.LayerHandler;
import s3app3.layers.TransportLayer;
import s3app3.states.PacketHandlerState;

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
