package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.models.Axis;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class DynamicStackWidget extends Widget<DynamicStackWidget> {
    protected final ObjectProperty<Axis> axis = new SimpleObjectProperty<>(this, "axis", Axis.Vertical);
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());

    public DynamicStackWidget() {
        containerAttachment.childrenProperty().set(FXCollections.observableArrayList((child -> new Observable[] { child.outerSizeProperty() })));
        size.bind(Bindings.createObjectBinding(() -> {
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

        attach(containerAttachment);

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

                        return Point.of(x, y);
                    },
                    positionProperty(), marginProperty(), paddingProperty(), childrenProperty()));
        }, removedWidget -> {
            removedWidget.positionProperty().unbind();
        });
    }

    public ObjectProperty<Axis> axisProperty() {
        return axis;
    }
    public Axis getAxis() {
        return axis.get();
    }
    public DynamicStackWidget setAxis(Axis axis) {
        this.axis.set(axis);
        return this;
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return containerAttachment.childrenProperty();
    }
    public DynamicStackWidget addChild(Widget<?> child) {
        containerAttachment.addChild(child);
        return this;
    }
}
