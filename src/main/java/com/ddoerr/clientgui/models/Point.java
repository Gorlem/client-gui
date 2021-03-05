package com.ddoerr.clientgui.models;

public class Point {
    public static final Point ORIGIN = Point.of(0, 0);
    public static final Point OFF_SCREEN = Point.of(-1, -1);

    private final double x;
    private final double y;

    private Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point of(double x, double y) {
        return new Point(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Point add(double x, double y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point addInnerInsets(Insets insets) {
        return add(insets.getLeft(), insets.getTop());
    }

    public Point addPoint(Point point) {
        return add(point.getX(), point.getY());
    }

    public Point subtractPoint(Point point) {
        return add(-1 * point.getX(), -1 * point.getY());
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
