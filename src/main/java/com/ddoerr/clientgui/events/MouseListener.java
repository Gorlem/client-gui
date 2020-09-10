package com.ddoerr.clientgui.events;

import java.util.EventListener;

public interface MouseListener extends EventListener {
    default void mouseUp(MouseEvent mouseEvent) {}
    default void mouseDown(MouseEvent mouseEvent) {}
    default void mouseMoved(MouseEvent mouseEvent) {}
}
