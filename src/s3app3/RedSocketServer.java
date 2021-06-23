package s3app3;

import s3app3.packets.Packet;
import s3app3.states.ReceiveState;

public class RedSocketServer extends RedSocket {

    public RedSocketServer() {
        super(new ReceiveState());
    }

    /**
     * Starts handling packets.
     */
    @Override
    public void run() {
        while(true) {
            Packet packet = new Packet();
            getHandler().handle(packet);
        }
    }
}
