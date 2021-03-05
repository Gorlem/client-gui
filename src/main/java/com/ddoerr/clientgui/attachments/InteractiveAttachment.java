package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.*;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.event.EventListenerSupport;
import org.apache.logging.log4j.LogManager;

public class InteractiveAttachment implements MouseListener, KeyboardListener, Attachment {
    protected final EventListenerSupport<ActionListener> actionListeners = EventListenerSupport.create(ActionListener.class);

    private Widget<?> widget;
    private final FocusListener focusListener;

    public InteractiveAttachment(FocusListener focusListener) {
        this.focusListener = focusListener;
    }

    @Override
    public void initialize(Widget<?> widget) {
        this.widget = widget;
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.addListener(actionListener);
    }

    @Override
    public ActionResult mouseDown(MouseEvent mouseEvent) {
        if (widget.isEnabled() && widget.isWithinWidget(mouseEvent.getPosition())) {
            actionListeners.fire().doAction(new ActionEvent(widget));
            focusListener.focusChanged(new FocusEvent(widget));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public ActionResult keyDown(KeyboardEvent keyboardEvent) {
        if (widget.isEnabled() && widget.isFocused() && keyboardEvent.getKey() == InputUtil.fromTranslationKey("key.keyboard.space")) {
            actionListeners.fire().doAction(new ActionEvent(widget));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
