package interfaces;

import packets.Packet;

public interface Handleable {
    Packet handle(Packet p);
}
