package com.ddoerr.clientgui.models;

public class ColorPaletteBuilder {
    private Color baseColor;
    private Color disabledColor;
    private Color focusedColor;
    private Color activeColor;
    private Color highlightColor;

    public static ColorPaletteBuilder base(Color color) {
        return new ColorPaletteBuilder(color);
    }

    private ColorPaletteBuilder(Color baseColor) {
        this.baseColor = baseColor;
    }

    public ColorPaletteBuilder disabled(Color color) {
        disabledColor = color;
        return this;
    }

    public ColorPaletteBuilder focused(Color color) {
        focusedColor = color;
        return this;
    }

    public ColorPaletteBuilder active(Color color) {
        activeColor = color;
        return this;
    }

    public ColorPaletteBuilder highlight(Color color) {
        highlightColor = color;
        return this;
    }

    public ColorPalette build() {
        return new ColorPalette(baseColor, disabledColor, focusedColor, activeColor, highlightColor);
    }
}
