package com.ddoerr.clientgui.widgets.layout;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.widgets.Widget;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

public class CardWidget extends Widget<CardWidget> {
    protected final ContainerAttachment containerAttachment = new ContainerAttachment(this, focusListeners.fire());

    protected final IntegerProperty selectedIndex = new SimpleIntegerProperty(this, "selectedIndex", 0);

    public CardWidget() {
        selectedIndex.addListener((bean, old, now) -> {
            ListProperty<Widget<?>> childrenProperty = containerAttachment.childrenProperty();
            for (int i = 0; i < childrenProperty.size(); i++) {
                Widget<?> widget = childrenProperty.get(i);
                widget.setVisible(i == now.intValue());
            }
        });
        containerAttachment.childrenProperty().addListener((ListChangeListener<? super Widget<?>>) c -> {
            ListProperty<Widget<?>> childrenProperty = containerAttachment.childrenProperty();
            for (int i = 0; i < childrenProperty.size(); i++) {
                Widget<?> widget = childrenProperty.get(i);
                widget.setVisible(i == getSelectedIndex());
            }
        });
        attach(containerAttachment);
    }

    public IntegerProperty selectedIndexProperty() {
        return selectedIndex;
    }
    public CardWidget setSelectedIndex(int selectedIndex) {
        this.selectedIndex.set(selectedIndex);
        return this;
    }
    public int getSelectedIndex() {
        return selectedIndex.get();
    }

    public ListProperty<Widget<?>> childrenProperty() {
        return containerAttachment.childrenProperty();
    }

    public CardWidget addChild(Widget<?> child) {
        containerAttachment.addChild(child, null, true);
        return this;
    }
}
