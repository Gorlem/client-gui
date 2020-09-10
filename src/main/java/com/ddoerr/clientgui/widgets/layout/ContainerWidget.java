package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import net.minecraft.client.util.math.MatrixStack;

import java.util.List;

public abstract class ContainerWidget<T> extends Widget {
    protected final ListProperty<WidgetWithData<T>> children = new SimpleListProperty<>(this, "children", FXCollections.observableArrayList());

    public ContainerWidget() {
        children.addListener((ListChangeListener<? super WidgetWithData<T>>) this::handleChange);
    }

    protected void handleChange(ListChangeListener.Change<? extends WidgetWithData<T>> changes) {
        while (changes.next()) {
            if (!changes.wasPermutated() && !changes.wasUpdated()) {
                for (WidgetWithData<T> oldWidget : changes.getRemoved()) {
                    oldWidget.getWidget().positionProperty().unbind();

                    if (oldWidget.isSizeBound()) {
                        oldWidget.getWidget().sizeProperty().unbind();
                    }

                    oldWidget.getWidget().removeFocusListener(focusListeners.fire());
                }

                for (WidgetWithData<T> newWidget : changes.getAddedSubList()) {
                    newWidget.getWidget().positionProperty().bind(Bindings.createObjectBinding(() -> calculateChildPosition(newWidget), position, outerSize, newWidget.getWidget().outerSizeProperty()));

                    if (newWidget.isSizeBound()) {
                        newWidget.getWidget().sizeProperty().bind(Bindings.createObjectBinding(() -> calculateChildSize(newWidget), outerSize));
                    }

                    newWidget.getWidget().addFocusListener(focusListeners.fire());
                }
            }
        }
    }

    abstract Point calculateChildPosition(WidgetWithData<T> child);
    abstract Size calculateChildSize(WidgetWithData<T> child);

    public ListProperty<WidgetWithData<T>> childrenProperty() {
        return children;
    }
    public void addChild(Widget child, T data) {
        children.add(new WidgetWithData<>(child, data));
    }
    public List<WidgetWithData<T>> getChildren() {
        return children.get();
    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {
        for (WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseDown(mouseEvent);
        }
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        for (WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseUp(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        for (WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseMoved(mouseEvent);
        }
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        super.focusChanged(focusEvent);

        for (WidgetWithData<T> child : children.get()) {
            child.getWidget().focusChanged(focusEvent);
        }
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        for (WidgetWithData<T> child : children.get()) {
            child.getWidget().renderLayer(renderEvent);
        }

        super.renderLayer(renderEvent);
    }

    public static class WidgetWithData<T> {
        private final Widget widget;
        private final T data;
        private final boolean bindSize;

        public WidgetWithData(Widget widget, T data, boolean bindSize) {
            this.widget = widget;
            this.data = data;
            this.bindSize = bindSize;
        }

        public WidgetWithData(Widget widget, T data) {
            this(widget, data, false);
        }

        public Widget getWidget() {
            return widget;
        }

        public T getData() {
            return data;
        }

        public boolean isSizeBound() {
            return bindSize;
        }
    }
}
