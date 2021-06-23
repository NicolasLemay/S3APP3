package s3app3.layers;

import s3app3.packets.Packet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * The application layer serves to read the contents of a specific file and extract its bytes and write bytes to a new file.
 */
public class ApplicationLayer extends LayerHandler {


    /**
     * Loads file data into packet for the next layer.
     * @param packet incoming packet with file location.
     * @return outgoing packet with byte data.
     */
    @Override
    public Packet send(Packet packet) {
        System.out.println("Application layer : send");
        Path path = Paths.get(NetConfig.clientFilesPath() + "/" + packet.getFileName());

        try {
            byte[] fileContent = Files.readAllBytes(path);
            packet.setData(fileContent);
            System.out.println("Successfully read file : " + path);
        } catch (IOException e) {
            System.out.println("Could not read file : " + path + " : " + e.getMessage());
            packet.setData(new byte[0]);
        }

        return packet;
    }


    /**
     * Creates a file from incoming packet data.
     * @param packet incoming packet with data.
     * @return outgoing packet with created file.
     */
    @Override
    public Packet receive(Packet packet) {
        System.out.println("Application layer : receive");

        byte[] fileContent = packet.getData();
        Path path = Paths.get(NetConfig.serverFilesPath() + "/" + packet.getFileName());

        try {
            Files.write(path, fileContent);
            System.out.println("Successfully wrote to file : " + path.toString());
        } catch (IOException e) {
            System.out.println("Could not write to file : " + path.toString() + " : " + e.getMessage());
        }

        return packet;
    }
}
