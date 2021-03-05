package com.ddoerr.clientgui.models;

import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Shortcut {
    private final InputUtil.Key key;
    private final EnumSet<Modifier> modifiers;
    private final List<Predicate<Widget<?>>> rules;

    public Shortcut(InputUtil.Key key, EnumSet<Modifier> modifiers, List<Predicate<Widget<?>>> rules) {
        this.key = key;
        this.modifiers = modifiers;
        this.rules = rules;
    }

    public InputUtil.Key getKey() {
        return key;
    }

    public EnumSet<Modifier> getModifiers() {
        return EnumSet.copyOf(modifiers);
    }

    public List<Predicate<Widget<?>>> getRules() {
        return Collections.unmodifiableList(rules);
    }
}
