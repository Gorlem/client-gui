package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.ShortcutAttachment;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.util.ShortcutBuilder;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleDoubleProperty;

public class ListWidget extends Widget<ListWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();

    protected final DoubleProperty scrollPosition = new SimpleDoubleProperty(this, "scrollPosition");
    protected final ReadOnlyObjectWrapper<Size> innerSize = new ReadOnlyObjectWrapper<>(this, "innerSize");
    protected final ReadOnlyIntegerWrapper offset = new ReadOnlyIntegerWrapper(this, "offset");

    public ListWidget() {
        attach(containerAttachment);
        attach(shortcutAttachment);

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.up"), e -> scrollPosition.set(scrollPosition.get() - 0.1));
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.down"), e -> scrollPosition.set(scrollPosition.get() + 0.1));

        innerSize.bind(Bindings.createObjectBinding(() -> {
            int width = 0;
            int height = 0;

            for (Widget<?> child : containerAttachment.getChildren()) {
//                switch (stackDirection.get()) {
//                    case Vertical:
                        height += child.getOuterSize().getHeight();
                        width = Math.max(width, child.getOuterSize().getWidth());
//                        break;
//                    case Horizontal:
//                        width += child.getOuterSize().getWidth();
//                        height = Math.max(height, child.getOuterSize().getHeight());
//                        break;
//                }
            }

            return Size.of(width, height);
        }, containerAttachment.childrenProperty()));

        offset.bind(Bindings.selectInteger(sizeProperty(), "height").subtract(Bindings.selectInteger(innerSize, "height")).multiply(scrollPosition));

        containerAttachment.setWidgetConsumer(addedWidget -> {
            addedWidget.positionProperty().bind(Bindings.createObjectBinding(() -> {
                int x = position.get().getX();
                int y = position.get().getY();

                for (Widget<?> child : containerAttachment.getChildren()) {
                    if (child.equals(addedWidget)) {
                        break;
                    }

//                    switch (stackDirection.get()) {
//                        case Vertical:
                    y += child.getOuterSize().getHeight();
//                            break;
//                        case Horizontal:
//                            x += child.getOuterSize().getWidth();
//                            break;
//                    }
                }

                y += offset.get();

                return Point.of(x, y);
            }, containerAttachment.childrenProperty(), positionProperty(), offset));
        }, removedWidget -> {
            removedWidget.positionProperty().unbind();
        });
    }

    public ListWidget addChild(Widget<?> widget) {
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
