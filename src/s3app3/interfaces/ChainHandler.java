package s3app3.interfaces;

import s3app3.packets.Packet;

/**
 * Chain of responsibility interface for receiving and sending packets
 */
public interface ChainHandler extends Handleable {

    /**
     * Abstract method for handling the reception of a packet.
     * @param packet incoming packet.
     * @return outgoing packet.
     */
    Packet send(Packet packet);


    /**
     * Abstract method for handling the sending of a packet.
     * @param packet incoming packet.
     * @return outgoing packet.
     */
    Packet receive(Packet packet);
}
