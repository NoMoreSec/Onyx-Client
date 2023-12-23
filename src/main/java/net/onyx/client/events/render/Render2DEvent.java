package net.onyx.client.events.render;

import net.onyx.client.events.Event;
import net.onyx.client.utils.ClientUtils;

public class Render2DEvent extends Event {

    public int screenWidth, screenHeight;
    public double frameTime;
    public float tickDelta;
    public Render2DEvent(int screenWidth, int screenHeight, float tickDelta) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        frameTime = ClientUtils.frameTime;
        this.tickDelta = tickDelta;
    }
}
