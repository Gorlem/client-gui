package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.ActionEvent;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.events.KeyboardListener;
import com.ddoerr.clientgui.models.Modifier;
import com.ddoerr.clientgui.models.Shortcut;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

public class ShortcutAttachment implements KeyboardListener, Attachment {
    private final Map<Shortcut, ActionListener> shortcuts = new HashMap<>();
    private Widget<?> widget;

    public void addShortcut(Shortcut shortcut, ActionListener actionListener) {
        shortcuts.put(shortcut, actionListener);
    }

    @Override
    public void initialize(Widget<?> widget) {
        this.widget = widget;
    }

    @Override
    public ActionResult keyDown(KeyboardEvent keyboardEvent) {
        for (Map.Entry<Shortcut, ActionListener> entry : shortcuts.entrySet()) {
            if (matchesKeys(entry.getKey(), keyboardEvent) && matchesRules(entry.getKey(), widget)) {
                entry.getValue().doAction(new ActionEvent(keyboardEvent.getSource()));
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    protected boolean matchesKeys(Shortcut shortcut, KeyboardEvent keyboardEvent) {
        return keyboardEvent.getKey().equals(shortcut.getKey()) && keyboardEvent.getModifiers().equals(shortcut.getModifiers());
    }

    protected boolean matchesRules(Shortcut shortcut, Widget<?> widget) {
        return shortcut.getRules().stream().allMatch(s -> s.test(widget));
    }
}
