package me.therealdan.dansengine.network;

import java.net.InetAddress;

public class Packet {

    private InetAddress address;
    private int port;
    protected String data;

    public Packet() {
        this("");
    }

    public Packet(String data, InetAddress address, int port) {
        this(data);
        setAddress(address, port);
    }

    public Packet(String data) {
        this.data = data;
    }

    public void setAddress(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public String getData() {
        return data;
    }
}