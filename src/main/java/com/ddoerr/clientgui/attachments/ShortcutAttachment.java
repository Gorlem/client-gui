package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.ActionEvent;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.events.KeyboardListener;
import com.ddoerr.clientgui.models.Modifier;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ShortcutAttachment implements KeyboardListener {
    private final Map<Shortcut, ActionListener> shortcuts = new HashMap<>();

    public void addShortcut(Shortcut shortcut, ActionListener actionListener) {
        shortcuts.put(shortcut, actionListener);
    }
    @Override
    public ActionResult keyDown(KeyboardEvent keyboardEvent) {
        for (Map.Entry<Shortcut, ActionListener> entry : shortcuts.entrySet()) {
            if (keyboardEvent.getKey().equals(entry.getKey().getKey()) && keyboardEvent.getModifiers().equals(entry.getKey().getModifiers())) {
                entry.getValue().doAction(new ActionEvent(keyboardEvent.getSource()));
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.PASS;
    }

    public interface Shortcut {
        InputUtil.Key getKey();
        EnumSet<Modifier> getModifiers();
    }
}
