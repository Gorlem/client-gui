package com.ddoerr.clientgui.events;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;

import java.util.Optional;

public class MouseEvent extends AbstractEvent {
    private final Point position;
    private final InputUtil.Key mouseButton;
    private final Point delta;

    public MouseEvent(Widget<?> source, Point position, InputUtil.Key mouseButton) {
        super(source);
        this.position = position;
        this.mouseButton = mouseButton;
        this.delta = null;
    }

    public MouseEvent(Widget<?> source, Point position, InputUtil.Key mouseButton, Point delta) {
        super(source);
        this.position = position;
        this.mouseButton = mouseButton;
        this.delta = delta;
    }

    public MouseEvent(Widget<?> source, Point position) {
        super(source);
        this.position = position;
        this.mouseButton = null;
        this.delta = null;
    }

    public Point getPosition() {
        return position;
    }

    public Optional<InputUtil.Key> getMouseButton() {
        return Optional.ofNullable(mouseButton);
    }

    public Optional<Point> getDelta() {
        return Optional.ofNullable(delta);
    }
}
