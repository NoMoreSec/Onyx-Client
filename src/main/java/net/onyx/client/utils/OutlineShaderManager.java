package net.onyx.client.utils;

import net.minecraft.client.gl.ShaderEffect;
import net.onyx.client.OnyxClient;
import net.onyx.client.mixin.render.WorldRendererAccessor;

public class OutlineShaderManager {

    public static void loadShader(ShaderEffect shader) {
        if (getCurrentShader() != null) {
            getCurrentShader().close();
        }

        ((WorldRendererAccessor) OnyxClient.getClient().worldRenderer).setEntityOutlineShader(shader);
        ((WorldRendererAccessor) OnyxClient.getClient().worldRenderer).setEntityOutlinesFramebuffer(shader.getSecondaryTarget("final"));
    }

    public static void loadDefaultShader() {
        OnyxClient.getClient().worldRenderer.loadEntityOutlineShader();
    }

    public static ShaderEffect getCurrentShader() {
        return ((WorldRendererAccessor) OnyxClient.getClient().worldRenderer).getEntityOutlineShader();
    }
}
