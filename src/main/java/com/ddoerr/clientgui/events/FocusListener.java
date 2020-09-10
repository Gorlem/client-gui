package com.ddoerr.clientgui.events;

public interface FocusListener {
    default void focusChanged(FocusEvent focusEvent) {}
}
