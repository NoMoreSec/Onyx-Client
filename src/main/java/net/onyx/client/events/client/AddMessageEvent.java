package net.onyx.client.events.client;

import net.minecraft.text.Text;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class AddMessageEvent extends Event {
    public Text chatText;
    public CallbackInfo ci;

    public AddMessageEvent(Text chatText, CallbackInfo ci) {
        this.chatText = chatText;
        this.ci = ci;
    }
}
