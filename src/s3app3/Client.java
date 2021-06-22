import packets.Packet;
import states.SendState;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        RedSocket r = new RedSocketClient();
        r.start();
    }
}
