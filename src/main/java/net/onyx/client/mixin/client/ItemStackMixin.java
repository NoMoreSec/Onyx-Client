package net.onyx.client.mixin.client;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.onyx.client.OnyxClient;
import net.onyx.client.modules.combat.AntiCrystalBounce;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin({ItemStack.class})
public abstract class ItemStackMixin {

    /**
     * Stops crystals / general items from having an animation when picked up
     */
    @Inject(method = {"getBobbingAnimationTime"}, at = {@At("HEAD")}, cancellable = true)
    private void reducePlaceDelay(CallbackInfoReturnable<Integer> info) {
        if (!AntiCrystalBounce.enabled) return;
        if (OnyxClient.getClient().player == null) return;
        ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
        if (mainHandStack.isOf(Items.END_CRYSTAL)) {
            info.setReturnValue(0);
        }
    }
}