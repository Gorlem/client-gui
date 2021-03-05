package com.ddoerr.clientgui.models;

import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ShortcutBuilder {
    private final InputUtil.Key key;
    private final EnumSet<Modifier> modifiers = EnumSet.noneOf(Modifier.class);
    private final List<Predicate<Widget<?>>> rules = new ArrayList<>();

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

    public ShortcutBuilder whenFocused() {
        rules.add(Widget::isFocused);
        return this;
    }

    public Shortcut build() {
        return new Shortcut(key, modifiers, rules);
    }
}
