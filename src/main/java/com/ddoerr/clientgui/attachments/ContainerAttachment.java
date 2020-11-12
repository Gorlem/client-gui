package com.ddoerr.clientgui.attachments;

import com.ddoerr.clientgui.events.*;
import com.ddoerr.clientgui.util.EventUtil;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.function.Consumer;

public class ContainerAttachment implements MouseListener, RenderListener, FocusListener, KeyboardListener, Attachment {
    protected final ListProperty<Widget<?>> children = new SimpleListProperty<>(this, "children", FXCollections.observableArrayList());

    private final FocusListener focusListener;

    private Consumer<Widget<?>> widgetAddedConsumer;
    private Consumer<Widget<?>> widgetRemovedConsumer;

    public ContainerAttachment(FocusListener focusListener) {
        this.focusListener = focusListener;
        children.addListener((ListChangeListener<? super Widget<?>>) this::handleChange);
    }

    @Override
    public void initialize(Widget<?> widget) {
        if (widgetRemovedConsumer != null && widgetAddedConsumer != null) {
            return;
        }

        setWidgetConsumer(addedWidget -> {
            addedWidget.positionProperty().bind(Bindings.createObjectBinding(() ->
                            widget.getPosition().addInnerInsets(widget.getMargin()).addInnerInsets(widget.getPadding()),
                    widget.positionProperty(), widget.marginProperty(), widget.paddingProperty()));

            addedWidget.sizeProperty().bind(Bindings.createObjectBinding(() ->
                            widget.getSize().addInnerInsets(widget.getPadding()).addInnerInsets(addedWidget.getMargin()),
                    widget.sizeProperty(), widget.paddingProperty(), addedWidget.marginProperty()));
        }, removedWidget -> {
            removedWidget.positionProperty().unbind();
            removedWidget.sizeProperty().unbind();
        });
    }

    public void setWidgetConsumer(Consumer<Widget<?>> widgetAddedConsumer, Consumer<Widget<?>> widgetRemovedConsumer) {
        this.widgetAddedConsumer = widgetAddedConsumer;
        this.widgetRemovedConsumer = widgetRemovedConsumer;
    }

    protected void handleChange(ListChangeListener.Change<? extends Widget<?>> changes) {
        while (changes.next()) {
            if (!changes.wasPermutated() && !changes.wasUpdated()) {
                for (Widget<?> oldWidget : changes.getRemoved()) {
                    if (oldWidget == null) {
                        continue;
                    }

                    widgetRemovedConsumer.accept(oldWidget);
                    oldWidget.removeFocusListener(focusListener);
                }

                for (Widget<?> newWidget : changes.getAddedSubList()) {
                    if (newWidget == null) {
                        continue;
                    }

                    widgetAddedConsumer.accept(newWidget);
                    newWidget.addFocusListener(focusListener);
                }
            }
        }
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return children;
    }
    public void addChild(Widget<?> child) {
        children.add(child);
    }
    public List<Widget<?>> getChildren() {
        return children.get();
    }
    public void removeChildren() {
        children.clear();
    }
    public void removeChildrenWithData(Object data) {
        children.removeIf(w -> w.findFirstAttachment(d -> d == data).isPresent());
    }

    @Override
    public ActionResult mouseDown(MouseEvent mouseEvent) {
        return EventUtil.handle(children.get(), child -> child.mouseDown(mouseEvent));
    }

    @Override
    public ActionResult mouseUp(MouseEvent mouseEvent) {
        return EventUtil.handle(children.get(), child -> child.mouseUp(mouseEvent));
    }

    @Override
    public ActionResult mouseMoved(MouseEvent mouseEvent) {
        return EventUtil.handle(children.get(), child -> child.mouseMoved(mouseEvent));
    }

    @Override
    public ActionResult mouseDragged(MouseEvent mouseEvent) {
        return EventUtil.handle(children.get(), child -> child.mouseDragged(mouseEvent));
    }

    @Override
    public void focusChanged(FocusEvent focusEvent) {
        EventUtil.call(children.get(), child -> child.focusChanged(focusEvent));
    }

    @Override
    public void renderLayer(RenderEvent renderEvent) {
        EventUtil.call(children.get(), child -> child.renderLayer(renderEvent));
    }

    @Override
    public ActionResult keyDown(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(children.get(), child -> child.keyDown(keyboardEvent));
    }

    @Override
    public ActionResult keyUp(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(children.get(), child -> child.keyUp(keyboardEvent));
    }

    @Override
    public ActionResult characterTyped(KeyboardEvent keyboardEvent) {
        return EventUtil.handle(children.get(), child -> child.characterTyped(keyboardEvent));
    }
}
