package com.ddoerr.clientgui.modifications.mixin;

import com.ddoerr.clientgui.attacher.ScreenAttacher;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/screen/Screen.render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V"))
    private void proxyRender(Screen screen, MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        screen.render(matrixStack, mouseX, mouseY, delta);

        Screen proxy = ScreenAttacher.getProxyForScreen(screen);

        if (proxy != null) {
            proxy.render(matrixStack, mouseX, mouseY, delta);
        }
    }
}
