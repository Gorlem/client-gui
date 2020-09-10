package com.ddoerr.clientgui.modifications.mixin;

import com.ddoerr.clientgui.attacher.ScreenAttacher;
import net.minecraft.client.Mouse;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Mouse.class)
public abstract class MouseMixin {
    @Redirect(method = "net/minecraft/client/Mouse.method_1611([ZDDI)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.mouseClicked(DDI)Z"))
    private boolean proxyMouseClicked(Screen screen, double mouseX, double mouseY, int button) {
        boolean result = screen.mouseClicked(mouseX, mouseY, button);

        Screen proxy = ScreenAttacher.getProxyForScreen(screen);

        if (proxy != null) {
            proxy.mouseClicked(mouseX, mouseY, button);
        }

        return result;
    }

    @Redirect(method = "net/minecraft/client/Mouse.method_1605([ZDDI)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.mouseReleased(DDI)Z"))
    private boolean proxyMouseReleased(Screen screen, double mouseX, double mouseY, int button) {
        boolean result = screen.mouseReleased(mouseX, mouseY, button);

        Screen proxy = ScreenAttacher.getProxyForScreen(screen);

        if (proxy != null) {
            proxy.mouseReleased(mouseX, mouseY, button);
        }

        return result;
    }
}
