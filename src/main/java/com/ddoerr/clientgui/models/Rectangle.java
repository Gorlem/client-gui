package com.ddoerr.clientgui.models;

public class Rectangle {
    private final Point position;
    private final Size size;

    private Rectangle(Point position, Size size) {
        this.position = position;
        this.size = size;
    }

    public static Rectangle of(Point position, Size size) {
        return new Rectangle(position, size);
    }

    public Point getTopLeftPoint() {
        return position;
    }

    public Point getTopRightPoint() {
        return position.add(size.getWidth(), 0);
    }

    public Point getBottomRightPoint() {
        return position.add(size.getWidth(), size.getHeight());
    }

    public Point getBottomLeftPoint() {
        return position.add(0, size.getHeight());
    }

    public Point getCenterPoint() {
        return position.addPoint(size.getCenter());
    }

    public int getWidth() {
        return size.getWidth();
    }

    public int getHeight() {
        return size.getHeight();
    }

    public Size getSize() {
        return size;
    }
}
