package ej.ratclient.packet;

public class Packet {

    public int readIndex = 0;
    public final byte[] payload;

    public Packet(byte[] payload) {
        this.payload = payload;
    }

    public int get() {
        return payload[readIndex++] & 0xff;
    }

    public byte[] getBytes(byte[] buffer, int offset, int length) {
        for (int i = offset; i < length; i++) {
            buffer[i] = (byte) get();
        }
        return buffer;
    }

    public int getInt() {
        return (getShort() << 16) | getShort();
    }

    public int getShort() {
        return (get() << 8) | get();
    }

    public boolean getBoolean() {
        return get() == 1;
    }

    public String getString() {
        int data = '\n';
        StringBuilder builder = new StringBuilder();
        while ((data = get()) != '\n') {
            builder.append((char) data);
        }
        return builder.toString();
    }

    public long getLong() {
        return ((long) getInt() << 32L) | getInt();
    }

    public int remaining() {
        return payload.length - readIndex;
    }

    public int getLength() {
        return payload.length;
    }

    public byte[] getPayload() {
        return payload;
    }
}