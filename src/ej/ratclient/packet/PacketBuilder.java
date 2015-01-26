package ej.ratclient.packet;

public class PacketBuilder {

    public int packetLength = 0;
    public byte[] payload = new byte[32];

    public PacketBuilder(int opcode) {
        put(opcode);
    }

    public PacketBuilder put(int i) {
        ensureCapacity(packetLength + 1);
        payload[packetLength++] = (byte) i;
        return this;
    }

    public PacketBuilder putBoolean(boolean val) {
        return put(val == true ? 1 : 0);
    }

    public PacketBuilder putBytes(byte[] buffer, int offset, int length) {
        for (int i = offset; i < length; i++) {
            put(buffer[i]);
        }
        return this;
    }

    public PacketBuilder putInt(int i) {
        ensureCapacity(packetLength + 4);
        return putShort(i >> 16).putShort(i);
    }

    public PacketBuilder putShort(int i) {
        ensureCapacity(packetLength + 2);
        return put(i >> 8).put(i);
    }

    public PacketBuilder putString(String s) {
        for (byte b : s.getBytes()) {
            put(b);
        }
        return put('\n');
    }

    public PacketBuilder putLong(long i) {
        return putInt((int) (i >> 32)).putInt((int) i);
    }

    public void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity >= payload.length) {
            expandCapacity(minimumCapacity);
        }
    }

    public void expandCapacity(int minimumCapacity) {
        int newCapacity = (payload.length + 1) * 2;
        if (minimumCapacity > newCapacity) {
            newCapacity = minimumCapacity;
        }
        byte[] newPayload = new byte[newCapacity];
        try {
            while (packetLength > payload.length) {
                packetLength--;
            }
            System.arraycopy(payload, 0, newPayload, 0, packetLength);
        } catch (Exception e) {
        }
        payload = newPayload;
    }

    public Packet toPacket() {

        byte[] data = new byte[packetLength + 2];

        data[0] = (byte) (packetLength >> 8);
        data[1] = (byte) packetLength;

        System.arraycopy(payload, 0, data, 2, packetLength);

        return new Packet(data);
    }
}