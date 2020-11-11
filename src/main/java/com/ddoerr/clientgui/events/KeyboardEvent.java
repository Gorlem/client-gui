package com.ddoerr.clientgui.events;

import com.ddoerr.clientgui.models.Modifier;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;

import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

public class KeyboardEvent extends AbstractEvent {
    private final InputUtil.Key key;
    private final EnumSet<Modifier> modifiers;
    private final char character;

    public KeyboardEvent(Widget<?> source, InputUtil.Key key, EnumSet<Modifier> modifiers) {
        super(source);
        this.key = key;
        this.modifiers = modifiers;

        this.character = Character.MIN_VALUE;
    }

    public KeyboardEvent(Widget<?> source, InputUtil.Key key, char character) {
        super(source);
        this.key = key;
        this.character = character;

        this.modifiers = EnumSet.noneOf(Modifier.class);
    }

    public InputUtil.Key getKey() {
        return key;
    }

    public EnumSet<Modifier> getModifiers() {
        return modifiers;
    }

    public Optional<Character> getCharacter() {
        return character == Character.MIN_VALUE ? Optional.empty() : Optional.of(character);
    }
}
