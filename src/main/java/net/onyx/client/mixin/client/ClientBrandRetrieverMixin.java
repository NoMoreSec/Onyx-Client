package net.onyx.client.mixin.client;

import net.minecraft.client.ClientBrandRetriever;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.GetClientModNameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBrandRetriever.class)
public class ClientBrandRetrieverMixin {
    @Inject(at = @At("TAIL"), method="getClientModName()Ljava/lang/String;", cancellable = true, remap = false)
    private static void onGetClientModName(CallbackInfoReturnable<String> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetClientModNameEvent(cir));
    }
}
