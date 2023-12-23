package net.onyx.client.mixin.client;

import net.minecraft.client.Keyboard;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.io.OnKeyEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public class KeyboardMixin {
    @Inject(at = @At("HEAD"), method="onKey(JIIII)V", cancellable = true)
    private void onKey(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnKeyEvent(window, key, scancode, action, modifiers, ci));
    }
}
