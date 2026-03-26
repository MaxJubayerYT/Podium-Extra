package me.andreasmelone.podium.mixin;

import me.andreasmelone.podium.RendererGuard;
import net.caffeinemc.mods.sodium.client.compatibility.checks.PreLaunchChecks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PreLaunchChecks.class)
public abstract class PreLaunchChecksMixin {
    @Inject(
            method = "checkLwjglRuntimeVersion()V",
            at = @At("HEAD"),
            cancellable = true,
            remap = false
    )
    private static void checkLwjglRuntimeVersionMixin(CallbackInfo ci) {
        if (RendererGuard.shouldBypassMobileBlocks()) {
            ci.cancel();
        }
    }
}
