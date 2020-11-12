package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.CursorAttachment;
import com.ddoerr.clientgui.attachments.SelectionAttachment;
import com.ddoerr.clientgui.attachments.ShortcutAttachment;
import com.ddoerr.clientgui.bindings.BindingUtil;
import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.events.KeyboardEvent;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.templates.ColorPalette;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.util.ShortcutBuilder;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;

public class TextFieldWidget extends Widget<TextFieldWidget> {
    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.IBEAM);
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();
    protected final SelectionAttachment selectionAttachment = new SelectionAttachment();

    protected final ObjectProperty<ColorPalette> backgroundPalette = new SimpleObjectProperty<>(this, "backgroundPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.4))
                    .disabled(Color.GREY.addAlpha(0.4)));

    protected final ObjectProperty<ColorPalette> borderPalette = new SimpleObjectProperty<>(this, "borderPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.7))
                    .focused(Color.BLUE.addAlpha(0.7))
                    .disabled(Color.GREY.addAlpha(0.7)));

    public TextFieldWidget() {
        attach(cursorAttachment);
        attach(containerAttachment);
        attach(shortcutAttachment);
        attach(selectionAttachment);

        containerAttachment.addChild(build());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.backspace"), e -> removeLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.delete"), e -> removeRight());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.left"), e -> moveCursorLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.left").andShift(), e -> moveSelectionLeft());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.right"), e -> moveCursorRight());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.right").andShift(), e -> moveSelectionRight());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.v").andControl(), e -> paste());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.c").andControl(), e -> copy());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.x").andControl(), e -> cut());

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.a").andControl(), e -> selectAll());

        MinecraftClient.getInstance().keyboard.setRepeatEvents(true);
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .Do(w -> w.borderColorProperty().bind(Bindings.createObjectBinding(() -> borderPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
                .Do(w -> w.backgroundColorProperty().bind(Bindings.createObjectBinding(() -> backgroundPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
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
        if (!isWithinWidget(mouseEvent.getPosition())) {
            return super.mouseUp(mouseEvent);
        }

        if (!isFocused()) {
            focusListeners.fire().focusChanged(new FocusEvent(this));
        }

        int width = mouseEvent.getPosition().getX() - position.get().getX() + 2;
        int index = MinecraftClient.getInstance().textRenderer.trimToWidth(selectionAttachment.contentProperty().get(), width).length();
        selectionAttachment.setCursorIndex(index);
        selectionAttachment.setSelectionIndex(index);

        return ActionResult.SUCCESS;
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        if (!isWithinWidget(mouseEvent.getPosition())) {
            return super.mouseDragged(mouseEvent);
        }

        int width = mouseEvent.getPosition().getX() - position.get().getX() + 2;
        int index = MinecraftClient.getInstance().textRenderer.trimToWidth(selectionAttachment.contentProperty().get(), width).length();
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

        int x = getPosition().getX() + 2;
        int y = getPosition().getY() + (getSize().getHeight() - cursor.getSize().getHeight()) / 2;

        if (isFocused()) {
            Renderer.renderRectangle(matrixStack, Rectangle.of(cursor.getTopLeftPoint().add(x, y), cursor.getSize()), Color.GREY);
        }
        Renderer.renderRectangle(matrixStack, Rectangle.of(selection.getTopLeftPoint().add(x, y), selection.getSize()), Color.GREY.addAlpha(0.5));
    }
}
