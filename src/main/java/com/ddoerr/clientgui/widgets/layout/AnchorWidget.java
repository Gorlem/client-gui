package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.models.*;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;

public class AnchorWidget extends Widget<AnchorWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(this, focusListeners.fire());

    public AnchorWidget() {
        containerAttachment.setPositionCalculation(this::calculateChildPosition);
        attach(containerAttachment);

        containerAttachment.setWidgetConsumer(addedWidget -> {
            addedWidget.positionProperty().bind(Bindings.createObjectBinding(() ->
                            addedWidget.findFirstAttachment(Anchor.class)
                                    .map(a -> a.calculatePosition(getSize(), getMargin(), addedWidget.getOuterSize()).addPoint(getPosition()))
                                    .orElse(getPosition()),
                    positionProperty(), sizeProperty(), marginProperty(), addedWidget.outerSizeProperty()));
        }, removedWidget -> {
            removedWidget.positionProperty().unbind();
        });
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return containerAttachment.childrenProperty();
    }

    public AnchorWidget addChild(Widget<?> widget, Anchor anchor) {
        containerAttachment.addChild(widget, anchor);
        return this;
    }

    Point calculateChildPosition(Widget<?> child) {
        return child.findFirstAttachment(Anchor.class)
                .map(a -> a.calculatePosition(getSize(), getMargin(), child.getOuterSize()).addPoint(position.get()))
                .orElse(Point.ORIGIN);
    }

    public enum Anchor {
        TopLeft((parent, margin, child) -> Point.ORIGIN.addInnerInsets(margin)),
        TopCenter((parent, margin, child) ->
                Point.of(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        margin.getTop())),
        TopRight((parent, margin, child) ->
                Point.of(
                        parent.getWidth() - child.getWidth() - margin.getRight(),
                        margin.getTop())),
        BottomLeft((parent, margin, child) ->
                Point.of(
                        margin.getLeft(),
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        BottomCenter((parent, margin, child) ->
                Point.of(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        BottomRight((parent, margin, child) ->
                Point.of(
                        parent.getWidth() - child.getWidth() - margin.getRight(),
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        MiddleLeft((parent, margin, child) ->
                Point.of(
                        margin.getLeft(),
                        parent.getHeight() / 2 - child.getHeight() / 2)),
        MiddleCenter((parent, margin, child) ->
                Point.of(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        parent.getHeight() / 2 - child.getHeight() / 2)),
        MiddleRight((parent, margin, child) ->
                Point.of(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        margin.getRight()));

        private final PositionCalculation positionCalculation;

        Anchor(PositionCalculation positionCalculation) {
            this.positionCalculation = positionCalculation;
        }

        public Point calculatePosition(Size parent, Insets parentMargin, Size child) {
            return positionCalculation.calculatePosition(parent, parentMargin, child);
        }
    }

    protected interface PositionCalculation {
        Point calculatePosition(Size parent, Insets parentMargin, Size child);
    }
}
