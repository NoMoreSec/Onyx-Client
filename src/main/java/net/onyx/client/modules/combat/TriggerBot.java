package net.onyx.client.modules.combat;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class TriggerBot extends Module {


    public TriggerBot() {
        super("TriggerBot");

        this.setDescription("");

        this.setCategory("Combat");

        this.addSetting(new Setting("Cooldown", 1.0d) {{
            this.setMax(1.0d);
            this.setMin(0.0d);
            this.setDescription("cooldown when swinging sword in ticks");
        }});

        this.addSetting(new Setting("AttackInAir", true) {{
            this.setDescription("Whether or not to attack in mid air");
        }});
        this.addSetting(new Setting("AttackOnJump", true) {{
            this.setDescription("Whether or not to attack when jumping");
        }});


    }


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (OnyxClient.getClient().player.isUsingItem())
                    return;
                if (!(OnyxClient.getClient().player.getMainHandStack().getItem() instanceof SwordItem))
                    return;
                HitResult hit = OnyxClient.getClient().crosshairTarget;
                if (hit.getType() != HitResult.Type.ENTITY)
                    return;
                if (OnyxClient.getClient().player.getAttackCooldownProgress(0) < this.getDoubleSetting("Cooldown"))
                    return;
                Entity target = ((EntityHitResult) hit).getEntity();
                if (!(target instanceof PlayerEntity))
                    return;
                if (!target.isOnGround() && !this.getBoolSetting("AttackInAir"))
                    return;
                if (OnyxClient.getClient().player.getY() > OnyxClient.getClient().player.prevY && !this.getBoolSetting("AttackOnJump"))
                    return;
                OnyxClient.getClient().interactionManager.attackEntity(OnyxClient.getClient().player, target);
                OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
            }
        }
    }




}

