package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.MouseListener;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.util.ActionResult;

public class DraggableAttachment implements Attachment, MouseListener {
    private Widget<?> widget;
    private Widget<?> parent;

    public void setParent(Widget<?> parent) {
        this.parent = parent;
    }

    @Override
    public void initialize(Widget<?> widget) {
        this.widget = widget;
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        if (parent.isWithinWidget(mouseEvent.getPosition(), widget.getSize())) {
            widget.setPosition(mouseEvent.getPosition());
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
