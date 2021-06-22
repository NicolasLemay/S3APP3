package s3app3;

import java.io.IOException;

public class Client {

    public static void main(String[] args) throws IOException {
        RedSocket r = new RedSocketClient();
        r.start();
    }
}
