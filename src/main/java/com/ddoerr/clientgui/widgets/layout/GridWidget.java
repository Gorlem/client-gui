package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class GridWidget extends Widget<GridWidget> {
    protected final ObjectProperty<GridDefinition> gridDefinition = new SimpleObjectProperty<>(this, "gridDefinition", GridDefinition.DEFAULT);
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(this, focusListeners.fire());

    public GridWidget() {
        containerAttachment.setSizeCalculation(this::calculateChildSize);
        containerAttachment.setPositionCalculation(this::calculateChildPosition);
        attach(containerAttachment);

        containerAttachment.setWidgetConsumer(addedWidget -> {
            addedWidget.positionProperty().bind(Bindings.createObjectBinding(() -> {
                        CellPosition cellPosition = addedWidget.findFirstAttachment(CellPosition.class).orElse(CellPosition.DEFAULT);

                        int cellWidth = getSize().getWidth() / getGridDefinition().columns;
                        int cellHeight = getSize().getHeight() / getGridDefinition().rows;

                        int x = cellWidth * cellPosition.column;
                        int y = cellHeight * cellPosition.row;

                        return getPosition().add(x, y).addInnerInsets(getMargin());
                    },
                positionProperty(), marginProperty(), paddingProperty()));


            addedWidget.sizeProperty().bind(sizeProperty()
                    .divide(Bindings.selectInteger(gridDefinitionProperty(), "columns"), Bindings.selectInteger(gridDefinitionProperty(), "rows"))
                    .calculate(size -> {
                        CellPosition cellPosition = addedWidget.findFirstAttachment(CellPosition.class).orElse(CellPosition.DEFAULT);
                        return Size.of(size.getWidth() * cellPosition.columnSpan, size.getHeight() * cellPosition.rowSpan);
                    })
                    .addInnerInsets(addedWidget.marginProperty()));
        }, removedWidget -> {
            removedWidget.positionProperty().unbind();
            removedWidget.sizeProperty().unbind();
        });
    }

    public ObjectProperty<GridDefinition> gridDefinitionProperty() {
        return gridDefinition;
    }
    public GridDefinition getGridDefinition() {
        return gridDefinition.get();
    }
    public void setGridDefinition(GridDefinition gridDefinition) {
        this.gridDefinition.set(gridDefinition);
    }

    Point calculateChildPosition(Widget<?> child) {
        CellPosition cellPosition = child.findFirstAttachment(CellPosition.class).orElse(CellPosition.DEFAULT);

        int cellWidth = getSize().getWidth() / getGridDefinition().columns;
        int cellHeight = getSize().getHeight() / getGridDefinition().rows;

        int x = cellWidth * cellPosition.column;
        int y = cellHeight * cellPosition.row;

        return getPosition().add(x, y).addInnerInsets(getMargin());
    }

    Size calculateChildSize(Widget<?> child) {
        CellPosition cellPosition = child.findFirstAttachment(CellPosition.class).orElse(CellPosition.DEFAULT);

        int cellWidth = getSize().getWidth() / getGridDefinition().columns * cellPosition.columnSpan;
        int cellHeight = getSize().getHeight() / getGridDefinition().rows * cellPosition.rowSpan;

        return Size.of(cellWidth, cellHeight).addInnerInsets(child.getMargin());
    }

    public void addChild(Widget<?> child, CellPosition cellPosition, boolean fitToCell) {
        containerAttachment.addChild(child, cellPosition, fitToCell);
    }

    public static class GridDefinition {
        public static final GridDefinition DEFAULT = new GridDefinition(1, 1);

        private final int columns;
        private final int rows;

        public GridDefinition(int columns, int rows) {
            this.columns = columns;
            this.rows = rows;
        }

        public int getColumns() {
            return columns;
        }

        public int getRows() {
            return rows;
        }
    }

    public static class CellPosition {
        public static final CellPosition DEFAULT = new CellPosition(1, 1);

        private final int column;
        private final int columnSpan;
        private final int row;
        private final int rowSpan;

        public CellPosition(int column, int columnSpan, int row, int rowSpan) {
            this.column = column;
            this.columnSpan = columnSpan;
            this.row = row;
            this.rowSpan = rowSpan;
        }

        public CellPosition(int column, int row) {
            this(column, 1, row, 1);
        }
    }
}
