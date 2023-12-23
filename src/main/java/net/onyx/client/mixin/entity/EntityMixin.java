package net.onyx.client.mixin.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.render.IsEntityGlowingEvent;
import net.onyx.client.events.render.IsEntityInvisibleEvent;
import net.onyx.client.modules.combat.Hitboxes;
import net.onyx.client.utils.InventoryUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(at = @At("RETURN"), method="isInvisible()Z", cancellable = true)
    public void onIsInvisible(CallbackInfoReturnable<Boolean> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new IsEntityInvisibleEvent(cir));
    }

    @Inject(at = @At("RETURN"), method="isGlowing()Z", cancellable = true)
    public void onIsGlowing(CallbackInfoReturnable<Boolean> cir) {
        Entity ent = (Entity)((Object)(this));

        OnyxClient.getInstance().emitter.triggerEvent(new IsEntityGlowingEvent(ent, cir));
    }

    @Inject(method = "getTargetingMargin", at = @At("HEAD"), cancellable = true)
    public void getTargetingMargin(CallbackInfoReturnable<Float> cir) {
        Entity ent = (Entity)(Object)this;
        OnyxClient client = OnyxClient.getInstance();

        if (client.isEnabled(Hitboxes.class)) {
            Hitboxes hit = client.getModule(Hitboxes.class);

            if (!(ent instanceof PlayerEntity) && hit.getBoolSetting("Only Players")) {
                return;
            }
            if (!InventoryUtils.nameContains("sword") && hit.getBoolSetting("Only Sword")) {
                return;
            }

            cir.setReturnValue(hit.getRange());
        }
    }
}
