package com.salesforce.test.util;

import com.salesforce.test.core.exceptions.AutomationBugException;

public final class SystemNameProvider {

    private static OS os;

    private SystemNameProvider() {
    }

    public static boolean isWindows() {
        return OS.WINDOWS == getOS();
    }

    private static OS getOS() {
        if (os == null) {
            var operSys = System.getProperty("os.name").toLowerCase();
            if (operSys.contains("win")) {
                os = OS.WINDOWS;
            } else if (operSys.contains("nix") || operSys.contains("nux") || operSys.contains("aix")) {
                os = OS.LINUX;
            } else if (operSys.contains("mac")) {
                os = OS.MAC;
            } else {
                throw new AutomationBugException("Current system not identified: " + operSys);
            }
        }
        return os;
    }

    private enum OS {
        WINDOWS, LINUX, MAC
    }
}
