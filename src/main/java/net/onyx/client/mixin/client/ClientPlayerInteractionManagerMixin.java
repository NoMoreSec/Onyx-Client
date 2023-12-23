package net.onyx.client.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.onyx.client.Global;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.InteractBlockEvent;
import net.onyx.client.events.client.OnAttackEntityEvent;
import net.onyx.client.events.client.StopUsingItemEvent;
import net.onyx.client.events.client.UpdateBlockBreakingProgressEvent;
import net.onyx.client.modules.combat.Reach;
import net.onyx.client.modules.utilities.NoBreakDelay;
import net.onyx.client.onyxevent.events.AttackEntityListener;
import net.onyx.client.utils.MixinUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class ClientPlayerInteractionManagerMixin implements Global {

    @Shadow private int blockBreakingCooldown;

    @Inject(method = "attackBlock", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I"))
    public void attackBlock(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        int cd = getModule(NoBreakDelay.class).getIntSetting("Break Delay");
        blockBreakingCooldown = blockBreakingCooldown > cd ? cd : 0;
    }

    @Inject(method = "updateBlockBreakingProgress", at = @At(value = "FIELD", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I"))
    public void updateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        int cd = getModule(NoBreakDelay.class).getIntSetting("Break Delay");
        blockBreakingCooldown = blockBreakingCooldown > cd ? cd : 0;
    }

    @Inject(method = {"getReachDistance"}, at = {@At("HEAD")}, cancellable = true)
    private void onGetReachDistance(CallbackInfoReturnable<Float> info) {
        info.setReturnValue(((Reach) OnyxClient.getInstance().getModules().get("reach")).blockReach());
    }

    @Inject(at = @At("HEAD"), method = "attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V", cancellable = true)
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new OnAttackEntityEvent(player, target, ci));
        MixinUtils.fireCancellable(new AttackEntityListener.AttackEntityEvent(player, target), ci);
    }

    @Inject(at = @At("HEAD"), method = "stopUsingItem(Lnet/minecraft/entity/player/PlayerEntity;)V", cancellable = true)
    private void onStopUsingItem(PlayerEntity playerEntity, CallbackInfo ci) {
        OnyxClient.getInstance().emitter.triggerEvent(new StopUsingItemEvent(playerEntity, ci));
    }

    @Inject(at = @At("HEAD"), method = "updateBlockBreakingProgress(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/Direction;)Z", cancellable = true)
    private void onUpdateBlockBreakingProgress(BlockPos pos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new UpdateBlockBreakingProgressEvent(pos, direction, cir));
    }


    @Inject(method = "interactBlock", at = @At("HEAD"), cancellable = true)
    public void interactBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir) {
        InteractBlockEvent event = new InteractBlockEvent(player.getMainHandStack().isEmpty() ? Hand.OFF_HAND : hand, hitResult);
        if (event.cancel)
            cir.setReturnValue(ActionResult.FAIL);
        OnyxClient.getInstance().emitter.triggerEvent(event);
    }
}
