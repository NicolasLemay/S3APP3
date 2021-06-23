package s3app3;

/**
 * Main class for the server program.
 */
public class Server {

    public static void main(String[] args) {
        RedSocket r = new RedSocketServer();
        r.start();
    }
}
