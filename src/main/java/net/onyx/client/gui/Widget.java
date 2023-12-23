package net.onyx.client.gui;

import net.minecraft.client.gui.Drawable;

public interface Widget extends Drawable {
    void tick();
    void init();
    void close();
}
