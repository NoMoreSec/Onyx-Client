package net.onyx.client.mixin.render;

import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WorldRenderer.class)
public interface WorldRendererAccessor {

    @Accessor
    Framebuffer getEntityOutlinesFramebuffer();

    @Accessor
    void setEntityOutlinesFramebuffer(Framebuffer framebuffer);

    @Accessor
    ShaderEffect getEntityOutlineShader();

    @Accessor
    void setEntityOutlineShader(ShaderEffect shaderEffect);

    @Accessor
    Frustum getFrustum();

    @Accessor
    void setFrustum(Frustum frustum);
}
