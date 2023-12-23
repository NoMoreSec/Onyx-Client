package net.onyx.client.mixin.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.JumpEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Inject(at = @At("HEAD"), method="jump()V", cancellable = true)
    private void onJump(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new JumpEvent(ci));
    }
}
