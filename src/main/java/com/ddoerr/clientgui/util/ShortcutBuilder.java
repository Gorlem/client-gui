package com.ddoerr.clientgui.util;

import com.ddoerr.clientgui.attachments.ShortcutAttachment;
import com.ddoerr.clientgui.models.Modifier;
import net.minecraft.client.util.InputUtil;

import java.util.EnumSet;

public class ShortcutBuilder implements ShortcutAttachment.Shortcut {
    private final InputUtil.Key key;
    private final EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);

    public static ShortcutBuilder of(String key) {
        return new ShortcutBuilder(InputUtil.fromTranslationKey(key));
    }

    private ShortcutBuilder(InputUtil.Key key) {
        this.key = key;
    }

    public ShortcutBuilder andControl() {
        modifiers.add(Modifier.CONTROL);
        return this;
    }

    public ShortcutBuilder andShift() {
        modifiers.add(Modifier.SHIFT);
        return this;
    }

    public ShortcutBuilder andAlt() {
        modifiers.add(Modifier.ALT);
        return this;
    }

    public ShortcutBuilder andSuper() {
        modifiers.add(Modifier.SUPER);
        return this;
    }

    @Override
    public InputUtil.Key getKey() {
        return key;
    }

    @Override
    public EnumSet<Modifier> getModifiers() {
        return modifiers;
    }
}
