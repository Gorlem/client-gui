package com.ddoerr.clientgui.models;

public class Point {
    public static final Point ORIGIN = new Point(0, 0);
    public static final Point OFF_SCREEN = new Point(-1, -1);

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point add(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point addInsets(Insets insets) {
        return add(insets.getLeft(), insets.getTop());
    }

    public Point addPoint(Point point) {
        return add(point.getX(), point.getY());
    }
}
