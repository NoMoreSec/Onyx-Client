package net.onyx.client.events.screen;

import net.minecraft.client.network.ServerInfo;
import net.onyx.client.events.Event;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class ConnectEvent extends Event {
    public ServerInfo entry;
    public CallbackInfo ci;
    
    public ConnectEvent(ServerInfo entry, CallbackInfo ci) {
        this.entry = entry;
        this.ci = ci;
    }
}
