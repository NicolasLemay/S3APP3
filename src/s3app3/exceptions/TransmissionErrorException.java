package s3app3.exceptions;

public class TransmissionErrorException extends Exception {

    public TransmissionErrorException(String errorMessage) {
        super("TransmissionErrorException : " + errorMessage);
    }

}
