package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.FocusListener;
import com.ddoerr.clientgui.widgets.Widget;

public class DragAreaAttachment extends ContainerAttachment {
    public DragAreaAttachment(FocusListener focusListener) {
        super(focusListener);
    }

    @Override
    public void initialize(Widget<?> widget) {
        super.initialize(widget);

        setWidgetConsumer(widgetAdded -> {
            widgetAdded.findFirstAttachment(DraggableAttachment.class)
                    .ifPresent(attachment -> attachment.setParent(widget));
        }, removedWidget -> {});
    }
}
