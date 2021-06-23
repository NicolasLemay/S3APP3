package s3app3;

import s3app3.packets.Packet;
import s3app3.states.SendState;

import java.io.IOException;
import java.util.Scanner;

public class RedSocketClient extends RedSocket {

    String fileName;

    /**
     * @param fileName name of the file to be handled.
     * @throws IOException
     */
    public RedSocketClient(String fileName) throws IOException {
        super(new SendState());
        this.fileName = fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Starts handling the packet.
     */
    @Override
    public void run() {
        Packet packetToSend = new Packet(fileName);
        getHandler().handle(packetToSend);
    }
}
