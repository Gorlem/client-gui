package com.ddoerr.clientgui.widgets.functional;

import com.ddoerr.clientgui.ClientGuiMod;
import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.bindings.ClampedDoubleProperty;
import com.ddoerr.clientgui.models.Axis;
import com.ddoerr.clientgui.widgets.Widget;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.layout.DynamicStackWidget;
import com.ddoerr.clientgui.widgets.visual.LabelWidget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import net.minecraft.util.Identifier;

public class ScrollbarWidget extends Widget<ScrollbarWidget> {
    public static final Identifier IDENTIFIER = new Identifier(ClientGuiMod.CLIENT_GUI_NAMESPACE, "scrollbar_widget");
    public static final String ARROW_UP = "↑";
    public static final String ARROW_LEFT = "←";
    public static final String ARROW_DOWN = "↓";
    public static final String ARROW_RIGHT = "→";

    protected ContainerAttachment containerAttachment = new ContainerAttachment(focusListeners.fire());

    protected final ObjectProperty<Axis> axis = new SimpleObjectProperty<>(this, "axis", Axis.Vertical);
    protected final DoubleProperty scrollFactor = new ClampedDoubleProperty(this, "scrollFactor", 0, 0, 1);
    protected final DoubleProperty gripSizeFactor = new ClampedDoubleProperty(this, "gripSizeFactor", 0, 0, 1);

    public ScrollbarWidget() {
        attach(containerAttachment);
        containerAttachment.addChild(build());
    }

    protected Widget<?> build() {
        SliderWidget sliderWidget = new SliderWidget()
                .Do(w -> w.gripSizeFactorProperty().bind(gripSizeFactorProperty()))
                .Do(w -> w.sizeProperty().bind(Bindings.when(axisProperty().isEqualTo(Axis.Vertical))
                        .then(sizeProperty()
                                .subtractHeight(Bindings.selectInteger(sizeProperty(), "width"))
                                .subtractHeight(Bindings.selectInteger(sizeProperty(), "width")))
                        .otherwise(sizeProperty()
                                .subtractWidth(Bindings.selectInteger(sizeProperty(), "height"))
                                .subtractWidth(Bindings.selectInteger(sizeProperty(), "height"))))
                )
                .Do(w -> w.axisProperty().bind(axisProperty()));

        scrollFactor.bindBidirectional(sliderWidget.valueProperty());

        return new DynamicStackWidget()
                .Do(w -> w.axisProperty().bind(axisProperty()))
                .addChild(new ButtonWidget()
                        .Do(w -> w.childProperty().bind(Bindings.when(axis.isEqualTo(Axis.Vertical))
                                .then(new LabelWidget().setText(ARROW_UP).attach(AnchorWidget.Anchor.MiddleCenter))
                                .otherwise(new LabelWidget().setText(ARROW_LEFT).attach(AnchorWidget.Anchor.MiddleCenter))
                        ))
                        .Do(w -> w.sizeProperty().bind(Bindings.when(axis.isEqualTo(Axis.Vertical))
                                .then(sizeProperty().setHeight(Bindings.selectInteger(sizeProperty(), "width")))
                                .otherwise(sizeProperty().setWidth(Bindings.selectInteger(sizeProperty(), "height")))
                        ))
                        .addActionListener(e -> sliderWidget.moveNegative())
                )
                .addChild(sliderWidget)
                .addChild(new ButtonWidget()
                        .Do(w -> w.childProperty().bind(Bindings.when(axis.isEqualTo(Axis.Vertical))
                                .then(new LabelWidget().setText(ARROW_DOWN).attach(AnchorWidget.Anchor.MiddleCenter))
                                .otherwise(new LabelWidget().setText(ARROW_RIGHT).attach(AnchorWidget.Anchor.MiddleCenter))
                        ))
                        .Do(w -> w.sizeProperty().bind(Bindings.when(axis.isEqualTo(Axis.Vertical))
                                .then(sizeProperty().setHeight(Bindings.selectInteger(sizeProperty(), "width")))
                                .otherwise(sizeProperty().setWidth(Bindings.selectInteger(sizeProperty(), "height")))
                        ))
                        .addActionListener(e -> sliderWidget.movePositive())
                );
    }

    public ObjectProperty<Axis> axisProperty() {
        return axis;
    }
    public ScrollbarWidget setAxis(Axis axis) {
        this.axis.set(axis);
        return this;
    }

    public DoubleProperty gripSizeFactorProperty() {
        return gripSizeFactor;
    }

    public DoubleProperty scrollFactoryProperty() {
        return scrollFactor;
    }
}
