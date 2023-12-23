package net.onyx.client.mixin.packet;

import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.onyx.client.Global;
import net.onyx.client.modules.packet.AntiKnockback;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityVelocityUpdateS2CPacket.class)
public abstract class EntityVelocityUpdateS2CPacketMixin implements Global {

    @Inject(method = "getVelocityX", at = @At("RETURN"), cancellable = true)
    public void getVelX(CallbackInfoReturnable<Integer> cir) {
        if (wc.isEnabled(AntiKnockback.class)) {
            double mu =  wc.getModule(AntiKnockback.class).getDoubleSetting("Multiplier X");
            cir.setReturnValue((int)(cir.getReturnValue() * mu));
        }
    }

    @Inject(method = "getVelocityY", at = @At("RETURN"), cancellable = true)
    public void getVelY(CallbackInfoReturnable<Integer> cir) {
        if (wc.isEnabled(AntiKnockback.class)) {
            double mu =  wc.getModule(AntiKnockback.class).getDoubleSetting("Multiplier Y");
            cir.setReturnValue((int)(cir.getReturnValue() * mu));
        }
    }

    @Inject(method = "getVelocityZ", at = @At("RETURN"), cancellable = true)
    public void getVelZ(CallbackInfoReturnable<Integer> cir) {
        if (wc.isEnabled(AntiKnockback.class)) {
            double mu =  wc.getModule(AntiKnockback.class).getDoubleSetting("Multiplier Z");
            cir.setReturnValue((int)(cir.getReturnValue() * mu));
        }
    }
}
