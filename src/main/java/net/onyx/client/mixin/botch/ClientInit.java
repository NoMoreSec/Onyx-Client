package net.onyx.client.mixin.botch;

import net.minecraft.client.MinecraftClient;
import net.onyx.client.OnyxClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ClientInit {

    @Inject(at = @At("TAIL"), method = "<init>", cancellable = false)
    public void init(CallbackInfo ci) {
        OnyxClient.getInstance().initialize();
    }
}
