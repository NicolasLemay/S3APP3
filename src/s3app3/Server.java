package s3app3;

import s3app3.states.ReceiveState;

public class Server {

    public static void main(String[] args) {
        RedSocket r = new RedSocket(new ReceiveState());
        r.start();
    }
}
