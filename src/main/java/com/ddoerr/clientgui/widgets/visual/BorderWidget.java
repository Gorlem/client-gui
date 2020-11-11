package com.ddoerr.clientgui.widgets.visual;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.util.math.MatrixStack;

public class BorderWidget extends Widget<BorderWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(this, focusListeners.fire());

    protected final ObjectProperty<Insets> borderThickness = new SimpleObjectProperty<>(this, "borderThickness", Insets.EMPTY);
    protected final ObjectProperty<Color> borderColor = new SimpleObjectProperty<>(this, "borderColor", Color.TRANSPARENT);
    protected final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(this, "backgroundColor", Color.TRANSPARENT);

    public BorderWidget() {
        attach(containerAttachment);
    }

    public ObjectProperty<Insets> borderThicknessProperty() {
        return borderThickness;
    }
    public BorderWidget setBorderThickness(Insets borderThickness) {
        this.borderThickness.set(borderThickness);
        return this;
    }
    public Insets getBorderThickness() {
        return borderThickness.get();
    }

    public ObjectProperty<Color> borderColorProperty() {
        return borderColor;
    }
    public void setBorderColor(Color borderColor) {
        this.borderColor.set(borderColor);
    }
    public Color getBorderColor() {
        return borderColor.get();
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }
    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return containerAttachment.childrenProperty();
    }

    public BorderWidget setChild(Widget<?> widget) {
        containerAttachment.removeChildren();
        containerAttachment.addChild(widget, null, true);
        return this;
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Insets border = getBorderThickness();
        Renderer.renderRectangle(matrixStack, Rectangle.of(getPosition().add(border.getLeft(), border.getTop()), getSize().addInnerInsets(border)), getBackgroundColor());
        Renderer.renderBorder(matrixStack, Rectangle.of(getPosition(), getSize()), border, getBorderColor());
    }
}
