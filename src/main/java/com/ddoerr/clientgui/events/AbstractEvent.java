package com.ddoerr.clientgui.events;

import com.ddoerr.clientgui.widgets.Widget;

public abstract class AbstractEvent {
    private final Widget source;

    public AbstractEvent(Widget source) {
        this.source = source;
    }

    public Widget getSource() {
        return source;
    }
}
