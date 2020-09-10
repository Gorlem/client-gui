package com.ddoerr.clientgui.models;

public class Insets {
    public static final Insets EMPTY = new Insets(0);

    private final int top;
    private final int right;
    private final int bottom;
    private final int left;

    public Insets(int top, int right, int bottom, int left) {
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.left = left;
    }

    public Insets(int topBottom, int rightLeft) {
        this(topBottom, rightLeft, topBottom, rightLeft);
    }

    public Insets(int allSides) {
        this(allSides, allSides, allSides, allSides);
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
}
