package com.ddoerr.clientgui.models;

public class Size {
    public static final Size EMPTY = Size.of(0);

    private final int width;
    private final int height;

    private Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public static Size of(int width, int height) {
        return new Size(width, height);
    }

    public static Size of(int size) {
        return new Size(size, size);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Point getCenter() {
        return Point.of(width / 2, height / 2);
    }

    public Size addInnerInsets(Insets insets) {
        return Size.of(width - insets.getWidth(), height - insets.getHeight());
    }

    public Size addOuterInsets(Insets insets) {
        return Size.of(width + insets.getWidth(), height + insets.getHeight());
    }

    @Override
    public String toString() {
        return "Size{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}
