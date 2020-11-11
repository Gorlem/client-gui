package com.ddoerr.clientgui.models;

public class Insets {
    public static final Insets EMPTY = Insets.of(0);

    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    private Insets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public static Insets of(int top, int right, int bottom, int left) {
        return new Insets(top, right, bottom, left);
    }

    public static Insets of(int topBottom, int rightLeft) {
        return new Insets(topBottom, rightLeft, topBottom, rightLeft);
    }

    public static Insets of(int allSides) {
        return new Insets(allSides, allSides, allSides, allSides);
    }

    public int getTop() {
        return top;
    }

    public int getRight() {
        return right;
    }

    public int getBottom() {
        return bottom;
    }

    public int getLeft() {
        return left;
    }

    public int getWidth() {
        return left + right;
    }

    public int getHeight() {
        return top + bottom;
    }

    @Override
    public String toString() {
        return "Insets{" +
                "top=" + top +
                ", right=" + right +
                ", bottom=" + bottom +
                ", left=" + left +
                '}';
    }
}
