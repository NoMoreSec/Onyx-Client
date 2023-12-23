package net.onyx.client.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.DisconnectEvent;
import net.onyx.client.events.client.HandleDisconnectionEvent;
import net.onyx.client.events.packet.ReceivePacketEvent;
import net.onyx.client.modules.combat.MarlowOptimizer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(at = @At("HEAD"), method = "handleDisconnection()V", cancellable = true)
    public void handleDisconnection(CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new DisconnectEvent(ci));
        OnyxClient.getInstance().emitter.triggerEvent(new HandleDisconnectionEvent(ci));
    }

    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        ReceivePacketEvent event = new ReceivePacketEvent(packet);
        if (event.cancel)
            info.cancel();
    }


    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onPacketSend(Packet<?> packet, CallbackInfo ci) {
        if (!MarlowOptimizer.enabled)
            return;
        final MinecraftClient mc = MinecraftClient.getInstance();
            if (packet instanceof PlayerInteractEntityC2SPacket interactPacket) {
                interactPacket.handle(new PlayerInteractEntityC2SPacket.Handler() {
                    public void interact(Hand hand) {
                    }

                    public void interactAt(Hand hand, Vec3d pos) {
                    }

                    public void attack() {
                        HitResult hitResult = mc.crosshairTarget;
                        if (hitResult != null) {
                            if (hitResult.getType() == HitResult.Type.ENTITY) {
                                EntityHitResult entityHitResult = (EntityHitResult) hitResult;
                                Entity entity = entityHitResult.getEntity();
                                if (entity instanceof EndCrystalEntity) {
                                    StatusEffectInstance weakness = mc.player.getStatusEffect(StatusEffects.WEAKNESS);
                                    StatusEffectInstance strength = mc.player.getStatusEffect(StatusEffects.STRENGTH);
                                    if (weakness != null && (strength == null || strength.getAmplifier() <= weakness.getAmplifier()) && !isTool(mc.player.getMainHandStack())) {
                                        return;
                                    }

                                    entity.kill();
                                    entity.setRemoved(Entity.RemovalReason.KILLED);
                                    entity.onRemoved();
                                }
                            }

                        }
                    }
                });
            }
        }


    private boolean isTool(ItemStack itemStack) {
        if (itemStack.getItem() instanceof ToolItem && !(itemStack.getItem() instanceof HoeItem)) {
            ToolMaterial material = ((ToolItem)itemStack.getItem()).getMaterial();
            return material == ToolMaterials.DIAMOND || material == ToolMaterials.NETHERITE;
        } else {
            return false;
        }
    }
}
