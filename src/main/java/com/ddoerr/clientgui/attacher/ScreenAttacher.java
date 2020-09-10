package com.ddoerr.clientgui.attacher;

import com.ddoerr.clientgui.views.AbstractView;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.Window;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ScreenAttacher {
    private static final List<ScreenHolder> SCREEN_MAPPINGS = new ArrayList<>();
    private static final Map<Screen, Screen> SCREEN_CACHE = new WeakHashMap<>();

    private static final List<Screen> HUD_SCREENS = new ArrayList<>();
    private static final List<Supplier<Screen>> NOT_INITIALIZED_VIEWS = new ArrayList<>();

    static {
        ClientLifecycleEvents.CLIENT_STARTED.register(minecraft -> {
            for (Supplier<Screen> supplier : NOT_INITIALIZED_VIEWS) {
                Screen proxy = supplier.get();
                proxy.init(minecraft, minecraft.getWindow().getScaledWidth(), minecraft.getWindow().getScaledHeight());
                HUD_SCREENS.add(proxy);
            }
            NOT_INITIALIZED_VIEWS.clear();
        });

        HudRenderCallback.EVENT.register((matrixStack, delta) -> {
            for (Screen screen : HUD_SCREENS) {
                screen.render(matrixStack, -1, -1, delta);
            }
        });
    }

    public static void attachToScreen(Class<? extends Screen> screenClass, Supplier<Screen> supplier) {
        attachToScreen(screenClass::isInstance, supplier);
    }

    public static void attachToScreen(Predicate<Screen> predicate, Supplier<Screen> supplier) {
        SCREEN_MAPPINGS.add(new ScreenHolder(predicate, supplier));
    }

    public static void attachToHud(Supplier<Screen> supplier) {
        Window window = MinecraftClient.getInstance().getWindow();

        if (window == null) {
            NOT_INITIALIZED_VIEWS.add(supplier);
        } else {
            Screen proxy = supplier.get();
            proxy.init(MinecraftClient.getInstance(), window.getScaledWidth(), window.getScaledHeight());
            HUD_SCREENS.add(proxy);
        }
    }

    public static Screen getProxyForScreen(Screen screen) {
        if (screen == null) {
            return null;
        }

        Screen cached = SCREEN_CACHE.get(screen);

        if (cached != null) {
            return cached;
        }

        for (ScreenHolder screenHolder : SCREEN_MAPPINGS) {
            if (screenHolder.predicate.test(screen)) {
                Screen proxy = screenHolder.supplier.get();
                SCREEN_CACHE.put(screen, proxy);
                return proxy;
            }
        }

        return null;
    }

    public static void removeProxyFromCache(Screen screen) {
        //SCREEN_CACHE.remove(screen);
    }

    private static class ScreenHolder {
        private final Predicate<Screen> predicate;
        private final Supplier<Screen> supplier;

        public ScreenHolder(Predicate<Screen> predicate, Supplier<Screen> supplier) {
            this.predicate = predicate;
            this.supplier = supplier;
        }
    }
}
