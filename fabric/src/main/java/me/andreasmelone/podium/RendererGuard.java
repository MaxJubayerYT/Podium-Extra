package me.andreasmelone.podium;

import java.util.Locale;

public final class RendererGuard {
    private static final String[] RENDERER_MARKERS = {
            "mobileglues",
            "ltw",
            "zink"
    };

    private RendererGuard() {}

    public static boolean shouldBypassMobileBlocks() {
        return containsRendererMarker(read("pojav.renderer"))
                || containsRendererMarker(read("pojav.render_backend"))
                || containsRendererMarker(read("org.lwjgl.opengl.libname"))
                || containsRendererMarker(System.getenv("POJAV_RENDERER"))
                || containsRendererMarker(System.getenv("POJAV_RENDER_BACKEND"));
    }

    public static String scrubSignatureKey(String key) {
        if (key == null) {
            return null;
        }

        String normalized = key.toLowerCase(Locale.ROOT);
        if (normalized.contains("pojav") || normalized.contains("amethyst") || normalized.contains("android")) {
            return "";
        }

        return null;
    }

    private static String read(String key) {
        try {
            return System.getProperty(key);
        } catch (Throwable ignored) {
            return null;
        }
    }

    private static boolean containsRendererMarker(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }

        String normalized = value.toLowerCase(Locale.ROOT);
        for (String marker : RENDERER_MARKERS) {
            if (normalized.contains(marker)) {
                return true;
            }
        }

        return false;
    }
}
