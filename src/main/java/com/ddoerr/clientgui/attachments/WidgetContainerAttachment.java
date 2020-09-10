package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.*;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.ContainerWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;

import java.util.List;

public class WidgetContainerAttachment<T> implements Attachment, MouseListener, RenderListener, FocusListener {
    protected final ListProperty<ContainerWidget.WidgetWithData<T>> children = new SimpleListProperty<>(this, "children", FXCollections.observableArrayList());

    private final Widget widget;

    public WidgetContainerAttachment(Widget widget) {
        this.widget = widget;
        children.addListener((ListChangeListener<? super ContainerWidget.WidgetWithData<T>>) this::handleChange);
    }

    protected void handleChange(ListChangeListener.Change<? extends ContainerWidget.WidgetWithData<T>> changes) {
        while (changes.next()) {
            if (!changes.wasPermutated() && !changes.wasUpdated()) {
                for (ContainerWidget.WidgetWithData<T> oldWidget : changes.getRemoved()) {
                    oldWidget.getWidget().positionProperty().unbind();

                    if (oldWidget.isSizeBound()) {
                        oldWidget.getWidget().sizeProperty().unbind();
                    }

//                    oldWidget.getWidget().removeFocusListener(widget.focusListeners.fire());
                }

                for (ContainerWidget.WidgetWithData<T> newWidget : changes.getAddedSubList()) {
                    newWidget.getWidget().positionProperty().bind(Bindings.createObjectBinding(() ->
                            calculateChildPosition(newWidget), widget.positionProperty(), widget.outerSizeProperty(), newWidget.getWidget().outerSizeProperty()));

                    if (newWidget.isSizeBound()) {
                        newWidget.getWidget().sizeProperty().bind(Bindings.createObjectBinding(() -> calculateChildSize(newWidget), widget.outerSizeProperty()));
                    }

//                    newWidget.getWidget().addFocusListener(focusListeners.fire());
                }
            }
        }
    }

    abstract Point calculateChildPosition(ContainerWidget.WidgetWithData<T> child);
    abstract Size calculateChildSize(ContainerWidget.WidgetWithData<T> child);

    public ListProperty<ContainerWidget.WidgetWithData<T>> childrenProperty() {
        return children;
    }
    public void addChild(Widget child, T data) {
        children.add(new ContainerWidget.WidgetWithData<>(child, data));
    }
    public List<ContainerWidget.WidgetWithData<T>> getChildren() {
        return children.get();
    }

    @Override
    public void mouseDown(MouseEvent mouseEvent) {
        for (ContainerWidget.WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseDown(mouseEvent);
        }
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        for (ContainerWidget.WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseUp(mouseEvent);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        for (ContainerWidget.WidgetWithData<T> child : children.get()) {
            child.getWidget().mouseMoved(mouseEvent);
        }
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        for (ContainerWidget.WidgetWithData<T> child : children.get()) {
            child.getWidget().focusChanged(focusEvent);
        }
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        for (ContainerWidget.WidgetWithData<T> child : children.get()) {
            child.getWidget().renderLayer(renderEvent);
        }
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
