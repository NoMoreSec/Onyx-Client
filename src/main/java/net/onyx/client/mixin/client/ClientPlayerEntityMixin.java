package net.onyx.client.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.*;
import net.onyx.client.events.packet.PostMovementPacketEvent;
import net.onyx.client.events.packet.PreMovementPacketEvent;
import net.onyx.client.gui.impl.ClickGUIScreen;
import net.onyx.client.onyxevent.EventManager;
import net.onyx.client.onyxevent.events.PlayerTickListener;
import net.onyx.client.utils.RotationFaker;
import net.onyx.client.utils.RotationUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile, PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(at = @At("HEAD"), method="move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V")
    private void onMove(MovementType type, Vec3d offset, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new PlayerMoveEvent(type, offset, ci));
    }

    @Inject(at = @At("HEAD"), method="sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", cancellable = true)
    private void onSendChatMessage(String message, Text preview, CallbackInfo ci) {
        // Handle commands etc.
        OnyxClient.getInstance().processChatPost(message, ci);

        OnyxClient.getInstance().emitter.triggerEvent(new PlayerChatEvent(message, ci));
    }

    @Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
    private void onPushOutOfBlocks(double x, double d, CallbackInfo info) {
        PushOutOfBlockEvent event = new PushOutOfBlockEvent(x, d);
        OnyxClient.getInstance().emitter.triggerEvent(event);
        if(event.cancel) info.cancel();
    }


    @Inject(at = @At("HEAD"), method="sendMovementPackets()V", cancellable = true)
    private void beforeSendMovementPackets(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new PreMovementPacketEvent(ci));
    }

    @Inject(at = @At("TAIL"), method="sendMovementPackets()V", cancellable = false)
    private void afterSendMovementPackets(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new PostMovementPacketEvent(ci));
    }

    @Inject(at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V",
            ordinal = 0), method = "tick()V")
    private void onTick(CallbackInfo ci) {
        EventManager.fire(new PlayerTickListener.PlayerTickEvent());
        OnyxClient.getInstance().emitter.triggerEvent(new ClientTickEvent(ci));
        ClickGUIScreen.onTick();
        RotationUtils.tick();
    }

    @Inject(at = @At("HEAD"), method="tickMovement()V", cancellable = false)
    private void onTickMovement(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new ClientTickMovementEvent(ci));
    }

    @Inject(at = {@At("HEAD")}, method = {"sendMovementPackets()V"})
    private void onSendMovementPacketsHEAD(CallbackInfo ci)
    {
        RotationFaker.onPreMotion();
    }

    @Inject(at = {@At("TAIL")}, method = {"sendMovementPackets()V"})
    private void onSendMovementPacketsTAIL(CallbackInfo ci)
    {
        RotationFaker.onPostMotion();
    }
}