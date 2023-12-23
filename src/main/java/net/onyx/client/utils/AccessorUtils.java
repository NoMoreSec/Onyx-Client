package net.onyx.client.utils;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.onyx.client.mixin.client.CursorSlotMixin;

import javax.annotation.Nullable;

public class AccessorUtils {
    @Nullable
    public static Slot getSlotUnderMouse(HandledScreen<?> gui) {
        return ((CursorSlotMixin)gui).itemscroller_getHoveredSlot();
    }
}
