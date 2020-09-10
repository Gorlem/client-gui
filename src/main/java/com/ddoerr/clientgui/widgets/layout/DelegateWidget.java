package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.util.math.MatrixStack;

public abstract class DelegateWidget extends Widget {
    protected final ObjectProperty<Widget> child = new SimpleObjectProperty<>(this, "child");

    public DelegateWidget() {
        child.addListener((object, old, now) -> newChild(old, now));
    }

    protected void newChild(Widget oldWidget, Widget newWidget) {
        if (oldWidget != null) {
            oldWidget.positionProperty().unbind();
            oldWidget.sizeProperty().unbind();

            oldWidget.removeFocusListener(focusListeners.fire());
        }

        if (newWidget != null) {
            newWidget.positionProperty().bind(Bindings.createObjectBinding(() ->
                    getPosition().addInsets(getMargin()).addInsets(getPadding()), position, outerSize, newWidget.outerSizeProperty()));
            newWidget.sizeProperty().bind(Bindings.createObjectBinding(() ->
                    new Size(
                            getSize().getWidth() - getPadding().getWidth() - newWidget.getMargin().getWidth(),
                            getSize().getHeight() - getPadding().getHeight() - newWidget.getMargin().getHeight()
                    ), outerSize));

            newWidget.addFocusListener(focusListeners.fire());
        }
    }

    public ObjectProperty<Widget> childProperty() {
        return child;
    }
    public Widget getChild() {
        return child.get();
    }
    public void setChild(Widget child) {
        this.child.set(child);
    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {
        child.get().mouseDown(mouseEvent);
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        child.get().mouseUp(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        child.get().mouseMoved(mouseEvent);
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        super.focusChanged(focusEvent);
        child.get().focusChanged(focusEvent);
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        child.get().renderLayer(renderEvent);
        super.renderLayer(renderEvent);
    }
}
