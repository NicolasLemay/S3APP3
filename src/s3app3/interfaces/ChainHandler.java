package interfaces;

import packets.Packet;

public interface ChainHandler extends Handleable {
    Packet send(Packet packet);
    Packet receive(Packet packet);
}
