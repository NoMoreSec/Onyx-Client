package net.onyx.client.mixin.screen;

import net.minecraft.client.gui.screen.DeathScreen;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.screen.DeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DeathScreen.class)
public class DeathScreenMixin {
    @Inject(at = @At("TAIL"), method = "init()V", cancellable = false)
    public void init(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new DeathEvent(ci));
    }
}
