package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.events.RenderListener;
import com.ddoerr.clientgui.models.RenderLayer;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class TooltipAttachment implements RenderListener {
    protected final ListProperty<OrderedText> textLines = new SimpleListProperty<>(this, "textLines", FXCollections.observableArrayList());

    public TooltipAttachment(Widget<?> widget) {
        this.widget = widget;
    }

    private final Widget<?> widget;


    public ListProperty<OrderedText> textLinesProperty() {
        return textLines;
    }
    public ObservableList<OrderedText> getTextLines() {
        return textLines.get();
    }
    public void addTextLine(OrderedText orderedText) {
        textLines.add(orderedText);
    }
    public void addTextLine(Text text) {
        textLines.add(text.asOrderedText());
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        MinecraftClient minecraft = MinecraftClient.getInstance();

        if (renderEvent.getRenderLayer() != RenderLayer.FINAL || minecraft.currentScreen == null || !widget.isWithinWidget(renderEvent.getMouse())) {
            return;
        }

        Renderer.renderTooltip(renderEvent.getMatrixStack(), renderEvent.getMouse(), textLinesProperty().get());
    }
}
