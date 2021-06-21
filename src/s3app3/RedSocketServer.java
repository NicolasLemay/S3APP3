import packets.Packet;
import states.ReceiveState;

public class RedSocketServer extends RedSocket {

    public RedSocketServer() {
        super(new ReceiveState());
    }

    @Override
    public void run() {
        Packet packet = new Packet("./test.txt");
        getHandler().handle(packet);
        while(true);
    }
}
