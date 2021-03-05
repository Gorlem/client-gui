package com.ddoerr.clientgui.util;

import com.ddoerr.clientgui.widgets.Widget;
import net.minecraft.util.ActionResult;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventUtil {
    public static <T> ActionResult handle(List<T> listeners, Function<T, ActionResult> proxy) {
        for (T listener : listeners) {
            if (listener instanceof Widget<?>) {
                if (!((Widget<?>) listener).isVisible()) {
                    continue;
                }
            }

            ActionResult result = proxy.apply(listener);

            if (result != ActionResult.PASS) {
                return result;
            }
        }

        return ActionResult.PASS;
    }

    public static <T> void call(List<T> listeners, Consumer<T> proxy) {
        for (T listener : listeners) {
            if (proxy == null) {
                continue;
            }
            proxy.accept(listener);
        }
    }
}
