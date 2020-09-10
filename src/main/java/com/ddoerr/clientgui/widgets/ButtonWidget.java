package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.util.Renderer;
import com.ddoerr.clientgui.events.ActionEvent;
import com.ddoerr.clientgui.events.ActionListener;
import com.ddoerr.clientgui.events.MouseEvent;
import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.widgets.layout.AnchorWidget;
import com.ddoerr.clientgui.widgets.layout.DelegateWidget;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.lang3.event.EventListenerSupport;

public class ButtonWidget extends DelegateWidget {
    protected final EventListenerSupport<ActionListener> actionListeners = EventListenerSupport.create(ActionListener.class);

    protected final ObjectProperty<ButtonStyle> buttonStyle = new SimpleObjectProperty<>(this, "buttonStyle", ButtonStyle.Default);

    public ObjectProperty<ButtonStyle> buttonStyleProperty() {
        return buttonStyle;
    }
    public ButtonStyle getButtonStyle() {
        return buttonStyle.get();
    }
    public void setButtonStyle(ButtonStyle buttonStyle) {
        this.buttonStyle.set(buttonStyle);
    }

    public void setText(String text) {
        LabelWidget label = new LabelWidget();
        label.setText(text);

        AnchorWidget anchor = new AnchorWidget();
        anchor.addChild(label, AnchorWidget.Anchor.MiddleCenter);

        setChild(anchor);
    }

    public void addActionListener(ActionListener actionListener) {
        actionListeners.addListener(actionListener);
    }

    @Override
    public boolean isFocusable() {
        return true;
    }

    @Override
    public void renderBackground(MatrixStack matrixStack, Point mouse) {
        Color color = Color.BLACK;

        switch (buttonStyle.get()) {
            case Primary:
                color = Color.BLUE;
                break;
            case Warning:
                color = Color.ORANGE;
                break;
            case Error:
                color = Color.RED;
                break;
            case Success:
                color = Color.GREEN;
                break;
        }

        Rectangle rectangle = new Rectangle(position.get().addInsets(margin.get()), size.get());

        Renderer.renderRectangle(matrixStack, rectangle, isWithinWidget(mouse) ? color : color.addAlpha(0.5));
        Renderer.renderBorder(matrixStack, rectangle, isFocused() ? Color.RED : Color.WHITE);
    }

    @Override
    public void mouseUp(MouseEvent mouseEvent) {
        if (isWithinWidget(mouseEvent.getPosition())) {
            actionListeners.fire().doAction(new ActionEvent(this));
            focusListeners.fire().focusChanged(new FocusEvent(this));
        }
    }

    public enum ButtonStyle {
        Default,
        Primary,
        Warning,
        Error,
        Success
    }
}
