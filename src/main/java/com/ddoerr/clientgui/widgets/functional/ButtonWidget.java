package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.ClientGuiMod;
import com.ddoerr.clientgui.ClientGuiRegistries;
import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.CursorAttachment;
import com.ddoerr.clientgui.attachments.InteractiveAttachment;
import com.ddoerr.clientgui.bindings.BindingUtil;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.util.Identifier;

public class ButtonWidget extends Widget<ButtonWidget> {
    public static final Identifier IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "button_widget");

    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(focusListeners.fire());
    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.HAND);

    protected final ObjectProperty<Widget<?>> child = new SimpleObjectProperty<>(this, "child");

    public ButtonWidget() {
        attach(interactiveAttachment);
        attach(containerAttachment);
        attach(cursorAttachment);

        containerAttachment.addChild(build());
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .Do(w -> w.borderColorProperty().bind(ClientGuiRegistries.BORDER_COLOR.get(IDENTIFIER).createBinding(this)))
                .Do(w -> w.backgroundColorProperty().bind(ClientGuiRegistries.BACKGROUND_COLOR.get(IDENTIFIER).createBinding(this)))
                .setBorderThickness(Insets.of(1))
                .setChild(
                        new AnchorWidget().Do(w -> w.childrenProperty().bindContent(BindingUtil.single(child)))
                );
    }

    public ObjectProperty<Widget<?>> childProperty() {
        return child;
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
