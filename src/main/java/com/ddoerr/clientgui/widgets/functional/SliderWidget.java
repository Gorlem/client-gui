package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.ClientGuiMod;
import com.ddoerr.clientgui.ClientGuiRegistries;
import com.ddoerr.clientgui.attachments.*;
import com.ddoerr.clientgui.bindings.ClampedDoubleProperty;
import com.ddoerr.clientgui.models.Axis;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.ShortcutBuilder;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.visual.BorderWidget;
import com.ddoerr.clientgui.widgets.visual.ColorWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import net.minecraft.util.Identifier;

public class SliderWidget extends Widget<SliderWidget> {
    public static final Identifier IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "slider_widget");

    protected final ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());
    protected final InteractiveAttachment interactiveAttachment = new InteractiveAttachment(focusListeners.fire());
    protected final ShortcutAttachment shortcutAttachment = new ShortcutAttachment();

    // Will be added to the grip instead of the SliderWidget
    protected final DraggableAttachment draggableAttachment = new DraggableAttachment();

    protected final ObjectProperty<Axis> axis = new SimpleObjectProperty<>(this, "axis", Axis.Vertical);
    protected final DoubleProperty value = new ClampedDoubleProperty(this, "value", 0, 0, 1);
    protected final DoubleProperty gripSizeFactor = new ClampedDoubleProperty(this, "gripSizeFactor", 0, 0, 1);

    public SliderWidget() {
        attach(containerAttachment);
        attach(interactiveAttachment);
        attach(shortcutAttachment);

        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.up").whenFocused().build(), e -> moveNegative());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.left").whenFocused().build(), e -> moveNegative());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.down").whenFocused().build(), e -> movePositive());
        shortcutAttachment.addShortcut(ShortcutBuilder.of("key.keyboard.right").whenFocused().build(), e -> movePositive());

        containerAttachment.addChild(build());
        value.bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
            .then(draggableAttachment.heightFactorProperty())
            .otherwise(draggableAttachment.widthFactorProperty())
        );
    }

    protected Widget<?> build() {
        return new BorderWidget()
                .setBorderThickness(Insets.of(0, 1))
                .Do(w -> w.borderColorProperty().bind(ClientGuiRegistries.BORDER_COLOR.get(IDENTIFIER).createBinding(this)))
                .Do(w -> w.backgroundColorProperty().set(ClientGuiRegistries.BACKGROUND_COLOR.get(IDENTIFIER).getBaseColor()))
                .Do(w -> {
                    DragAreaAttachment dragArea = new DragAreaAttachment(w.focusListeners.fire());
                    w.attach(dragArea);
                    dragArea.addChild(
                            new ColorWidget()
                                    .Do(s -> s.colorProperty().bind(ClientGuiRegistries.BACKGROUND_COLOR.get(IDENTIFIER).createBinding(this)))
                                    .Do(s -> s.sizeProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                                            .then(sizeProperty().setHeight(gripSizeFactorProperty().multiply(Bindings.selectInteger(sizeProperty(), "height"))))
                                            .otherwise(sizeProperty().setWidth(gripSizeFactorProperty().multiply(Bindings.selectInteger(sizeProperty(), "width"))))
                                    ))
                                    .attach(draggableAttachment)
                    );
                });
    }

    public ObjectProperty<Axis> axisProperty() {
        return axis;
    }

    public DoubleProperty valueProperty() {
        return value;
    }

    public DoubleProperty gripSizeFactorProperty() {
        return gripSizeFactor;
    }

    public SliderWidget moveNegative() {
        if (axis.get() == Axis.Vertical) {
            draggableAttachment.decrementHeightFactor();
        } else {
            draggableAttachment.decrementWidthFactor();
        }
        return this;
    }

    public SliderWidget movePositive() {
        if (axis.get() == Axis.Vertical) {
            draggableAttachment.incrementHeightFactor();
        } else {
            draggableAttachment.incrementWidthFactor();
        }
        return this;
    }
}
