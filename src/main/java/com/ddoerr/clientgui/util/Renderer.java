package com.ddoerr.clientgui.util;

import com.ddoerr.clientgui.models.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class Renderer {
    private static final MinecraftClient minecraft = MinecraftClient.getInstance();

    public static void renderRectangle(MatrixStack matrices, Rectangle rectangle, Color color) {
        DrawableHelper.fill(matrices,
                (int) rectangle.getTopLeftPoint().getX(),
                (int) rectangle.getTopLeftPoint().getY(),
                (int) rectangle.getBottomRightPoint().getX(),
                (int) rectangle.getBottomRightPoint().getY(),
                color.toMinecraftColor());
    }

    public static void renderText(MatrixStack matrices, OrderedText text, Point position, Color color) {
        minecraft.textRenderer.drawWithShadow(matrices, text, (int) position.getX(), (int) position.getY(), color.toMinecraftColor());
    }

    public static void renderCenteredText(MatrixStack matrices, OrderedText text, Rectangle rectangle, Color color) {
        int textWidth = minecraft.textRenderer.getWidth(text);
        int textHeight = minecraft.textRenderer.fontHeight;
        renderText(matrices, text, rectangle.getCenterPoint().add(textWidth / 2 * -1, textHeight / 2 * -1), color);
    }

    public static void renderHorizontalLine(MatrixStack matrices, Point from, int width, Color color) {
        renderRectangle(matrices, Rectangle.of(from, Size.of(width, 1)), color);
    }

    public static void renderVerticalLine(MatrixStack matrices, Point from, int height, Color color) {
        renderRectangle(matrices, Rectangle.of(from, Size.of(1, height)), color);
    }

    public static void renderBorder(MatrixStack matrices, Rectangle rectangle, Color color) {
        renderBorder(matrices, rectangle, Insets.of(1), color);
    }

    public static void renderBorder(MatrixStack matrixStack, Rectangle rectangle, Insets border, Color color) {
        renderRectangle(matrixStack,
                Rectangle.of(rectangle.getTopLeftPoint(),
                        Size.of(rectangle.getWidth() - border.getRight(), border.getTop())),
                color);
        renderRectangle(matrixStack,
                Rectangle.of(rectangle.getBottomLeftPoint().add(border.getLeft(), -1 * border.getBottom()),
                        Size.of(rectangle.getWidth() - border.getLeft(), border.getBottom())),
                color);
        renderRectangle(matrixStack,
                Rectangle.of(rectangle.getTopLeftPoint().add(0, border.getTop()),
                        Size.of(border.getLeft(), rectangle.getHeight() - border.getTop())),
                color);
        renderRectangle(matrixStack,
                Rectangle.of(rectangle.getTopRightPoint().add(-1 * border.getRight(), 0),
                        Size.of(border.getRight(), rectangle.getHeight() - border.getBottom())),
                color);
    }

    public static void renderItem(MatrixStack matrices, Point position, Item item) {
        minecraft.getItemRenderer().renderInGui(new ItemStack(item), (int) position.getX(), (int) position.getY());
    }

    public static int textWidth(String text) {
        return minecraft.textRenderer.getWidth(text);
    }

    public static int textHeight() {
        return minecraft.textRenderer.fontHeight;
    }

    public static void renderTooltip(MatrixStack matrixStack, Point position, List<OrderedText> textLines) {

        minecraft.currentScreen.renderOrderedTooltip(matrixStack, textLines, (int) position.getX(), (int) position.getY());
    }

    public static void enableScissor(Rectangle area) {
        double scaleFactor = minecraft.getWindow().getScaleFactor();

        Point point = area.getBottomLeftPoint();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(
                (int) (point.getX() * scaleFactor),
                (int) ((minecraft.getWindow().getScaledHeight() - point.getY()) * scaleFactor),
                (int) (area.getWidth() * scaleFactor),
                (int) (area.getHeight() * scaleFactor));
    }

    public static void disableScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }
}
