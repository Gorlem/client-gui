package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.ClientGuiMod;
import com.ddoerr.clientgui.ClientGuiRegistries;
import com.ddoerr.clientgui.attachments.*;
import com.ddoerr.clientgui.bindings.BindingUtil;
import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.models.ShortcutBuilder;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class TextFieldWidget extends Widget<TextFieldWidget> {
    public static final Identifier IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "text_field_widget");

    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.IBEAM);
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();
    protected final SelectionAttachment selectionAttachment = new SelectionAttachment();
    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(focusListeners.fire());

    public TextFieldWidget() {
        attach(cursorAttachment);
        attach(containerAttachment);
        attach(shortcutAttachment);
        attach(selectionAttachment);
        attach(interactiveAttachment);

        containerAttachment.addChild(build());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.backspace").whenFocused().build(), e -> removeLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.delete").whenFocused().build(), e -> removeRight());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.left").whenFocused().build(), e -> moveCursorLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.left").whenFocused().andShift().build(), e -> moveSelectionLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.right").whenFocused().build(), e -> moveCursorRight());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.right").whenFocused().andShift().build(), e -> moveSelectionRight());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.v").whenFocused().andControl().build(), e -> paste());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.c").whenFocused().andControl().build(), e -> copy());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.x").whenFocused().andControl().build(), e -> cut());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.a").whenFocused().andControl().build(), e -> selectAll());

        MinecraftClient.getInstance().keyboard.setRepeatEvents(true);
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .Do(w -> w.borderColorProperty().bind(ClientGuiRegistries.BORDER_COLOR.get(IDENTIFIER).createBinding(this)))
                .Do(w -> w.backgroundColorProperty().bind(ClientGuiRegistries.BACKGROUND_COLOR.get(IDENTIFIER).createBinding(this)))
                .setBorderThickness(Insets.of(0, 0, 1, 0))
                .setPadding(Insets.of(2))
                .setChild(
                        new AnchorWidget().addChild(new LabelWidget().Do(l -> l.textProperty().bind(BindingUtil.text(selectionAttachment.contentProperty()))), AnchorWidget.Anchor.MiddleLeft)
                );
    }

    public void moveCursorLeft() {
        int index = selectionAttachment.moveCursorIndex(-1);
        selectionAttachment.setSelectionIndex(index);
    }

    public void moveSelectionLeft() {
        selectionAttachment.moveCursorIndex(-1);
    }

    public void moveCursorRight() {
        int index = selectionAttachment.moveCursorIndex(1);
        selectionAttachment.setSelectionIndex(index);
    }

    public void moveSelectionRight() {
        selectionAttachment.moveCursorIndex(1);
    }

    public void removeLeft() {
        selectionAttachment.remove(-1);
    }

    public void removeRight() {
        selectionAttachment.remove(1);
    }

    public void selectAll() {
        selectionAttachment.setSelectionIndex(0);
        selectionAttachment.setCursorIndex(-1);
    }

    public void paste() {
        selectionAttachment.append(MinecraftClient.getInstance().keyboard.getClipboard());
    }

    public void copy() {
        String selection = selectionAttachment.selectionContentProperty().get();
        MinecraftClient.getInstance().keyboard.setClipboard(selection);
    }

    public void cut() {
        String selection = selectionAttachment.selectionContentProperty().get();
        selectionAttachment.remove(0);
        MinecraftClient.getInstance().keyboard.setClipboard(selection);
    }

    @Override
    public ActionResult characterTyped(KeyboardEvent keyboardEvent) {
        if (isFocused() && keyboardEvent.getCharacter().isPresent()) {
            selectionAttachment.append(keyboardEvent.getCharacter().get());
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult mouseDown(MouseEvent mouseEvent) {
        if (isWithinWidget(mouseEvent.getPosition())) {
            double width = mouseEvent.getPosition().getX() - position.get().getX() + 2;
            int index = MinecraftClient.getInstance().textRenderer.trimToWidth(selectionAttachment.contentProperty().get(), (int) width).length();
            selectionAttachment.setCursorIndex(index);
            selectionAttachment.setSelectionIndex(index);
        }

        return super.mouseDown(mouseEvent);
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        if (!isWithinWidget(mouseEvent.getPosition())) {
            return super.mouseDragged(mouseEvent);
        }

        double width = mouseEvent.getPosition().getX() - position.get().getX() + 2;
        int index = MinecraftClient.getInstance().textRenderer.trimToWidth(selectionAttachment.contentProperty().get(), (int) width).length();
        selectionAttachment.setCursorIndex(index);
        selectionAttachment.setSelectionIndex(index);

        return ActionResult.SUCCESS;
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void renderFinal(MatrixStack matrixStack, Point mouse) {
        super.renderFinal(matrixStack, mouse);

        Rectangle cursor = selectionAttachment.cursorProperty().get();
        Rectangle selection = selectionAttachment.selectionProperty().get();

        double x = getPosition().getX() + 2;
        double y = getPosition().getY() + (getSize().getHeight() - cursor.getSize().getHeight()) / 2;

        if (isFocused()) {
            Renderer.renderRectangle(matrixStack, Rectangle.of(cursor.getTopLeftPoint().add(x, y), cursor.getSize()), Color.GREY);
        }
        Renderer.renderRectangle(matrixStack, Rectangle.of(selection.getTopLeftPoint().add(x, y), selection.getSize()), Color.GREY.addAlpha(0.5));
    }
}
