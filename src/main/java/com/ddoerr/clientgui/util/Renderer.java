package com.ddoerr.clientgui.util;

import com.ddoerr.clientgui.models.Color;
import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Rectangle;
import com.ddoerr.clientgui.models.Size;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class Renderer {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void renderRectangle(MatrixStack matrices, Rectangle rectangle, Color color) {
        DrawableHelper.fill(matrices,
                rectangle.getTopLeftPoint().getX(),
                rectangle.getTopLeftPoint().getY(),
                rectangle.getBottomRightPoint().getX(),
                rectangle.getBottomRightPoint().getY(),
                color.toMinecraftColor());
    }

    public static void renderText(MatrixStack matrices, Text text, Point position, Color color) {
        minecraft.textRenderer.drawWithShadow(matrices, text, position.getX(), position.getY(), color.toMinecraftColor());
    }

    public static void renderCenteredText(MatrixStack matrices, Text text, Rectangle rectangle, Color color) {
        int textWidth = minecraft.textRenderer.getWidth(text);
        int textHeight = minecraft.textRenderer.fontHeight;
        renderText(matrices, text, rectangle.getCenterPoint().add(textWidth / 2 * -1, textHeight / 2 * -1), color);
    }

    public static void renderVerticalLine(MatrixStack matrices, Point from, Point to, Color color) {
        renderRectangle(matrices, new Rectangle(from, new Size(to.getX() - from.getX(), 1)), color);
    }

    public static void renderHorizontalLine(MatrixStack matrices, Point from, Point to, Color color) {
        renderRectangle(matrices, new Rectangle(from, new Size(1, to.getY() - from.getY())), color);
    }

    public static void renderBorder(MatrixStack matrices, Rectangle rectangle, Color color) {
        renderVerticalLine(matrices, rectangle.getTopLeftPoint(), rectangle.getTopRightPoint(), color);
        renderVerticalLine(matrices, rectangle.getBottomLeftPoint().add(0, -1), rectangle.getBottomRightPoint().add(0, -1), color);
        renderHorizontalLine(matrices, rectangle.getTopLeftPoint(), rectangle.getBottomLeftPoint(), color);
        renderHorizontalLine(matrices, rectangle.getTopRightPoint().add(-1, 0), rectangle.getBottomRightPoint().add(-1, 0), color);
    }

    public static void renderItem(MatrixStack matrices, Point position, Item item) {
        minecraft.getItemRenderer().renderInGui(new ItemStack(item), position.getX(), position.getY());
    }
}
