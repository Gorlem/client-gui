package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;

public class AnchorWidget extends ContainerWidget<AnchorWidget.Anchor> {
    @Override
    Point calculateChildPosition(WidgetWithData<Anchor> child) {
        return child.getData().calculatePosition(getSize(), getMargin(), child.getWidget().getOuterSize())
                .addPoint(position.get());
    }

    @Override
    Size calculateChildSize(WidgetWithData<Anchor> child) {
        return null;
    }

    public enum Anchor implements PositionCalculation {
        TopLeft((parent, margin, child) -> Point.ORIGIN.addInsets(margin)),
        TopCenter((parent, margin, child) ->
                new Point(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        margin.getTop())),
        TopRight((parent, margin, child) ->
                new Point(
                        parent.getWidth() - child.getWidth() - margin.getRight(),
                        margin.getTop())),
        BottomLeft((parent, margin, child) ->
                new Point(
                        margin.getLeft(),
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        BottomCenter((parent, margin, child) ->
                new Point(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        BottomRight((parent, margin, child) ->
                new Point(
                        parent.getWidth() - child.getWidth() - margin.getRight(),
                        parent.getHeight() - child.getHeight() - margin.getBottom())),
        MiddleLeft((parent, margin, child) ->
                new Point(
                        margin.getLeft(),
                        parent.getHeight() / 2 - child.getHeight() / 2)),
        MiddleCenter((parent, margin, child) ->
                new Point(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        parent.getHeight() / 2 - child.getHeight() / 2)),
        MiddleRight((parent, margin, child) ->
                new Point(
                        parent.getWidth() / 2 - child.getWidth() / 2,
                        margin.getRight()));

        private final PositionCalculation positionCalculation;

        Anchor(PositionCalculation positionCalculation) {
            this.positionCalculation = positionCalculation;
        }

        @Override
        public Point calculatePosition(Size parent, Insets parentMargin, Size child) {
            return positionCalculation.calculatePosition(parent, parentMargin, child);
        }
    }

    protected interface PositionCalculation {
        Point calculatePosition(Size parent, Insets parentMargin, Size child);
    }
}
