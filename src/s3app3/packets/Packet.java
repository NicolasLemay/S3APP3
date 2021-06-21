package s3app3.packets;

import java.util.Arrays;

public class Packet {

    private String fileName;
    private String filePath;
    private Integer size;
    private Integer sequenceNumber;
    private String sourceIP;
    private String targetIP;
    private Byte[] data;

    public Packet(String filePath, String fileName) {
        this.filePath = filePath;
        this.fileName = fileName;
        //TODO import data and determine size
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSourceIP() {
        return sourceIP;
    }

    public void setSourceIP(String sourceIP) {
        this.sourceIP = sourceIP;
    }

    public String getTargetIP() {
        return targetIP;
    }

    public void setTargetIP(String targetIP) {
        this.targetIP = targetIP;
    }

    public Byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", size=" + size +
                ", sequenceNumber=" + sequenceNumber +
                ", sourceIP='" + sourceIP + '\'' +
                ", targetIP='" + targetIP + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }
}
