package com.ddoerr.clientgui.attacher;

import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.RenderLayer;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.FocusChangeSupport;
import com.ddoerr.clientgui.views.AbstractView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.LogManager;

public class ViewScreenAdapter extends Screen {
    private final AbstractView view;

    public ViewScreenAdapter(AbstractView view) {
        super(LiteralText.EMPTY);

        this.view = view;
    }

    @Override
    public void init(MinecraftClient client, int width, int height) {
        LogManager.getLogger().info("init");
        super.init(client, width, height);
        view.setSize(new Size(width, height));
        view.addFocusListener(view);

        view.build();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Point mouse = new Point(mouseX, mouseY);
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.BACKGROUND));
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.DEFAULT));
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.FINAL));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        view.mouseDown(new MouseEvent(view, new Point((int)mouseX, (int)mouseY), InputUtil.Type.MOUSE.createFromCode(button)));
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        view.mouseUp(new MouseEvent(view, new Point((int)mouseX, (int)mouseY), InputUtil.Type.MOUSE.createFromCode(button)));
        return false;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        view.mouseMoved(new MouseEvent(view, new Point((int)mouseX, (int)mouseY)));
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        LogManager.getLogger().info("resize");
        view.setSize(new Size(width, height));
    }

    @Override
    public boolean changeFocus(boolean lookForwards) {
        view.changeFocus(lookForwards ? FocusChangeSupport.Direction.Forwards : FocusChangeSupport.Direction.Backwards);
        return false;
    }
}
