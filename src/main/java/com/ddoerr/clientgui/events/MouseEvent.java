package com.ddoerr.clientgui.events;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;

import java.util.Optional;

public class MouseEvent extends AbstractEvent {
    private final Point position;
    private final InputUtil.Key mouseButton;

    public MouseEvent(Widget source, Point position, InputUtil.Key mouseButton) {
        super(source);
        this.position = position;
        this.mouseButton = mouseButton;
    }

    public MouseEvent(Widget source, Point position) {
        super(source);
        this.position = position;
        this.mouseButton = null;
    }

    public Point getPosition() {
        return position;
    }

    public Optional<InputUtil.Key> getMouseButton() {
        return Optional.ofNullable(mouseButton);
    }
}
