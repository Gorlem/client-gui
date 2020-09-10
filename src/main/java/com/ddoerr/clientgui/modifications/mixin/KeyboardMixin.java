package com.ddoerr.clientgui.modifications.mixin;

import com.ddoerr.clientgui.attacher.ScreenAttacher;
import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
    @Redirect(method = "net/minecraft/client/Keyboard.method_1454(I[ZLnet/minecraft/client/gui/ParentElement;III)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/ParentElement.keyReleased(III)Z"))
    private boolean proxyKeyReleased(ParentElement parentElement, int keyCode, int scanCode, int modifiers) {
        boolean result = parentElement.keyReleased(keyCode, scanCode, modifiers);

        Screen proxy = ScreenAttacher.getProxyForScreen((Screen)parentElement);

        if (proxy != null) {
            proxy.keyReleased(keyCode, scanCode, modifiers);
        }

        return result;
    }

    @Redirect(method = "net/minecraft/client/Keyboard.method_1454(I[ZLnet/minecraft/client/gui/ParentElement;III)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/ParentElement.keyPressed(III)Z"))
    private boolean proxyKeyPressed(ParentElement parentElement, int keyCode, int scanCode, int modifiers) {
        boolean result = parentElement.keyPressed(keyCode, scanCode, modifiers);

        Screen proxy = ScreenAttacher.getProxyForScreen((Screen)parentElement);

        if (proxy != null) {
            proxy.keyPressed(keyCode, scanCode, modifiers);
        }

        return result;
    }

    @Redirect(method = "net/minecraft/client/Keyboard.method_1458(Lnet/minecraft/client/gui/Element;II)V", at = @At(value = "INVOKE", target = "net/minecraft/client/gui/Element.charTyped(CI)Z"))
    private static boolean proxyCharTyped(Element element, char chr, int keyCode) {
        boolean result = element.charTyped(chr, keyCode);

        Screen proxy = ScreenAttacher.getProxyForScreen((Screen)element);

        if (proxy != null) {
            proxy.charTyped(chr, keyCode);
        }

        return result;
    }
}
