package me.therealdan.dansengine.network;

import me.therealdan.dansengine.main.Error;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Network {

    private static Network network;

    private HashSet<Client> clients = new HashSet<>();
    private HashSet<Server> servers = new HashSet<>();

    private DatagramSocket clientSocket, serverSocket;
    private Thread sendToClient, sendToServer;
    private InetAddress serverAddress;
    private int serverPort;
    private boolean hosting = false;
    private boolean connected = false;

    private int PACKET_SIZE = 128;

    public Network() {
        if (network == null) network = this;
    }

    public void register(Client client) {
        clients.add(client);
    }

    public void register(Server server) {
        servers.add(server);
    }

    public void unregister(Client client) {
        clients.remove(client);
    }

    public void unregister(Server server) {
        servers.remove(server);
    }

    public void hostServer(int port) {
        try {
            this.serverSocket = new DatagramSocket(port);

            Thread waitForClient = new Thread("WaitForClient") {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Packet packet = waitForClients();
                            for (Server server : servers)
                                server.receivePacket(packet);
                        } catch (Exception e) {
                            new Error(e, "WaitForClient Thread");
                        }
                    }
                }
            };
            waitForClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.hosting = true;
    }

    public void connectToServer(String address, int port) {
        try {
            this.serverPort = port;
            this.serverAddress = InetAddress.getByName(address);
            this.clientSocket = new DatagramSocket();

            Thread waitForServer = new Thread("WaitForServer") {
                @Override
                public void run() {
                    while (true) {
                        try {
                            Packet packet = waitForServer();
                            for (Client client : clients)
                                client.receivePacket(packet);
                        } catch (Exception e) {
                            new Error(e, "WaitForServer Thread");
                        }
                    }
                }
            };
            waitForServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.connected = true;
    }

    public void sendPacket(Packet packet) {
        if (packet.getAddress() == null) {
            sendToServer(packet.getData());
        } else {
            sendToClient(packet.getData(), packet.getAddress(), packet.getPort());
        }
    }

    private void sendToServer(String data) {
        final byte[] bytes = data.getBytes();
        sendToServer = new Thread("Client") {
            @Override
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, serverAddress, serverPort);
                    clientSocket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sendToServer.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        sendToServer.start();
    }

    private void sendToClient(String data, InetAddress address, int port) {
        final byte[] bytes = data.getBytes();
        sendToClient = new Thread("Server") {
            @Override
            public void run() {
                try {
                    DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
                    serverSocket.send(packet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    sendToClient.interrupt();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        sendToClient.start();
    }

    public void disconnectFromServer() {
        if (clientSocket == null) return;

        try {
            this.clientSocket.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.connected = false;
    }

    public void stopServer() {
        if (serverSocket == null) return;

        try {
            this.serverSocket.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.hosting = false;
    }

    public boolean isHosting() {
        return hosting;
    }

    public boolean isConnected() {
        return connected;
    }

    private Packet waitForServer() {
        try {
            byte[] bytes = new byte[PACKET_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            clientSocket.receive(datagramPacket);
            return new Packet(new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()), datagramPacket.getAddress(), datagramPacket.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Packet waitForClients() {
        try {
            byte[] bytes = new byte[PACKET_SIZE];
            DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
            serverSocket.receive(datagramPacket);
            return new Packet(new String(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength()), datagramPacket.getAddress(), datagramPacket.getPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Client> getClients() {
        return new ArrayList<>(clients);
    }

    public List<Server> getServers() {
        return new ArrayList<>(servers);
    }

    public static Network getInstance() {
        if (network == null) network = new Network();
        return network;
    }
}