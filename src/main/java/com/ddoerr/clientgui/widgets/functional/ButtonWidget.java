package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.CursorAttachment;
import com.ddoerr.clientgui.attachments.InteractiveAttachment;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.templates.ColorPalette;
import com.ddoerr.clientgui.bindings.BindingUtil;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class ButtonWidget extends Widget<ButtonWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(this, focusListeners.fire());
    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.HAND);

    protected final ObjectProperty<ColorPalette> backgroundPalette = new SimpleObjectProperty<>(this, "backgroundPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.4))
                    .active(Color.BLACK)
                    .highlight(Color.BLACK)
                    .disabled(Color.GREY.addAlpha(0.4)));

    protected final ObjectProperty<ColorPalette> borderPalette = new SimpleObjectProperty<>(this, "borderPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.7))
                    .focused(Color.BLUE.addAlpha(0.7))
                    .disabled(Color.GREY.addAlpha(0.7)));

    protected final ObjectProperty<Widget<?>> child = new SimpleObjectProperty<>(this, "child");

    public ButtonWidget() {
        attach(interactiveAttachment);
        attach(containerAttachment);
        attach(cursorAttachment);

        containerAttachment.addChild(build());
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .Do(w -> w.borderColorProperty().bind(Bindings.createObjectBinding(() -> borderPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
                .Do(w -> w.backgroundColorProperty().bind(Bindings.createObjectBinding(() -> backgroundPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
                .setBorderThickness(Insets.of(1))
                .setChild(
                        new AnchorWidget().Do(w -> w.childrenProperty().bindContent(BindingUtil.single(child)))
                );
    }

    public ButtonWidget setText(String text) {
        setChild(new LabelWidget().setText(text).attach(AnchorWidget.Anchor.MiddleCenter));
        return this;
    }

    public ButtonWidget setChild(Widget<?> widget) {
        child.set(widget);
        return this;
    }

    public ButtonWidget addActionListener(ActionListener actionListener) {
        interactiveAttachment.addActionListener(actionListener);
        return this;
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}
