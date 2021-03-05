package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.bindings.ClampedDoubleProperty;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.MouseListener;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;

public class DraggableAttachment implements Attachment, MouseListener {
    protected final DoubleProperty widthFactor = new ClampedDoubleProperty(this, "widthFactor", 0, 0, 1);
    protected final DoubleProperty heightFactor = new ClampedDoubleProperty(this, "heightFactor", 0, 0, 1);

    private Widget<?> widget;
    private Widget<?> parent;

    private Point offsetPosition = null;

    public DoubleProperty widthFactorProperty() {
        return widthFactor;
    }

    public DoubleProperty heightFactorProperty() {
        return heightFactor;
    }

    public void setParent(Widget<?> parent) {
        this.parent = parent;

        if (widget != null && parent != null) {
            bindPosition();
        }
    }

    @Override
    public void initialize(Widget<?> widget) {
        this.widget = widget;

        if (widget != null && parent != null) {
            bindPosition();
        }
    }

    public void incrementWidthFactor() {
        widthFactorProperty().set(widthFactorProperty().get() + 0.1);
    }

    public void decrementWidthFactor() {
        widthFactorProperty().set(widthFactorProperty().get() - 0.1);
    }

    public void incrementHeightFactor() {
        heightFactorProperty().set(heightFactorProperty().get() + 0.1);
    }

    public void decrementHeightFactor() {
        heightFactorProperty().set(heightFactorProperty().get() - 0.1);
    }

    protected void bindPosition() {
        widget.positionProperty().bind(Bindings.createObjectBinding(() -> {
            double widthOffset = (parent.getSize().getWidth() - widget.getSize().getWidth()) * widthFactor.get();
            double heightOffset = (parent.getSize().getHeight() - widget.getSize().getHeight()) * heightFactor.get();
            return parent.getPosition().add(widthOffset, heightOffset);
        }, widthFactor, heightFactor, parent.sizeProperty(), widget.sizeProperty(), parent.positionProperty()));
    }

    @Override
    public ActionResult mouseDown(MouseEvent mouseEvent) {
        if (widget.isWithinWidget(mouseEvent.getPosition())) {
            offsetPosition = mouseEvent.getPosition().subtractPoint(widget.getPosition());
        }

        return ActionResult.PASS;
    }

    @Override
    public ActionResult mouseUp(MouseEvent mouseEvent) {
        if (offsetPosition != null) {
            offsetPosition = null;
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getDelta().isPresent() && offsetPosition != null) {
            Point position = mouseEvent.getPosition().subtractPoint(offsetPosition).subtractPoint(parent.getPosition());
            int width = parent.getSize().getWidth() - widget.getSize().getWidth();
            int height = parent.getSize().getHeight() - widget.getSize().getHeight();

            double x = MathHelper.clamp(position.getX(), 0, width);
            double y = MathHelper.clamp(position.getY(), 0, height);

            widthFactor.set(width == 0 ? 0 : x / width);
            heightFactor.set(height == 0 ? 0 : y / height);

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }
}
