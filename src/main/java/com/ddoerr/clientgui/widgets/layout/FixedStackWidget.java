package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.ShortcutAttachment;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Axis;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;

public class FixedStackWidget extends Widget<FixedStackWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();

    protected final DoubleProperty scrollFactor = new SimpleDoubleProperty(this, "scrollFactor");
    protected final ReadOnlyObjectWrapper<Size> innerSize = new ReadOnlyObjectWrapper<>(this, "innerSize");
    protected final ReadOnlyIntegerWrapper offset = new ReadOnlyIntegerWrapper(this, "offset");

    protected final ObjectProperty<Axis> axis = new SimpleObjectProperty<>(this, "axis", Axis.Vertical);

    public FixedStackWidget() {
        attach(containerAttachment);
        attach(shortcutAttachment);

        innerSize.bind(Bindings.createObjectBinding(() -> {
            int width = 0;
            int height = 0;

            for (Widget<?> child : containerAttachment.getChildren()) {
                switch (axis.get()) {
                    case Vertical:
                        height += child.getOuterSize().getHeight();
                        width = Math.max(width, child.getOuterSize().getWidth());
                        break;
                    case Horizontal:
                        width += child.getOuterSize().getWidth();
                        height = Math.max(height, child.getOuterSize().getHeight());
                        break;
                }
            }

            return Size.of(width, height);
        }, containerAttachment.childrenProperty()));

        offset.bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                .then(Bindings.selectInteger(sizeProperty(), "height").subtract(Bindings.selectInteger(innerSize, "height")))
                .otherwise(Bindings.selectInteger(sizeProperty(), "width").subtract(Bindings.selectInteger(innerSize, "width")))
                .multiply(scrollFactor));

        containerAttachment.setWidgetConsumer(addedWidget -> {
            addedWidget.positionProperty().bind(Bindings.createObjectBinding(() -> {
                double x = position.get().getX();
                double y = position.get().getY();

                for (Widget<?> child : containerAttachment.getChildren()) {
                    if (child.equals(addedWidget)) {
                        break;
                    }

                    switch (axis.get()) {
                        case Vertical:
                            y += child.getOuterSize().getHeight();
                            break;
                        case Horizontal:
                            x += child.getOuterSize().getWidth();
                            break;
                    }
                }

                switch (axis.get()) {
                    case Vertical:
                        y += offset.get();
                        break;
                    case Horizontal:
                        x += offset.get();
                        break;
                }

                return Point.of(x, y);
            }, containerAttachment.childrenProperty(), positionProperty(), offset));
        }, removedWidget -> removedWidget.positionProperty().unbind());
    }

    public DoubleProperty scrollFactorProperty() {
        return scrollFactor;
    }

    public ObjectProperty<Axis> axisProperty() {
        return axis;
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return containerAttachment.childrenProperty();
    }

    public ReadOnlyObjectWrapper<Size> innerSizeProperty() {
        return innerSize;
    }

    public FixedStackWidget addChild(Widget<?> widget) {
        containerAttachment.addChild(widget);
        return this;
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        Renderer.enableScissor(Rectangle.of(getPosition(), getSize()));
        super.renderLayer(renderEvent);
        Renderer.disableScissor();
    }
}
