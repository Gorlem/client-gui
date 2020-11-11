package com.ddoerr.clientgui.widgets.visual;

import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class LabelWidget extends Widget<LabelWidget> {
    protected final ObjectProperty<OrderedText> text = new SimpleObjectProperty<OrderedText>(this, "text", OrderedText.EMPTY);
    protected final ObjectProperty<Color> foregroundColor = new SimpleObjectProperty<>(this, "foregroundColor", Color.WHITE);
    protected final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(this, "backgroundColor", Color.TRANSPARENT);

    public ObjectProperty<OrderedText> textProperty() {
        return text;
    }
    public OrderedText getText() {
        return text.get();
    }
    public LabelWidget setText(OrderedText text) {
        this.text.set(text);
        return this;
    }
    public LabelWidget setText(Text text) {
        return setText(text.asOrderedText());
    }
    public LabelWidget setText(String text) {
        return setText(new LiteralText(text).asOrderedText());
    }

    public ObjectProperty<Color> foregroundColorProperty() {
        return foregroundColor;
    }
    public Color getForegroundColor() {
        return foregroundColor.get();
    }
    public void setForegroundColor(Color foregroundColor) {
        this.foregroundColor.set(foregroundColor);
    }

    public ObjectProperty<Color> backgroundColorProperty() {
        return backgroundColor;
    }
    public Color getBackgroundColor() {
        return backgroundColor.get();
    }
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    public LabelWidget() {
        size.bind(Bindings.createObjectBinding(() -> {
            int stringHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
            int stringWidth = MinecraftClient.getInstance().textRenderer.getWidth(getText());
            return Size.of(stringWidth, stringHeight).addOuterInsets(getPadding());
        }, text, padding));
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.renderRectangle(matrixStack, Rectangle.of(getPosition().addInnerInsets(getMargin()), getSize()), getBackgroundColor());
        Renderer.renderText(matrixStack, getText(), getPosition().addInnerInsets(getMargin()).addInnerInsets(getPadding()), getForegroundColor());
    }
}
