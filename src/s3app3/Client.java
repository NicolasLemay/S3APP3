package s3app3;

import java.io.IOException;

/**
 * Main class for the client program. The first parameter passed to the program must be the file to be sent.
 */
public class Client {

    public static void main(String[] args) throws IOException {

        if(args.length < 1) {
            System.out.println("Missing first parameter \"fileName\". Shutting down...");
            return;
        }

        RedSocket r = new RedSocketClient(args[0]);
        r.start();
    }
}
