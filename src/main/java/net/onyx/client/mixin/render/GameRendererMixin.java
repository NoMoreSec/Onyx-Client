package net.onyx.client.mixin.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.BobViewWhenHurtEvent;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.events.render.Render3DEvent;
import net.onyx.client.events.render.RenderWorldViewBobbingEvent;
import net.onyx.client.modules.combat.Reach;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {



    @Inject(at = @At("HEAD"), method="bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
    void onBobViewWhenHurt(MatrixStack mStack, float f, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new BobViewWhenHurtEvent(mStack, f, ci));
    }

    @Inject(
		at = {@At(value = "FIELD",
			target = "Lnet/minecraft/client/render/GameRenderer;renderHand:Z",
			opcode = Opcodes.GETFIELD,
			ordinal = 0)},
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"
        },
        cancellable = true
    )
    private void onRender(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnRenderEvent(tickDelta, limitTime, matrix, ci));
    }

    @Redirect(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/GameRenderer;bobView(Lnet/minecraft/client/util/math/MatrixStack;F)V",
            ordinal = 0
        ),
		method = {
			"renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V"
        }
    )
	private void onRenderWorldViewBobbing(GameRenderer gameRenderer, MatrixStack matrixStack, float partalTicks) {
        RenderWorldViewBobbingEvent event = new RenderWorldViewBobbingEvent(gameRenderer, matrixStack, partalTicks);

        OnyxClient.getInstance().emitter.triggerEvent(event);

        if (event.cancel) {
            return;
        }

		bobView(matrixStack, partalTicks);
	}









    @Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info, boolean bl, Camera camera, MatrixStack matrixStack, double d, Matrix4f matrix4f) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc == null || mc.world == null || mc.player == null) return;

        Render3DEvent event = new Render3DEvent(matrices, tickDelta, camera.getPos().x, camera.getPos().y, camera.getPos().z);
        OnyxClient.getInstance().emitter.triggerEvent(event);

    }
    @ModifyConstant(method = {"updateTargetedEntity"}, constant = {@Constant(doubleValue = 9.0)})
    private double updateTargetedEntityModifySquaredMaxReach(double d) {
        return ((Reach) OnyxClient.getInstance().getModules().get("reach")).entityReach() * ((Reach) OnyxClient.getInstance().getModules().get("reach")).entityReach();
    }

    @Shadow
    protected abstract void bobView(MatrixStack matrixStack, float partalTicks);
}