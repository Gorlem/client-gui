package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.bindings.BindingUtil;
import com.ddoerr.clientgui.models.Axis;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.DynamicStackWidget;
import com.ddoerr.clientgui.widgets.layout.FixedStackWidget;
import com.sun.javafx.binding.IntegerConstant;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class ListWidget extends Widget<ListWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());

    protected final ObjectProperty<Axis> axis = new SimpleObjectProperty<>(this, "axis", Axis.Vertical);
    protected final ListProperty<Widget<?>> children = new SimpleListProperty<>(this, "children", FXCollections.observableArrayList());

    public ListWidget() {
        attach(containerAttachment);

        containerAttachment.addChild(build());
    }

    protected Widget<?> build() {
        ScrollbarWidget scrollbarWidget = new ScrollbarWidget()
            .Do(w -> w.axisProperty().bind(axisProperty()))
            .Do(w -> w.sizeProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                .then(sizeProperty().setWidth(10))
                .otherwise(sizeProperty().setHeight(10))
        ));

        FixedStackWidget fixedStackWidget = new FixedStackWidget()
                .Do(w -> w.axisProperty().bind(axisProperty()))
                .Do(w -> w.sizeProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                        .then(sizeProperty().subtractWidth(IntegerConstant.valueOf(10)))
                        .otherwise(sizeProperty().subtractHeight(IntegerConstant.valueOf(10)))
                ))
                .Do(w -> w.scrollFactorProperty().bind(scrollbarWidget.scrollFactoryProperty()))
                .Do(w -> w.childrenProperty().bind(childrenProperty()));

        scrollbarWidget.Do(w -> w.gripSizeFactorProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                    .then(BindingUtil.divide(
                            Bindings.selectInteger(sizeProperty(), "height"),
                            Bindings.selectInteger(fixedStackWidget.innerSizeProperty(), "height")))
                    .otherwise(BindingUtil.divide(
                            Bindings.selectInteger(sizeProperty(), "width"),
                            Bindings.selectInteger(fixedStackWidget.innerSizeProperty(), "width")))
        ));

        return new DynamicStackWidget()
            .Do(w -> w.axisProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical)).then(Axis.Horizontal).otherwise(Axis.Vertical)))
            .addChild(fixedStackWidget)
            .addChild(scrollbarWidget);
    }

    public ObjectProperty<Axis> axisProperty() {
        return axis;
    }

    public ListWidget setAxis(Axis axis) {
        this.axis.set(axis);
        return this;
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return children;
    }

    public ListWidget addChild(Widget<?> child) {
        childrenProperty().add(child);
        return this;
    }
}
