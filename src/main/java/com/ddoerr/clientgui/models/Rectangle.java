package com.ddoerr.clientgui.models;

public class Rectangle {
    private final Point position;
    private final Size size;

    public Rectangle(Point position, Size size) {
        this.position = position;
        this.size = size;
    }

    public Point getTopLeftPoint() {
        return position;
    }

    public Point getTopRightPoint() {
        return new Point(position.getX() + size.getWidth(), position.getY());
    }

    public Point getBottomRightPoint() {
        return new Point(position.getX() + size.getWidth(), position.getY() + size.getHeight());
    }

    public Point getBottomLeftPoint() {
        return new Point(position.getX(), position.getY() + size.getHeight());
    }

    public Point getCenterPoint() {
        return new Point(position.getX() + (size.getWidth() / 2), position.getY() + (size.getHeight() / 2));
    }
}
