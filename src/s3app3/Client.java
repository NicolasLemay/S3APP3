package s3app3;

import s3app3.packets.Packet;
import s3app3.states.SendState;

import java.io.IOException;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(" File path : ");
            String fileName = scanner.nextLine();

            if(fileName.equals("")) break;

            Packet packetToSend = new Packet(fileName);
            RedSocket r = new RedSocketClient(packetToSend);
            r.start();
        }
    }
}
