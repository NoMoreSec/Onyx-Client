package net.onyx.client.mixin.render;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.AddMessageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

	private final boolean shouldClear = false;

	@Inject(at = @At("HEAD"), method = "addMessage(Lnet/minecraft/text/Text;)V", cancellable = true)
	private void onAddMessage(Text chatText, CallbackInfo ci) {
		OnyxClient.getInstance().emitter.triggerEvent(new AddMessageEvent(chatText, ci));
	}
}