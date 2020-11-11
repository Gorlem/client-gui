package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.widgets.Widget;

public class CursorAttachment {
    public CursorAttachment(Widget<?> widget, CursorManager.Cursor cursor) {
        widget.highlightedProperty().addListener((bean, old, now) -> {
            if (now && widget.isEnabled()) {
                CursorManager.setCursor(cursor);
            } else {
                CursorManager.resetCursor();
            }
        });
        widget.enabledProperty().addListener((bean, old, now) -> {
            if (!now) {
                CursorManager.resetCursor();
            }
        });
    }
}
