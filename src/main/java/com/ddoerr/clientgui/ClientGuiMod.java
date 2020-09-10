package com.ddoerr.clientgui;

import com.ddoerr.clientgui.attacher.ScreenAttacher;
import com.ddoerr.clientgui.attacher.ViewScreenAdapter;
import com.ddoerr.clientgui.test.TestView;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class ClientGuiMod implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.clientgui.test",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "category.clientgui.test"
        ));

//        ScreenAttacher.attachToScreen(ChatScreen.class, () -> new ViewScreenAdapter(new TestView()));
//        ScreenAttacher.attachToScreen(TitleScreen.class, () -> new ViewScreenAdapter(new TestView()));
//        ScreenAttacher.attachToHud(() -> new ViewScreenAdapter(new TestView()));

//        new TestView().attachToHud();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (keyBinding.wasPressed()) {
//                client.openScreen(new TestView().asScreen());
//                Screen screen = new ChatScreen("");
//                new TestView().attachToScreen(screen);
                client.openScreen(new ViewScreenAdapter(new TestView()));
            }
        });
    }
}
