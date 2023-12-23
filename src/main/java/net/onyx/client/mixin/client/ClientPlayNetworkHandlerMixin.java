package net.onyx.client.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.*;
import net.minecraft.text.Text;
import net.minecraft.world.chunk.WorldChunk;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.ChunkDataEvent;
import net.onyx.client.events.client.OnDisconnectedEvent;
import net.onyx.client.events.packet.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Inject(at = @At("HEAD"), method="sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
    public void onSendPacket(Packet<?> packet, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new SendPacketEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onWorldTimeUpdate(Lnet/minecraft/network/packet/s2c/play/WorldTimeUpdateS2CPacket;)V", cancellable=true)
    public void onWorldTimeUpdate(WorldTimeUpdateS2CPacket packet, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnWorldTimeUpdateEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onEntityStatus(Lnet/minecraft/network/packet/s2c/play/EntityStatusS2CPacket;)V", cancellable = true)
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnEntityStatusEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onGameStateChange(Lnet/minecraft/network/packet/s2c/play/GameStateChangeS2CPacket;)V", cancellable = true)
    public void onGameStateChange(GameStateChangeS2CPacket packet, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnGameStateChangeEvent(packet, ci));
    }

    @Inject(method = "onChunkData", at = @At("TAIL"))
    private void onChunkData(ChunkDataS2CPacket packet, CallbackInfo info) {
        WorldChunk chunk = OnyxClient.getClient().world.getChunk(packet.getX(), packet.getZ());
        OnyxClient.getInstance().emitter.triggerEvent(new ChunkDataEvent(chunk));
    }

    @Inject(at = @At("HEAD"), method="onResourcePackSend(Lnet/minecraft/network/packet/s2c/play/ResourcePackSendS2CPacket;)V", cancellable = true)
    public void onResourcePackSend(ResourcePackSendS2CPacket packet, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnResourcePackSendEvent(packet, ci));
    }

    @Inject(at = @At("HEAD"), method="onDisconnected(Lnet/minecraft/text/Text;)V", cancellable = true)
    public void onDisconnected(Text reason, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnDisconnectedEvent(reason, ci));
    }
}
