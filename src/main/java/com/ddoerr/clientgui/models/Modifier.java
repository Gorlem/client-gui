package com.ddoerr.clientgui.models;

import org.lwjgl.glfw.GLFW;

public enum Modifier {
    SHIFT(GLFW.GLFW_MOD_SHIFT),
    CONTROL(GLFW.GLFW_MOD_CONTROL),
    ALT(GLFW.GLFW_MOD_ALT),
    SUPER(GLFW.GLFW_MOD_SUPER);

    private final int modifier;

    Modifier(int modifier) {
        this.modifier = modifier;
    }

    public int getModifier() {
        return modifier;
    }
}
