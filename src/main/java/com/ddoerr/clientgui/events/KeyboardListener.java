package com.ddoerr.clientgui.events;

import net.minecraft.util.ActionResult;

public interface KeyboardListener {
    default ActionResult keyDown(KeyboardEvent keyboardEvent) {
        return ActionResult.PASS;
    }
    default ActionResult keyUp(KeyboardEvent keyboardEvent) {
        return ActionResult.PASS;
    }
    default ActionResult characterTyped(KeyboardEvent keyboardEvent) {
        return ActionResult.PASS;
    }
}
