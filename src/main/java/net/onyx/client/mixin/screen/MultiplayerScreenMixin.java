package net.onyx.client.mixin.screen;

import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.network.ServerInfo;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.screen.ConnectEvent;
import net.onyx.client.utils.ServerUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin {

    @Inject(at = @At("HEAD"), method = "connect(Lnet/minecraft/client/network/ServerInfo;)V")
	private void onConnect(ServerInfo entry, CallbackInfo ci) {
        // For the util
        ServerUtils.setLastServer(entry);

        // For the event
		OnyxClient.getInstance().emitter.triggerEvent(new ConnectEvent(entry, ci));
        
	}
}
