package me.andreasmelone.podium;

import sun.misc.Unsafe;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class Util {
    public record ClassEntry(String className, byte[] bytes) {}

    public static List<ClassEntry> findSodiumCompatibilityClasses() {
        List<ClassEntry> classes = new ArrayList<>();
        File modsDir = new File("mods");
        File[] files = modsDir.listFiles();
        if (files == null) return classes;

        for (File mod : files) {
            if (!mod.getName().endsWith(".jar")) continue;
            try (JarFile ignored = new JarFile(mod)) {
                scanJar(Files.readAllBytes(mod.toPath()), classes);
            } catch (IOException ignoredEx) {
            }
        }
        return classes;
    }

    private static void scanJar(byte[] jarBytes, List<ClassEntry> classes) {
        try (JarInputStream jis = new JarInputStream(new ByteArrayInputStream(jarBytes))) {
            JarEntry entry;
            while ((entry = jis.getNextJarEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith(".class") && name.startsWith("net/caffeinemc/mods/sodium/client/compatibility/")) {
                    classes.add(new ClassEntry(name.substring(0, name.length() - 6).replace('/', '.'), jis.readAllBytes()));
                }
                jis.closeEntry();
            }
        } catch (IOException ignored) {
        }
    }

    public static Class<?> defineClass(ClassLoader loader, String className, byte[] bytecode) {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe"),
                    f1 = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            f1.setAccessible(false);
            Unsafe unsafe = (Unsafe) f.get(null);
            int i;
            for (i = 0; unsafe.getBoolean(f, i) == unsafe.getBoolean(f1, i); i++) ;
            Field f2 = Unsafe.class.getDeclaredField("theInternalUnsafe");
            unsafe.putBoolean(f2, i, true);
            Object internalUnsafe = f2.get(null);

            Method defineClass = internalUnsafe.getClass().getDeclaredMethod("defineClass",
                    String.class, byte[].class, int.class, int.class,
                    ClassLoader.class, ProtectionDomain.class);
            unsafe.putBoolean(defineClass, i, true);

            return (Class<?>) defineClass.invoke(internalUnsafe,
                    className, bytecode, 0, bytecode.length,
                    loader, null);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
    }
}
