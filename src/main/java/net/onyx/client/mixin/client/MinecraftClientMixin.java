package net.onyx.client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.DisconnectEvent;
import net.onyx.client.events.client.OnClientCloseEvent;
import net.onyx.client.interfaces.mixin.IClient;
import net.onyx.client.modules.combat.MarlowOptimizer;
import net.onyx.client.onyxevent.EventManager;
import net.onyx.client.onyxevent.events.ItemUseListener;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin implements IClient {
    @Inject(at = @At("HEAD"), method = "close()V", cancellable = false)
    private void onClose(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnClientCloseEvent(ci));

        // Close our client after all is said and done.
        OnyxClient.getInstance().close();
    }

    @Shadow
    @Final
    FontManager fontManager;

    @Override
    public FontManager getFontManager() {
        return fontManager;
    }

    @Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V", cancellable = true)
    private void onDisconnect(Screen screen, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new DisconnectEvent(ci));
    }



    @Inject(
            method = {"tick"},
            at = {@At("HEAD")}
    )
    private void onPreTick(CallbackInfo info) {
        if (!MarlowOptimizer.enabled)
            return;
        Iterator<Map.Entry<Entity, Integer>> iterator = OnyxClient.TO_KILL.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Entity, Integer> entry = iterator.next();
                Entity entity = entry.getKey();
                int delay = entry.getValue() - 1;
                if (delay == 0) {
                    iterator.remove();
                    if (!entity.isAlive()) {
                        entity.kill();
                        entity.setRemoved(Entity.RemovalReason.KILLED);
                        entity.onRemoved();
                    }
                } else {
                    entry.setValue(delay);
                }
            }

        }


    @Inject(at = @At("HEAD"), method = "doItemUse", cancellable = true)
    private void onDoItemUse(CallbackInfo ci) {
        ItemUseListener.ItemUseEvent event = new ItemUseListener.ItemUseEvent();
        EventManager.fire(event);
        if (event.isCancelled())
            ci.cancel();
    }

    @Shadow protected abstract void doItemUse();

    public void performItemUse() {
        this.doItemUse();
    }

    @Shadow protected abstract boolean doAttack();

    @Override
    public boolean performAttack() {
        return this.doAttack();
    }


    @Shadow protected int attackCooldown;
    @Override
    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
    }
}
