package com.ddoerr.clientgui;

import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.test.TestView;
import com.ddoerr.clientgui.models.ColorPaletteBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.registry.Registry;
import org.lwjgl.glfw.GLFW;

public class ClientGuiMod implements ClientModInitializer {
    public static String CLIENT_GUI_NAMESPACE = "clientgui";

    @Override
    public void onInitializeClient() {
        Registry.register(
                ClientGuiRegistries.BACKGROUND_COLOR,
                ClientGuiRegistries.DEFAULT_WIDGET_IDENTIFIER,
                ColorPaletteBuilder
                        .base(Color.BLACK.addAlpha(0.4))
                        .active(Color.BLACK)
                        .highlight(Color.BLACK)
                        .disabled(Color.GREY.addAlpha(0.4))
                        .build());

        Registry.register(
                ClientGuiRegistries.BORDER_COLOR,
                ClientGuiRegistries.DEFAULT_WIDGET_IDENTIFIER,
                ColorPaletteBuilder
                        .base(Color.BLACK.addAlpha(0.7))
                        .focused(Color.BLUE.addAlpha(0.7))
                        .disabled(Color.GREY.addAlpha(0.7))
                        .build());


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
                client.openScreen(new TestView().asScreen());
            }
        });
    }
}
