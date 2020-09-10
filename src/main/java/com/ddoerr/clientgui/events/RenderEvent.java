package com.ddoerr.clientgui.events;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.RenderLayer;
import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.client.util.math.MatrixStack;

public class RenderEvent extends AbstractEvent {
    private final MatrixStack matrixStack;
    private final Point mouse;
    private final RenderLayer renderLayer;

    public RenderEvent(Widget source, MatrixStack matrixStack, Point mouse, RenderLayer renderLayer) {
        super(source);
        this.matrixStack = matrixStack;
        this.mouse = mouse;
        this.renderLayer = renderLayer;
    }

    public MatrixStack getMatrixStack() {
        return matrixStack;
    }

    public Point getMouse() {
        return mouse;
    }

    public RenderLayer getRenderLayer() {
        return renderLayer;
    }
}
