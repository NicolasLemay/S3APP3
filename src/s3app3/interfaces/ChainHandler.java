package s3app3.interfaces;

import s3app3.packets.Packet;

import java.io.IOException;

public interface ChainHandler extends Handleable {
    Packet send(Packet packet);
    Packet receive(Packet packet);
}
