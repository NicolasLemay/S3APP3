package s3app3.layers;

import s3app3.packets.Packet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ApplicationLayer extends LayerHandler {

    @Override
    public Packet send(Packet packet) {
        System.out.println("Application layer : send");

        File file = packet.getFile();

        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            packet.setData(fileContent);
            System.out.println("Successfully read file : " + file.getPath());
        } catch (IOException e) {
            System.out.println("Could not read file : " + file.getPath() + " : " + e.getMessage());
        }

        System.out.println(packet.toString() + "\n");
        return packet;
    }

    @Override
    public Packet receive(Packet packet) {
        System.out.println("Application layer : receive");

        byte[] fileContent = packet.getData();
        Path path = Paths.get("./outputfiles/" + packet.getFileName());

        try {
            Files.write(path, fileContent);
            System.out.println("Successfully wrote to file : " + path.toString());
        } catch (IOException e) {
            System.out.println("Could not write to file : " + path.toString() + " : " + e.getMessage());
        }

        System.out.println(packet.toString() + "\n");
        return packet;
    }
}
