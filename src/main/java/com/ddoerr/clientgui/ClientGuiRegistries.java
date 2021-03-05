package com.ddoerr.clientgui;

import com.ddoerr.clientgui.models.ColorPalette;
import com.mojang.serialization.Lifecycle;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.DefaultedRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class ClientGuiRegistries {
    public static final Identifier DEFAULT_WIDGET_IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "widget");
    private static final Identifier BACKGROUND_COLOR_IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "background_color");
    private static final Identifier BORDER_COLOR_IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "border_color");

    public static final Registry<ColorPalette> BACKGROUND_COLOR = new DefaultedRegistry<>(
            DEFAULT_WIDGET_IDENTIFIER.toString(),
            RegistryKey.ofRegistry(BACKGROUND_COLOR_IDENTIFIER),
            Lifecycle.experimental());

    public static final Registry<ColorPalette> BORDER_COLOR = new DefaultedRegistry<>(
            DEFAULT_WIDGET_IDENTIFIER.toString(),
            RegistryKey.ofRegistry(BORDER_COLOR_IDENTIFIER),
            Lifecycle.experimental());
}
