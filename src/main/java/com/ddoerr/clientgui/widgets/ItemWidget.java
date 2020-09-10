package com.ddoerr.clientgui.widgets;

import com.ddoerr.clientgui.models.Point;
import com.ddoerr.clientgui.models.Size;
import com.ddoerr.clientgui.util.Renderer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemWidget extends Widget {
    private final ObjectProperty<Item> item = new SimpleObjectProperty<>(this, "item", Items.GRASS_BLOCK);

    public ItemWidget() {
        setSize(new Size(16, 16));
    }

    public ObjectProperty<Item> itemProperty() {
        return item;
    }
    public Item getItem() {
        return item.get();
    }
    public void setItem(Item item) {
        this.item.set(item);
    }

    @Override
    public void render(MatrixStack matrixStack, Point mouse) {
        Renderer.renderItem(matrixStack, position.get(), item.get());
    }
}
