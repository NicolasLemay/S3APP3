package s3app3;

import s3app3.layers.ApplicationLayer;
import s3app3.layers.DataLinkLayer;
import s3app3.layers.LayerHandler;
import s3app3.layers.TransportLayer;
import s3app3.packets.Packet;
import s3app3.states.PacketHandlerState;

public class RedSocket extends Thread {
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

    @Override
    public void run() {
        Packet packet = new Packet("bin", "test.txt");
        handler.handle(packet);
        while(true);
    }

}
