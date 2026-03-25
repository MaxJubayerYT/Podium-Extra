package me.andreasmelone.podium;

import java.lang.management.ManagementFactory;

public final class PodiumSafety {
    private static volatile boolean warned;

    private PodiumSafety() {}

    public static void emitRuntimeNotice() {
        if (warned) {
            return;
        }

        warned = true;
        double totalGiB = detectTotalRamGiB();
        System.out.println("[Podium] Launcher checks were bypassed for mobile renderer compatibility.");
        System.out.println("[Podium] This does not fix renderer bugs (white textures, driver resets, or GPU timeouts).");
        if (totalGiB > 0) {
            double low = totalGiB * 0.30;
            double high = totalGiB * 0.40;
            System.out.printf("[Podium] Suggested JVM memory: 30%%-40%% of system RAM (%.2f-%.2f GiB).%n", low, high);
        } else {
            System.out.println("[Podium] Suggested JVM memory factor: M_jvm = R_total * phi, where phi = 0.30-0.40.");
        }
        System.out.println("[Podium] Safe mode recommendation: use 1280x720 internal resolution for translation-layer stability.");
    }

    private static double detectTotalRamGiB() {
        try {
            var osBean = ManagementFactory.getPlatformMXBean(com.sun.management.OperatingSystemMXBean.class);
            if (osBean == null) {
                return -1;
            }
            return osBean.getTotalMemorySize() / 1024.0 / 1024.0 / 1024.0;
        } catch (Throwable ignored) {
            return -1;
        }
    }
}
