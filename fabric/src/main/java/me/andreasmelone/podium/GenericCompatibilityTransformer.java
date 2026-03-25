package me.andreasmelone.podium;

import org.objectweb.asm.*;

import java.util.Set;

public class GenericCompatibilityTransformer {
    private static final Set<String> BOOLEAN_METHODS = Set.of(
            "isUsingPojavLauncher",
            "isUsingAmethystLauncher",
            "isUsingAndroidLauncher",
            "isRunningOnAndroid",
            "isMobile"
    );

    private static final Set<String> VOID_METHODS = Set.of(
            "checkLwjglRuntimeVersion"
    );

    public byte[] transform(byte[] classfileBuffer) {
        ClassReader reader = new ClassReader(classfileBuffer);
        ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

        reader.accept(new ClassVisitor(Opcodes.ASM9, writer) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
                if (desc.equals("()Z") && BOOLEAN_METHODS.contains(name)) {
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            super.visitInsn(Opcodes.ICONST_0);
                            super.visitInsn(Opcodes.IRETURN);
                        }
                    };
                }
                if (desc.equals("()V") && VOID_METHODS.contains(name)) {
                    return new MethodVisitor(Opcodes.ASM9, mv) {
                        @Override
                        public void visitCode() {
                            super.visitCode();
                            super.visitInsn(Opcodes.RETURN);
                        }
                    };
                }
                return mv;
            }
        }, 0);

        return writer.toByteArray();
    }
}
