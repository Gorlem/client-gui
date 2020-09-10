package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;

public class ListWidget extends ContainerWidget<Void> {
    protected final ObjectProperty<StackDirection> stackDirection = new SimpleObjectProperty<>(this, "stackDirection", StackDirection.Vertical);

    public ListWidget() {
        super();
        children.set(FXCollections.observableArrayList((child -> new Observable[] { child.getWidget().outerSizeProperty() })));
        size.bind(Bindings.createObjectBinding(() -> {
            int width = 0;
            int height = 0;

            for (WidgetWithData<Void> child : getChildren()) {
                switch (stackDirection.get()) {
                    case Vertical:
                        height += child.getWidget().getOuterSize().getHeight();
                        width = Math.max(width, child.getWidget().getOuterSize().getWidth());
                        break;
                    case Horizontal:
                        width += child.getWidget().getOuterSize().getWidth();
                        height = Math.max(height, child.getWidget().getOuterSize().getHeight());
                        break;
                }
            }

            return new Size(width, height);
        }, children));
    }

    public ObjectProperty<StackDirection> stackDirectionProperty() {
        return stackDirection;
    }
    public StackDirection getStackDirection() {
        return stackDirection.get();
    }
    public void setStackDirection(StackDirection stackDirection) {
        this.stackDirection.set(stackDirection);
    }

    public void addChild(Widget child) {
        addChild(child, null);
    }

    @Override
    Point calculateChildPosition(WidgetWithData<Void> newChild) {
        int x = position.get().getX();
        int y = position.get().getY();

        for (WidgetWithData<Void> child : getChildren()) {
            if (child.equals(newChild)) {
                break;
            }

            switch (stackDirection.get()) {
                case Vertical:
                    y += child.getWidget().getOuterSize().getHeight();
                    break;
                case Horizontal:
                    x += child.getWidget().getOuterSize().getWidth();
                    break;
            }
        }

        return new Point(x, y);
    }

    @Override
    Size calculateChildSize(WidgetWithData<Void> child) {
        return null;
    }

    public enum StackDirection {
        Vertical,
        Horizontal
    }
}
