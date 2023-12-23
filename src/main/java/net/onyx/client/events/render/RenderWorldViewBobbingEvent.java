package net.onyx.client.events.render;

import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.onyx.client.events.Event;

public class RenderWorldViewBobbingEvent extends Event {
    public GameRenderer gameRenderer;
    public MatrixStack matrixStack;
    public float partalTicks;
    public boolean cancel = false;

    public RenderWorldViewBobbingEvent(GameRenderer gameRenderer, MatrixStack matrixStack, float partalTicks) {
        this.gameRenderer = gameRenderer;
        this.matrixStack = matrixStack;
        this.partalTicks = partalTicks;
    }
}
