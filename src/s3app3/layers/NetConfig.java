package s3app3.layers;

/**
 * Utility class serving as a placeholder for application parameters.
 */
public class NetConfig {

    /**
     * @return listening port.
     */
    public static Integer getPort() {
        return 25001;
    }

    /**
     * @return the number of bytes of data that can be contained in one fragment.
     */
    public static Integer maxBytesPerFragment() {
        return 200;
    }

    /**
     * @return the relative path of the server files.
     */
    public static String serverFilesPath() {
        return "server_files";
    }


    /**
     * @return the relative path of the client files.
     */
    public static String clientFilesPath() {
        return "client_files";
    }
}
