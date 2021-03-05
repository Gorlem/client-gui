package com.ddoerr.clientgui.widgets.functional;


import com.ddoerr.clientgui.ClientGuiMod;
import com.ddoerr.clientgui.ClientGuiRegistries;
import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.attachments.CursorAttachment;
import com.ddoerr.clientgui.attachments.InteractiveAttachment;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.CursorManager;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.binding.Bindings;
import net.minecraft.text.LiteralText;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;

public class CheckboxWidget extends Widget<CheckboxWidget> {
    public static final Identifier IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "checkbox_widget");
    public static final String CHECKMARK = "✔";

    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(focusListeners.fire());
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final CursorAttachment cursorAttachment = new CursorAttachment(this, CursorManager.Cursor.HAND);

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
                .Do(w -> w.borderColorProperty().bind(ClientGuiRegistries.BORDER_COLOR.get(IDENTIFIER).createBinding(this)))
                .Do(w -> w.backgroundColorProperty().bind(ClientGuiRegistries.BACKGROUND_COLOR.get(IDENTIFIER).createBinding(this)))
                .setBorderThickness(Insets.of(1))
                .setChild(new LabelWidget()
                        .setMargin(Insets.of(1, 0, 0, 2))
                        .Do(w -> w.textProperty().bind(Bindings.when(activeProperty())
                                .then(new LiteralText(CHECKMARK).asOrderedText())
                                .otherwise(OrderedText.EMPTY)
                        ))
                        .attach(AnchorWidget.Anchor.MiddleCenter)
                );
    }

    public void addActionListener(ActionListener actionListener) {
        interactiveAttachment.addActionListener(actionListener);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }
}
