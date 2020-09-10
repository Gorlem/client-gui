package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.attachments.Attachment;
import com.ddoerr.clientgui.events.*;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.lang3.event.EventListenerSupport;

import java.util.ArrayList;
import java.util.List;

public abstract class Widget implements MouseListener, FocusListener, RenderListener {
    protected final EventListenerSupport<FocusListener> focusListeners = EventListenerSupport.create(FocusListener.class);

    protected final ObjectProperty<Point> position = new SimpleObjectProperty<>(this, "position", Point.ORIGIN);
    protected final ObjectProperty<Size> size = new SimpleObjectProperty<>(this, "size", Size.EMPTY);
    protected final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(this, "padding", Insets.EMPTY);
    protected final ObjectProperty<Insets> margin = new SimpleObjectProperty<>(this, "margin", Insets.EMPTY);
    protected final BooleanProperty focused = new SimpleBooleanProperty(this, "focused", false);
    protected final BooleanProperty enabled = new SimpleBooleanProperty(this, "enabled", true);
    protected final BooleanProperty visible = new SimpleBooleanProperty(this, "visible", true);

    protected final ReadOnlyObjectWrapper<Size> outerSize = new ReadOnlyObjectWrapper<>(this, "outerSize", Size.EMPTY);

    protected final List<Attachment> attachments = new ArrayList<>();

    public Widget() {
        outerSize.bind(Bindings.createObjectBinding(() ->
                new Size(size.get().getWidth() + margin.get().getWidth(), size.get().getHeight() + margin.get().getHeight()),
                size, margin));
    }

    public void addFocusListener(FocusListener focusListener) {
        focusListeners.addListener(focusListener);
    }

    public void removeFocusListener(FocusListener focusListener) {
        focusListeners.removeListener(focusListener);
    }

    public ObjectProperty<Point> positionProperty() {
        return position;
    }
    public Point getPosition() {
        return position.get();
    }
    public void setPosition(Point position) {
        this.position.set(position);
    }

    public ObjectProperty<Size> sizeProperty() {
        return size;
    }
    public Size getSize() {
        return size.get();
    }
    public void setSize(Size size) {
        this.size.set(size);
    }

    public ReadOnlyObjectProperty<Size> outerSizeProperty() {
        return outerSize.getReadOnlyProperty();
    }
    public Size getOuterSize() {
        return outerSize.get();
    }

    public ObjectProperty<Insets> paddingProperty() {
        return padding;
    }
    public Insets getPadding() {
        return padding.get();
    }
    public void setPadding(Insets padding) {
        this.padding.set(padding);
    }

    public ObjectProperty<Insets> marginProperty() {
        return margin;
    }
    public Insets getMargin() {
        return margin.get();
    }
    public void setMargin(Insets margin) {
        this.margin.set(margin);
    }

    public BooleanProperty focusedProperty() {
        return focused;
    }
    public boolean isFocused() {
        return focused.get();
    }
    public void setFocused(boolean focused) {
        this.focused.set(focused);
    }

    public BooleanProperty visibleProperty() {
        return visible;
    }
    public boolean getVisible() {
        return visible.get();
    }
    public void setVisible(boolean visible) {
        this.visible.set(visible);
    }

    public BooleanProperty enabledProperty() {
        return enabled;
    }
    public boolean isEnabled() {
        return enabled.get();
    }
    public void setEnabled(boolean enabled) {
        this.enabled.set(enabled);
    }

    public boolean isFocusable() {
        return false;
    }

    public void attach(Attachment attachment) {
        attachments.add(attachment);
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        if (!visible.get()) {
            return;
        }

        switch (renderEvent.getRenderLayer()) {
            case BACKGROUND:
                renderBackground(renderEvent.getMatrixStack(), renderEvent.getMouse());
                break;
            case FINAL:
                renderFinal(renderEvent.getMatrixStack(), renderEvent.getMouse());
                break;
            default:
                render(renderEvent.getMatrixStack(), renderEvent.getMouse());
                break;
        }

        for (Object attachment : attachments) {
            if (attachment instanceof RenderListener) {
                ((RenderListener) attachment).renderLayer(renderEvent);
            }
        }
    }

    public void renderBackground(MatrixStack matrixStack, Point mouse) {}
    public void render(MatrixStack matrixStack, Point mouse) {}
    public void renderFinal(MatrixStack matrixStack, Point mouse) {}

    public boolean isWithinWidget(Point point) {
        return point.getX() >= position.get().getX() + margin.get().getLeft() && point.getX() <= position.get().getX() + margin.get().getLeft() + size.get().getWidth()
                && point.getY() >= position.get().getY() + margin.get().getTop() && point.getY() <= position.get().getY() + margin.get().getTop()  + size.get().getHeight();
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        setFocused(focusEvent.getSource().equals(this));
    }
}
