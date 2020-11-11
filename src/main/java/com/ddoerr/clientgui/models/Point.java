package com.ddoerr.clientgui.models;

public class Point {
    public static final Point ORIGIN = Point.of(0, 0);
    public static final Point OFF_SCREEN = Point.of(-1, -1);

    private final int x;
    private final int y;

    private Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(int x, int y) {
        return new Point(x, y);
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

    public Point addInnerInsets(Insets insets) {
        return add(insets.getLeft(), insets.getTop());
    }

    public Point addPoint(Point point) {
        return add(point.getX(), point.getY());
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
