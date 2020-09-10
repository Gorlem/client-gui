package com.ddoerr.clientgui.views;

import com.ddoerr.clientgui.attacher.ViewScreenAdapter;
import com.ddoerr.clientgui.util.FocusChangeSupport;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.widgets.layout.DelegateWidget;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;

public abstract class AbstractView extends DelegateWidget implements HudRenderCallback {
    protected final FocusChangeSupport focusChangeSupport = new FocusChangeSupport(this);

    private boolean calledBuild = false;

    public abstract void build();

    public void changeFocus(FocusChangeSupport.Direction direction) {
        focusChangeSupport.changeFocus(direction);
    }

    public Screen asScreen() {
        return new ViewScreenAdapter(this);
    }

    public void attachToHud() {
        HudRenderCallback.EVENT.register(this);
        this.addFocusListener(this);
    }

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        Window window = MinecraftClient.getInstance().getWindow();
        this.setSize(new Size(window.getScaledWidth(), window.getScaledHeight()));

        if (!calledBuild) {
            calledBuild = true;
            this.build();
        }

        this.render(matrixStack, Point.OFF_SCREEN);
    }
}
