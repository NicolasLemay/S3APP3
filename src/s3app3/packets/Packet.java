package packets;

import java.io.File;
import java.util.*;

public class Packet {

    private File file;
    private String sourceIP;
    private String targetIP;
    private byte[] data;
    private ArrayList<byte[]> fragments;

    public Packet(String filePath) {

        file = new File(filePath);
        fragments = new ArrayList<>();
    }

    public void setFilePath(String filePath) {
        file = new File(filePath);
    }

    public String getFileName() {
        return file.getName();
    }

    public String getFilePath() {
        return file.getPath();
    }

    public File getFile() {
        return file;
    }

    public ArrayList<byte[]> getFragments() {
        return fragments;
    }

    public void setFragments(ArrayList<byte[]> fragments) {
        this.fragments = fragments;
    }

    public void addFragment(byte[] fragment) {
        fragments.add(fragment);
    }

    public Integer getSize() {
        return fragments.size();
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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        StringBuilder fragmentString = new StringBuilder("[\n");
        for(byte[] b : fragments) {
            fragmentString.append('{').append(new String(b)).append("},\n");
        }
        fragmentString.append(']');
        return "Packet{" +
                "file=" + file +
                ", sourceIP='" + sourceIP + '\'' +
                ", targetIP='" + targetIP + '\'' +
                ", data=" + Arrays.toString(data) +
                ", fragments=" + fragmentString +
                '}';
    }
}
