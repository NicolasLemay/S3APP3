package s3app3;

public class Server {

    public static void main(String[] args) {
        RedSocket r = new RedSocketServer();
        r.start();
    }
}
