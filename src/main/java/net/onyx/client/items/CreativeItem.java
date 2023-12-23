package net.onyx.client.items;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public interface CreativeItem {
    ItemStack getStack();
    default String getName() {
        return this.getClass().getName();
    }
    default boolean useName() {
        return true;
    }
    default ItemStack readyStack() {
        ItemStack stack = this.getStack();
        if (this.useName()) stack.setCustomName(Text.of(this.getName()));

        return stack;
    }
    default Boolean isLaggy() {
        return false;
    }
}
