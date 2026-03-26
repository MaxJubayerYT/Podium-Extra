package me.andreasmelone.podium.mixin;

import me.andreasmelone.podium.PodiumSafety;
import me.andreasmelone.podium.RendererGuard;
import net.caffeinemc.mods.sodium.client.compatibility.checks.PostLaunchChecks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PostLaunchChecks.class)
public class PostLaunchChecksMixin {
    @Inject(
            at = @At("HEAD"),
            method = "isUsingPojavLauncher()Z",
            cancellable = true,
            remap = false
    )
    private static void isUsingPojavMixin(CallbackInfoReturnable<Boolean> cir) {
        if (RendererGuard.shouldBypassMobileBlocks()) {
            PodiumSafety.emitRuntimeNotice();
            cir.setReturnValue(false);
        }
    }

    @Redirect(
            method = "isUsingPojavLauncher()Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/System;getProperty(Ljava/lang/String;)Ljava/lang/String;"),
            remap = false,
            require = 0
    )
    private static String scrubPojavProperties(String key) {
        if (RendererGuard.shouldBypassMobileBlocks()) {
            String replacement = RendererGuard.scrubSignatureKey(key);
            if (replacement != null) {
                return replacement;
            }
        }

        return System.getProperty(key);
    }

    @Redirect(
            method = "isUsingPojavLauncher()Z",
            at = @At(value = "INVOKE", target = "Ljava/lang/System;getenv(Ljava/lang/String;)Ljava/lang/String;"),
            remap = false,
            require = 0
    )
    private static String scrubPojavEnvironment(String key) {
        if (RendererGuard.shouldBypassMobileBlocks()) {
            String replacement = RendererGuard.scrubSignatureKey(key);
            if (replacement != null) {
                return replacement;
            }
        }

        return System.getenv(key);
    }
}
