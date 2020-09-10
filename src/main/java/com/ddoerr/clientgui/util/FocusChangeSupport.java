package com.ddoerr.clientgui.util;

import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.widgets.layout.ContainerWidget;
import com.ddoerr.clientgui.widgets.layout.DelegateWidget;
import com.ddoerr.clientgui.widgets.Widget;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class FocusChangeSupport {
    private final Widget widget;

    public FocusChangeSupport(Widget widget) {
        this.widget = widget;
    }

    public void changeFocus(Direction direction) {
        Stack<Widget> widgets = new Stack<>();
        widgets.push(widget);
        Widget current = widget;
        boolean foundFocused = false;

        while (!widgets.empty()) {
            if (foundFocused && current.isFocusable() && current.isEnabled()) {
                widget.focusChanged(new FocusEvent(current));
                return;
            }

            if (current.isFocused()) {
                foundFocused = true;
            }

            if (current.isEnabled()) {
                if (current instanceof DelegateWidget) {
                    widgets.add(((DelegateWidget) current).getChild());
                } else if (current instanceof ContainerWidget<?>) {
                    List<Widget> list = ((ContainerWidget<?>) current).getChildren().stream().map(ContainerWidget.WidgetWithData::getWidget).collect(Collectors.toList());
                    widgets.addAll(Direction.Backwards == direction ? Lists.reverse(list) : list);
                }
            }

            current = widgets.pop();

            if (foundFocused && widgets.empty()) {
                widgets.push(current);
            }
        }
    }

    public enum Direction {
        Forwards,
        Backwards
    }
}
