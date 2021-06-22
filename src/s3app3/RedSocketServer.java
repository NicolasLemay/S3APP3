package s3app3;

import s3app3.packets.Packet;
import s3app3.states.ReceiveState;

public class RedSocketServer extends RedSocket {

    public RedSocketServer() {
        super(new ReceiveState());
    }

    @Override
    public void run() {
        Packet packet = new Packet("test.txt");
        getHandler().handle(packet);
        while(true);
    }
}
