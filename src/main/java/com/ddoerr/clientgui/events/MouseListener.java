package com.ddoerr.clientgui.events;

import net.minecraft.util.ActionResult;

import java.util.EventListener;

public interface MouseListener extends EventListener {
    default ActionResult mouseUp(MouseEvent mouseEvent) {
        return ActionResult.PASS;
    }
    default ActionResult mouseDown(MouseEvent mouseEvent) {
        return ActionResult.PASS;
    }
    default ActionResult mouseMoved(MouseEvent mouseEvent) {
        return ActionResult.PASS;
    }
    default ActionResult mouseDragged(MouseEvent mouseEvent) {
        return ActionResult.PASS;
    }
}
