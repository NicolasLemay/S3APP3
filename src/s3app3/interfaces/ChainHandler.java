package interfaces;

import packets.Packet;

import java.io.IOException;

public interface ChainHandler extends Handleable {
    Packet send(Packet packet);
    Packet receive(Packet packet);
}
