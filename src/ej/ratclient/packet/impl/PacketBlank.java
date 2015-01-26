package ej.ratclient.packet.impl;

import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.Packet;

public class PacketBlank implements PacketType {

    @Override
    public void handlePacket(Packet packet) {
    }
}
