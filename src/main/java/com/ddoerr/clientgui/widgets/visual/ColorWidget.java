package com.ddoerr.clientgui.widgets.visual;

import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.util.math.MatrixStack;

public class ColorWidget extends Widget<ColorWidget> {
    protected final ObjectProperty<Color> color = new SimpleObjectProperty<>(this, "color", Color.BLACK);

    public ObjectProperty<Color> colorProperty() {
        return color;
    }
    public Color getColor() {
        return color.get();
    }
    public ColorWidget setColor(Color color) {
        this.color.set(color);
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.renderRectangle(matrixStack, Rectangle.of(getPosition().addInnerInsets(getMargin()), getSize()), getColor());
    }
}
