package s3app3.interfaces;

import s3app3.packets.Packet;

public interface Handleable {
    Packet handle(Packet p);
}
