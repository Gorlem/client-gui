package com.ddoerr.clientgui.modifications.mixin;

import com.ddoerr.clientgui.attacher.ScreenAttacher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {
    @Redirect(method = "openScreen", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.init(Lnet/minecraft/client/MinecraftClient;II)V"))
    private void proxyInit(Screen screen, MinecraftClient minecraft, int width, int height) {
        screen.init(minecraft, width, height);

        Screen proxy = ScreenAttacher.getProxyForScreen(screen);

        if (proxy != null) {
            proxy.init(minecraft, width, height);
        }
    }

    @Redirect(method = "openScreen", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.removed()V"))
    private void proxyRemoved(Screen screen) {
        screen.removed();
        ScreenAttacher.removeProxyFromCache(screen);
    }

    @Redirect(method = "onResolutionChanged", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.resize(Lnet/minecraft/client/MinecraftClient;II)V"))
    private void proxyResize(Screen screen, MinecraftClient minecraft, int width, int height) {
        screen.resize(minecraft, width, height);

        Screen proxy = ScreenAttacher.getProxyForScreen(screen);

        if (proxy != null) {
            proxy.resize(minecraft, width, height);
        }
    }
}
