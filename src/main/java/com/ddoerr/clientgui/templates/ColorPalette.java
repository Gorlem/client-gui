package com.ddoerr.clientgui.templates;

import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.widgets.Widget;
import org.apache.logging.log4j.LogManager;

public class ColorPalette {
    private Color defaultColor;
    private Color disabledColor;
    private Color focusedColor;
    private Color activeColor;
    private Color highlightColor;
    private Color activeHighlightColor;

    public ColorPalette(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public ColorPalette disabled(Color color) {
        disabledColor = color;
        return this;
    }

    public ColorPalette focused(Color color) {
        focusedColor = color;
        return this;
    }

    public ColorPalette active(Color color) {
        activeColor = color;
        return this;
    }

    public ColorPalette highlight(Color color) {
        highlightColor = color;
        return this;
    }

    public ColorPalette activeHighlight(Color color) {
        activeHighlightColor = color;
        return this;
    }

    public Color getDefaultColor() {
        return defaultColor;
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

    public Color getActiveHighlightColor() {
        return activeHighlightColor;
    }

    public Color getColor(Widget<?> widget) {
        if (!widget.isEnabled() && disabledColor != null) {
            return disabledColor;
        }

        if (widget.isFocused() && focusedColor != null) {
            return focusedColor;
        }

        if (widget.isActive() && widget.isHighlighted() && activeHighlightColor != null) {
            return activeHighlightColor;
        }

        if (widget.isActive() && activeColor != null) {
            return activeColor;
        }

        if (widget.isHighlighted() && highlightColor != null) {
            return highlightColor;
        }

        return defaultColor;
    }
}
