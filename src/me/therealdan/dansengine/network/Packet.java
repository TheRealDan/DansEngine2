package me.therealdan.dansengine.network;

import java.net.InetAddress;

public class Packet {

    private InetAddress address;
    private int port;
    private String data;

    public Packet(String data, InetAddress address, int port) {
        this.data = data;
        this.address = address;
        this.port = port;
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