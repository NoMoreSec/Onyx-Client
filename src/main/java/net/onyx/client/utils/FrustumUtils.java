package net.onyx.client.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.util.math.Box;
import net.onyx.client.mixin.AccessorWorldRenderer;

public class FrustumUtils {

    public static Frustum getFrustum() {
        return ((AccessorWorldRenderer) MinecraftClient.getInstance().worldRenderer).getFrustum();
    }

    public static boolean isBoxVisible(Box box) {
        return getFrustum().isVisible(box);
    }
}
