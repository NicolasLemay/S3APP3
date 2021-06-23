package s3app3.exceptions;

/**
 * Error that can be thrown during the processing of a packet.
 */
public class TransmissionErrorException extends Exception {

    /**
     * Throws an error with the given message.
     * @param errorMessage the message to append to the error.
     */
    public TransmissionErrorException(String errorMessage) {
        super("TransmissionErrorException : " + errorMessage);
    }

}
