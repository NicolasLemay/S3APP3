import states.ReceiveState;

public class Server {

    public static void main(String[] args) {
        RedSocket r = new RedSocketServer();
        r.start();
    }
}
