package ej.ratclient.util;

public class OperatingSystem {

    public static final OperatingSystems OPERATING_SYSTEM;

    static {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows")) {
            OPERATING_SYSTEM = OperatingSystems.WINDOWS;
        } else if (osName.startsWith("Mac")) {
            OPERATING_SYSTEM = OperatingSystems.MAC;
        } else if (osName.contains("Linux") || osName.contains("Ubuntu") || osName.contains("Fedora")) {
            OPERATING_SYSTEM = OperatingSystems.LINUX;
        } else {
            OPERATING_SYSTEM = OperatingSystems.OTHER;
        }
    }

    public enum OperatingSystems {

        WINDOWS {

            @Override
            public String toString() {
                return "Windows";
            }
        }, MAC {

            @Override
            public String toString() {
                return "Mac";
            }
        }, LINUX {

            @Override
            public String toString() {
                return "Linux";
            }
        }, OTHER {

            @Override
            public String toString() {
                return "Other";
            }
        };
    }
}
