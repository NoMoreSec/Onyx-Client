package net.onyx.client.mixin.entity;

import net.minecraft.entity.LivingEntity;
import net.onyx.client.Global;
import net.onyx.client.modules.render.SlowHandSwing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements Global {

    @Inject(method = "getHandSwingDuration", at = @At("RETURN"), cancellable = true)
    public void getHandSwingDuration(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(wc.isEnabled(SlowHandSwing.class) ? 12 : cir.getReturnValue());
    }
}
