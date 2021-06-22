import packets.Packet;
import states.SendState;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class RedSocketClient extends RedSocket {

    public RedSocketClient() throws IOException {
        super(new SendState());
    }

    @Override
    public void run() {

        Scanner scanner = new Scanner(System.in);
        while(true) {
            System.out.print(" File path : ");
            String fileName = scanner.nextLine();

            if(fileName.equals("")) break;

            Packet packetToSend = new Packet(fileName);
            getHandler().handle(packetToSend);
        }

        /*try {
            DatagramSocket socket = new DatagramSocket();

            Packet reformatedPacket = getHandler().handle(packetToSend);
            byte[] buf = reformatedPacket.getData();

            DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getLocalHost(), NetConfig.getPort());
            socket.send(packet);

            // get response
            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            socket.receive(packet);

            // display response
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Message " + received + " received");

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
