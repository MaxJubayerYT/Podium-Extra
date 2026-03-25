package me.andreasmelone.podium;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PodiumMixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        PodiumSafety.emitRuntimeNotice();
        ClassLoader loader = PodiumMixinPlugin.class.getClassLoader();
        GenericCompatibilityTransformer transformer = new GenericCompatibilityTransformer();
        for (Util.ClassEntry classEntry : Util.findSodiumCompatibilityClasses()) {
            byte[] transformed = transformer.transform(classEntry.bytes());
            Util.defineClass(loader, classEntry.className(), transformed);
        }
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}
