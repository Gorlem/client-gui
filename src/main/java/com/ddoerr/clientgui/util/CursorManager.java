package com.ddoerr.clientgui.util;

import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class CursorManager {
    private static final long windowHandle = MinecraftClient.getInstance().getWindow().getHandle();

    private static long cursorHandle = -1;

    public static void setCursor(Cursor cursor) {
        if (cursorHandle != -1) {
            GLFW.glfwDestroyCursor(cursorHandle);
        }

        cursorHandle = GLFW.glfwCreateStandardCursor(cursor.cursor);
        GLFW.glfwSetCursor(windowHandle, cursorHandle);
    }

    public static void resetCursor() {
        if (cursorHandle != -1) {
            GLFW.glfwDestroyCursor(cursorHandle);
        }

        cursorHandle = -1;
        GLFW.glfwSetCursor(windowHandle, 0);
    }

    public enum Cursor {
        ARROW(GLFW.GLFW_ARROW_CURSOR),
        IBEAM(GLFW.GLFW_IBEAM_CURSOR),
        CROSSHAIR(GLFW.GLFW_CROSSHAIR_CURSOR),
        HAND(GLFW.GLFW_HAND_CURSOR),
        HRESIZE(GLFW.GLFW_HRESIZE_CURSOR),
        VRESIZE(GLFW.GLFW_VRESIZE_CURSOR);

        private final int cursor;

        Cursor(int cursor) {
            this.cursor = cursor;
        }
    }
}
