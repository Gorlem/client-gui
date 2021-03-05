package com.ddoerr.clientgui.models;

import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;

public class ColorPalette {
    private final Color baseColor;
    private final Color disabledColor;
    private final Color focusedColor;
    private final Color activeColor;
    private final Color highlightColor;

    public ColorPalette(Color baseColor, Color disabledColor, Color focusedColor, Color activeColor, Color highlightColor) {
        this.baseColor = baseColor;
        this.disabledColor = disabledColor;
        this.focusedColor = focusedColor;
        this.activeColor = activeColor;
        this.highlightColor = highlightColor;
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public Color getDisabledColor() {
        return disabledColor;
    }

    public Color getFocusedColor() {
        return focusedColor;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public Color getHighlightColor() {
        return highlightColor;
    }

    public Color getColor(Widget<?> widget) {
        if (!widget.isEnabled() && disabledColor != null) {
            return disabledColor;
        }

        if (widget.isFocused() && focusedColor != null) {
            return focusedColor;
        }

        if (widget.isActive() && activeColor != null) {
            return activeColor;
        }

        if (widget.isHighlighted() && highlightColor != null) {
            return highlightColor;
        }

        return baseColor;
    }

    public ObjectBinding<Color> createBinding(Widget<?> widget) {
        return Bindings.createObjectBinding(() -> getColor(widget),
                widget.focusedProperty(), widget.activeProperty(), widget.highlightedProperty(), widget.enabledProperty());
    }
}
