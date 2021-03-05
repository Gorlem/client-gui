package com.ddoerr.clientgui.views;

import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.events.RenderEvent;
import com.ddoerr.clientgui.models.Modifier;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.RenderLayer;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.views.AbstractView;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.ActionResult;

import java.util.EnumSet;

public class ViewScreenAdapter extends Screen {
    private final AbstractView view;

    public ViewScreenAdapter(AbstractView view) {
        super(LiteralText.EMPTY);

        this.view = view;
    }

    @Override
    public void init(MinecraftClient client, int width, int height) {
        super.init(client, width, height);
        view.setSize(Size.of(width, height));

        view.build();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        Point mouse = Point.of(mouseX, mouseY);
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.BACKGROUND));
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.DEFAULT));
        view.renderLayer(new RenderEvent(view, matrices, mouse, RenderLayer.FINAL));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        InputUtil.Key mouse = InputUtil.Type.MOUSE.createFromCode(button);
        ActionResult result = view.mouseDown(new MouseEvent(view, Point.of((float) mouseX, (float) mouseY), mouse));
        return result != ActionResult.PASS || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        InputUtil.Key mouse = InputUtil.Type.MOUSE.createFromCode(button);
        ActionResult result = view.mouseUp(new MouseEvent(view, Point.of((float) mouseX, (float) mouseY), mouse));
        return result != ActionResult.PASS || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        view.mouseMoved(new MouseEvent(view, Point.of((float)mouseX, (float)mouseY)));
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        InputUtil.Key mouse = InputUtil.Type.MOUSE.createFromCode(button);
        ActionResult result = view.mouseDragged(new MouseEvent(view, Point.of((float) mouseX, (float) mouseY), mouse, Point.of((float) deltaX, (float) deltaY)));
        return result != ActionResult.PASS || super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.width = width;
        this.height = height;
        view.setSize(Size.of(width, height));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EnumSet<Modifier> modifierSet = EnumSet.noneOf(Modifier.class);

        for (Modifier value : Modifier.values()) {
            if ((modifiers & value.getModifier()) == value.getModifier()) {
                modifierSet.add(value);
            }
        }

        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
        ActionResult result = view.keyDown(new KeyboardEvent(view, key, modifierSet));
        return result != ActionResult.PASS;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        EnumSet<Modifier> modifierSet = EnumSet.noneOf(Modifier.class);

        for (Modifier value : Modifier.values()) {
            if ((modifiers & value.getModifier()) == value.getModifier()) {
                modifierSet.add(value);
            }
        }

        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
        ActionResult result = view.keyUp(new KeyboardEvent(view, key, modifierSet));
        return result != ActionResult.PASS;
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        InputUtil.Key key = InputUtil.Type.KEYSYM.createFromCode(keyCode);
        ActionResult result = view.characterTyped(new KeyboardEvent(view, key, chr));
        return result != ActionResult.PASS;
    }
}
