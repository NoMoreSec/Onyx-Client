package net.onyx.client.mixin.client;

import net.minecraft.client.Mouse;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.io.OnMouseButtonEvent;
import net.onyx.client.interfaces.mixin.IMouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin implements IMouse {
    @Inject(at = @At("HEAD"), method="onMouseButton", cancellable = true)
    private void onMouseButton(long window, int button, int action, int mods, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnMouseButtonEvent(window, button, action, mods, ci));
    }


    @Shadow
    private void onMouseButton(long window, int button, int action, int mods) {}

    public void cwOnMouseButton(long window, int button, int action, int mods) {
        onMouseButton(window, button, action, mods);
    }
}
