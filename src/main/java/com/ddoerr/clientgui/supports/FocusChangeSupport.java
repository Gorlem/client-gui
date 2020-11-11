package com.ddoerr.clientgui.supports;

import com.ddoerr.clientgui.attachments.ContainerAttachment;
import com.ddoerr.clientgui.events.FocusEvent;
import com.ddoerr.clientgui.widgets.Widget;
import com.google.common.collect.Lists;
import net.minecraft.util.ActionResult;
import org.apache.logging.log4j.LogManager;

import java.util.List;

public class FocusChangeSupport {
    private final Widget<?> widget;

    public FocusChangeSupport(Widget<?> widget) {
        this.widget = widget;
    }

    public ActionResult changeFocus(Direction direction) {
        State state = new State();
        Widget<?> result = scan(widget, direction, state);

        if (result == null) {
            state.found = true;
            result = scan(widget, direction, state);
        }

        if (result != null) {
            widget.focusChanged(new FocusEvent(result));
            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private Widget<?> scan(Widget<?> widget, Direction direction, State state) {
        List<ContainerAttachment> containers = widget.findAllAttachments(ContainerAttachment.class);

        if (direction == Direction.Backwards) {
            containers = Lists.reverse(containers);
        }

        for (ContainerAttachment container : containers) {
            List<Widget<?>> children = container.getChildren();

            if (direction == Direction.Backwards) {
                children = Lists.reverse(children);
            }

            for (Widget<?> child : children) {
                if (state.found && !child.isVisible()) {
                    continue;
                }

                Widget<?> result = scan(child, direction, state);

                if (result != null) {
                    return result;
                }

                if (child.isFocusable() && state.found) {
                    return child;
                }

                if (child.isFocused()) {
                    state.found = true;
                }
            }
        }

        return null;
    }

    public enum Direction {
        Forwards,
        Backwards
    }

    class State {
        private boolean found = false;
    }
}
