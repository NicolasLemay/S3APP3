package s3app3.interfaces;

import s3app3.packets.Packet;

/**
 * Simple interface for implementing the handling of a packet.
 */
public interface Handleable {

    /**
     * Abstract method defining how to handle a packet.
     * @param p incoming packet.
     * @return outgoing packet.
     */
    Packet handle(Packet p);
}
