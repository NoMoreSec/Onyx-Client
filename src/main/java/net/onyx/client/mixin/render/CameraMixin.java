package net.onyx.client.mixin.render;

import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.OnSubmersionTypeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {    
    @Inject(at = @At("RETURN"), method="getSubmersionType()Lnet/minecraft/client/render/CameraSubmersionType;", cancellable = true)
    public void onGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnSubmersionTypeEvent(cir));
    }
}