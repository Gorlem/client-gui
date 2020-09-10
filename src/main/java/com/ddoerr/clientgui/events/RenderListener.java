package com.ddoerr.clientgui.events;

import java.util.EventListener;

public interface RenderListener extends EventListener {
    default void renderLayer(RenderEvent renderEvent) {}
}
