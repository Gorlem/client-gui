package com.ddoerr.clientgui.views;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.ShortcutAttachment;
import com.ddoerr.clientgui.supports.FocusChangeSupport;
import com.ddoerr.clientgui.models.ShortcutBuilder;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public abstract class AbstractView extends Widget<AbstractView> {
    protected final FocusChangeSupport focusChangeSupport = new FocusChangeSupport(this);

    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();

    public AbstractView() {
        attach(containerAttachment);
        attach(shortcutAttachment);

        focusListeners.addListener(this);

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.tab").build(), e -> focusChangeSupport.changeFocus(FocusChangeSupport.Direction.Forwards));
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.tab").andShift().build(), e -> focusChangeSupport.changeFocus(FocusChangeSupport.Direction.Backwards));

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.escape").build(), e -> MinecraftClient.getInstance().openScreen(null));
    }

    public void setChild(Widget<?> widget) {
        containerAttachment.removeChildren();
        containerAttachment.addChild(widget);
    }

    public abstract void build();

    public Screen asScreen() {
        return new ViewScreenAdapter(this);
    }
}
