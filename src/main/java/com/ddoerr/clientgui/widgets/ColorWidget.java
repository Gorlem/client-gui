package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.util.math.MatrixStack;

public class ColorWidget extends Widget {
    protected final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.BLACK);

    public ObjectProperty<Color> colorProperty() {
        return color;
    }
    public Color getColor() {
        return color.get();
    }
    public void setColor(Color color) {
        this.color.set(color);
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.renderRectangle(matrixStack, new Rectangle(position.get().addInsets(margin.get()), size.get()), color.get());
    }
}
