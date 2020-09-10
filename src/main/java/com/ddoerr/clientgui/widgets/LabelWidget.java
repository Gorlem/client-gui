package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class LabelWidget extends Widget {
    protected final ObjectProperty<Text> text = new SimpleObjectProperty<>(this, "text", LiteralText.EMPTY);
    protected final ObjectProperty<Color> foregroundColor = new SimpleObjectProperty<>(this, "foregroundColor", Color.WHITE);
    protected final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(this, "backgroundColor", Color.TRANSPARENT);

    public ObjectProperty<Text> textProperty() {
        return text;
    }
    public Text getText() {
        return text.get();
    }
    public void setText(Text text) {
        this.text.set(text);
    }
    public void setText(String text) {
        setText(new LiteralText(text));
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
        super();
        size.bind(Bindings.createObjectBinding(() -> {
            int stringHeight = MinecraftClient.getInstance().textRenderer.fontHeight;
            int stringWidth = MinecraftClient.getInstance().textRenderer.getWidth(text.get());
            return new Size(
                    stringWidth + padding.get().getWidth(),
                    stringHeight + padding.get().getHeight());
        }, text, padding));
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.renderRectangle(matrixStack, new Rectangle(position.get().addInsets(margin.get()), size.get()), backgroundColor.get());
        Renderer.renderText(matrixStack, text.get(), position.get().addInsets(margin.get()).addInsets(padding.get()), foregroundColor.get());
    }
}
