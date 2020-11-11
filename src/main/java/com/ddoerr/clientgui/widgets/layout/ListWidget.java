package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import net.minecraft.client.util.math.MatrixStack;

public class ListWidget extends Widget<ListWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(this, focusListeners.fire());

    protected final IntegerProperty scrollPosition = new SimpleIntegerProperty(this, "scrollPosition");

    public ListWidget() {
        attach(containerAttachment);
    }

    public void addChild(Widget<?> widget) {
        containerAttachment.addChild(widget, null, false);
    }


    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.enableScissor(Rectangle.of(getPosition(), getSize()));
        super.render(matrixStack, mouse);
        Renderer.disableScissor();
    }
}
