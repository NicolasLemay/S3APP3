package s3app3;

import s3app3.states.SendState;

public class Client {

    public static void main(String[] args) {
        RedSocket r = new RedSocket(new SendState());
        r.start();
    }
}
