package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.attachments.Attachment;
import com.ddoerr.clientgui.bindings.SizeProperty;
import com.ddoerr.clientgui.events.*;
import com.ddoerr.clientgui.models.Insets;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.EventUtil;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.ActionResult;
import org.apache.commons.lang3.event.EventListenerSupport;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public abstract class Widget<W extends Widget<W>> implements MouseListener, FocusListener, RenderListener, KeyboardListener {
    // TODO: make protected again, see DropdownWidget L45
    public final EventListenerSupport<FocusListener> focusListeners = EventListenerSupport.create(FocusListener.class);

    protected final ObjectProperty<Point> position = new SimpleObjectProperty<>(this, "position", Point.ORIGIN);
    protected final SizeProperty size = new SizeProperty(this, "size", Size.EMPTY);
    protected final ObjectProperty<Insets> padding = new SimpleObjectProperty<>(this, "padding", Insets.EMPTY);
    protected final ObjectProperty<Insets> margin = new SimpleObjectProperty<>(this, "margin", Insets.EMPTY);
    protected final BooleanProperty focused = new SimpleBooleanProperty(this, "focused", false);
    protected final BooleanProperty enabled = new SimpleBooleanProperty(this, "enabled", true);
    protected final BooleanProperty visible = new SimpleBooleanProperty(this, "visible", true);
    protected final BooleanProperty active = new SimpleBooleanProperty(this, "active", false);
    protected final BooleanProperty highlighted = new SimpleBooleanProperty(this, "highlighted", false);

    protected final ReadOnlyObjectWrapper<Size> outerSize = new ReadOnlyObjectWrapper<>(this, "outerSize", Size.EMPTY);

    protected final List<Object> attachments = new ArrayList<>();

    public Widget() {
        outerSize.bind(sizeProperty().addOuterInsets(marginProperty()));

        // HACK: Enables eager evaluation.
        // For some reason lazy evaluation stops working after the first resize
        outerSize.addListener((prop, old, now) -> { });
        highlighted.addListener((prop, old, now) -> { });
    }

    public W Do(Consumer<W> consumer) {
        consumer.accept((W) this);
        return (W) this;
    }

    public void addFocusListener(FocusListener focusListener) {
        focusListeners.addListener(focusListener);
    }

    public void removeFocusListener(FocusListener focusListener) {
        focusListeners.removeListener(focusListener);
    }

    public ObjectProperty<Point> positionProperty() { return position; }
    public Point getPosition() { return position.get(); }
    public void setPosition(Point position) { this.position.set(position); }

    public SizeProperty sizeProperty() { return size; }
    public Size getSize() { return size.get(); }
    public W setSize(Size size) {
        this.size.set(size);
        return (W) this;
    }

    public ReadOnlyObjectProperty<Size> outerSizeProperty() { return outerSize.getReadOnlyProperty(); }
    public Size getOuterSize() {  return outerSize.get(); }

    public ObjectProperty<Insets> paddingProperty() { return padding; }
    public Insets getPadding() { return padding.get(); }
    public W setPadding(Insets padding) {
        this.padding.set(padding);
        return (W) this;
    }

    public ObjectProperty<Insets> marginProperty() { return margin; }
    public Insets getMargin() { return margin.get(); }
    public W setMargin(Insets margin) {
        this.margin.set(margin);
        return (W) this;
    }

    public BooleanProperty focusedProperty() { return focused; }
    public boolean isFocused() { return focused.get(); }
    public void setFocused(boolean focused) { this.focused.set(focused); }

    public BooleanProperty visibleProperty() { return visible; }
    public boolean isVisible() { return visible.get(); }
    public void setVisible(boolean visible) { this.visible.set(visible); }

    public BooleanProperty enabledProperty() { return enabled; }
    public boolean isEnabled() { return enabled.get(); }
    public void setEnabled(boolean enabled) { this.enabled.set(enabled); }

    public BooleanProperty activeProperty() {
        return active;
    }
    public boolean isActive() {
        return active.get();
    }

    public BooleanProperty highlightedProperty() {
        return highlighted;
    }
    public boolean isHighlighted() {
        return highlighted.get();
    }

    public boolean isFocusable() { return false; }

    public W attach(Object attachment) {
        attachments.add(attachment);

        if (attachment instanceof Attachment) {
            ((Attachment) attachment).initialize(this);
        }

        return (W) this;
    }

    public Optional<Object> findFirstAttachment(Predicate<Object> attachmentPredicate) {
        return attachments.stream().filter(attachmentPredicate).findFirst();
    }
    public <T> Optional<T> findFirstAttachment(Class<T> attachmentClass) {
        return (Optional<T>) findFirstAttachment(attachmentClass::isInstance);
    }

    public List<Object> findAllAttachments(Predicate<Object> attachmentPredicate) {
        return attachments.stream().filter(attachmentPredicate).collect(Collectors.toList());
    }
    public <T> List<T> findAllAttachments(Class<T> attachmentClass) {
        return (List<T>) findAllAttachments(attachmentClass::isInstance);
    }

    public List<Object> getAllAttachments() {
        return Collections.unmodifiableList(attachments);
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

    @Override
    public ActionResult mouseMoved(MouseEvent mouseEvent) {
        if (isFocusable()) {
            if (isWithinWidget(mouseEvent.getPosition())) {
                highlighted.set(true);
            } else {
                highlighted.set(false);
            }
        }

        return EventUtil.handle(findAllAttachments(MouseListener.class), listener -> listener.mouseMoved(mouseEvent));
    }

    @Override
    public ActionResult mouseDown(MouseEvent mouseEvent) {
        return EventUtil.handle(findAllAttachments(MouseListener.class), listener -> listener.mouseDown(mouseEvent));
    }

    @Override
    public ActionResult mouseUp(MouseEvent mouseEvent) {
        return EventUtil.handle(findAllAttachments(MouseListener.class), listener -> listener.mouseUp(mouseEvent));
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        return EventUtil.handle(findAllAttachments(MouseListener.class), listener -> listener.mouseDragged(mouseEvent));
    }

    public void renderBackground(MatrixStack matrixStack, Point mouse) {}
    public void render(MatrixStack matrixStack, Point mouse) {}
    public void renderFinal(MatrixStack matrixStack, Point mouse) {}

    public boolean isWithinWidget(Point point) {
        return point.getX() >= position.get().getX() + margin.get().getLeft() && point.getX() < position.get().getX() + margin.get().getLeft() + size.get().getWidth()
                && point.getY() >= position.get().getY() + margin.get().getTop() && point.getY() < position.get().getY() + margin.get().getTop()  + size.get().getHeight();
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        for (FocusListener focusListener : findAllAttachments(FocusListener.class)) {
            focusListener.focusChanged(focusEvent);
        }

        setFocused(focusEvent.getSource() == this);
    }

    @Override
    public ActionResult keyUp(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(findAllAttachments(KeyboardListener.class), listener -> listener.keyUp(keyboardEvent));
    }

    @Override
    public ActionResult keyDown(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(findAllAttachments(KeyboardListener.class), listener -> listener.keyDown(keyboardEvent));
    }

    @Override
    public ActionResult characterTyped(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(findAllAttachments(KeyboardListener.class), listener -> listener.characterTyped(keyboardEvent));
    }
}
