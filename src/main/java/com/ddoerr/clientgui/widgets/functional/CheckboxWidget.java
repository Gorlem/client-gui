package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.CursorAttachment;
import com.ddoerr.clientgui.attachments.InteractiveAttachment;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.models.*;
import com.ddoerr.clientgui.templates.ColorPalette;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class CheckboxWidget extends Widget<CheckboxWidget> {
    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(this, focusListeners.fire());
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.HAND);

    protected final ObjectProperty<ColorPalette> backgroundPalette = new SimpleObjectProperty<>(this, "backgroundPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.4))
                .highlight(Color.BLACK)
                .activeHighlight(Color.GREEN)
                .active(Color.GREEN.addAlpha(0.4))
                .disabled(Color.GREY.addAlpha(0.4)));

    protected final ObjectProperty<ColorPalette> borderPalette = new SimpleObjectProperty<>(this, "borderPalette",
            new ColorPalette(Color.BLACK.addAlpha(0.7))
                    .focused(Color.BLUE.addAlpha(0.7))
                    .active(Color.GREEN.addAlpha(0.7))
                    .disabled(Color.GREY.addAlpha(0.7)));

    public CheckboxWidget() {
        setSize(Size.of(10));
        interactiveAttachment.addActionListener(actionEvent -> active.set(!isActive()));
        attach(interactiveAttachment);
        attach(cursorAttachment);
        attach(containerAttachment);

        containerAttachment.addChild(build());
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .Do(w -> w.borderColorProperty().bind(Bindings.createObjectBinding(() -> borderPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
                .Do(w -> w.backgroundColorProperty().bind(Bindings.createObjectBinding(() -> backgroundPalette.get().getColor(this), focusedProperty(), activeProperty(), highlightedProperty(), enabledProperty())))
                .setBorderThickness(Insets.of(1));
    }

    public void addActionListener(ActionListener actionListener) {
        interactiveAttachment.addActionListener(actionListener);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}
