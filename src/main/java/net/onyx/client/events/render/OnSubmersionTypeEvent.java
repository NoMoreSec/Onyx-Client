package net.onyx.client.events.render;

import net.minecraft.client.render.CameraSubmersionType;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class OnSubmersionTypeEvent extends Event {
    public CallbackInfoReturnable<CameraSubmersionType> cir;

    public OnSubmersionTypeEvent(CallbackInfoReturnable<CameraSubmersionType> cir) {
        this.cir = cir;
    }
}
