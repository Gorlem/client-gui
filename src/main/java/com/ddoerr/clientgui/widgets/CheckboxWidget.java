package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.events.ActionEvent;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.Renderer;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.lang3.event.EventListenerSupport;

public class CheckboxWidget extends Widget {
    protected final EventListenerSupport<ActionListener> actionListeners = EventListenerSupport.create(ActionListener.class);

    boolean isActive = false;

    public CheckboxWidget() {
        setSize(new Size(10));
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.addListener(actionListener);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Rectangle rectangle = new Rectangle(position.get().addInsets(margin.get()), size.get());

        Renderer.renderRectangle(matrixStack, rectangle, isActive ? isWithinWidget(mouse) ? Color.GREEN: Color.GREEN.addAlpha(0.5)  : isWithinWidget(mouse) ? Color.BLACK : Color.BLACK.addAlpha(0.5));
        Renderer.renderBorder(matrixStack, rectangle, isFocused() ? Color.RED : Color.WHITE);
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        if (isWithinWidget(mouseEvent.getPosition())) {
            isActive = !isActive;
            actionListeners.fire().doAction(new ActionEvent(this));
            focusListeners.fire().focusChanged(new FocusEvent(this));
        }
    }
}
