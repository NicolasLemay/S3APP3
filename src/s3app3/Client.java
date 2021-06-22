package s3app3;

import s3app3.packets.Packet;
import s3app3.states.SendState;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        RedSocket r = new RedSocketClient();
        r.start();
    }
}
