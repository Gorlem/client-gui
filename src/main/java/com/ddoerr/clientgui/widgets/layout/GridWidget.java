package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class GridWidget extends ContainerWidget<GridWidget.CellPosition> {
    protected final ObjectProperty<GridDefinition> gridDefinition = new SimpleObjectProperty<>(this, "gridDefinition", GridDefinition.EMPTY);

    public ObjectProperty<GridDefinition> gridDefinitionProperty() {
        return gridDefinition;
    }
    public GridDefinition getGridDefinition() {
        return gridDefinition.get();
    }
    public void setGridDefinition(GridDefinition gridDefinition) {
        this.gridDefinition.set(gridDefinition);
    }

    @Override
    Point calculateChildPosition(WidgetWithData<CellPosition> child) {
        int cellWidth = getSize().getWidth() / getGridDefinition().columns;
        int cellHeight = getSize().getHeight() / getGridDefinition().rows;

        int x = cellWidth * child.getData().column;
        int y = cellHeight * child.getData().row;

        return new Point(x, y).addInsets(getMargin()).addPoint(position.get());
    }

    @Override
    Size calculateChildSize(WidgetWithData<CellPosition> child) {
        int cellWidth = getSize().getWidth() / getGridDefinition().columns * child.getData().columnSpan;
        int cellHeight = getSize().getHeight() / getGridDefinition().rows * child.getData().rowSpan;

        return new Size(cellWidth - child.getWidget().getMargin().getWidth(), cellHeight - child.getWidget().getMargin().getHeight());
    }

    public void addChild(Widget child, CellPosition data, boolean fitToCell) {
        childrenProperty().add(new WidgetWithData<>(child, data, fitToCell));
    }

    public static class GridDefinition {
        public static final GridDefinition EMPTY = new GridDefinition(1, 1);

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

        public int getColumn() {
            return column;
        }

        public int getColumnSpan() {
            return columnSpan;
        }

        public int getRow() {
            return row;
        }

        public int getRowSpan() {
            return rowSpan;
        }
    }
}
