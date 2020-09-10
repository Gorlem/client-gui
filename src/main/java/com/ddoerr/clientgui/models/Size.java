package com.ddoerr.clientgui.models;

public class Size {
    public static final Size EMPTY = new Size(0);

    private final int width;
    private final int height;

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Size(int size) {
        this(size, size);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
