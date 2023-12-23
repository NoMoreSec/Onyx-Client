package net.onyx.client.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.util.Identifier;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.GetCapeTextureEvent;
import net.onyx.client.events.render.GetModelEvent;
import net.onyx.client.events.render.GetSkinTextureEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {
    @Inject(at = @At("RETURN"), method = "getSkinTexture()Lnet/minecraft/util/Identifier;", cancellable = true)
    private void onGetSkinTexture(CallbackInfoReturnable<Identifier> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetSkinTextureEvent((AbstractClientPlayerEntity)(Object)(this), cir));
    }

    @Inject(at = @At("RETURN"), method = "getModel()Ljava/lang/String;", cancellable = true)
    private void onGetModel(CallbackInfoReturnable<String> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetModelEvent((AbstractClientPlayerEntity)(Object)(this), cir));
    }

    @Inject(method = "getCapeTexture", at = @At("HEAD"), cancellable = true)
    private void onGetCapeTexture(CallbackInfoReturnable<Identifier> cir){
        OnyxClient.getInstance().emitter.triggerEvent(new GetCapeTextureEvent((AbstractClientPlayerEntity)(Object)(this), cir));
    }
}
