package net.onyx.client.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.RenderUtils;

public class ShowDamageTick extends Module {


    public ShowDamageTick() {
        super("PlayerState");

        this.setDescription("Renders what state other players are in");

        this.setCategory("Render");


    }
    public static boolean cannotBeHit;

    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
        cannotBeHit = false;
    }


    private boolean canEntityBeHit() {
        return OnyxClient.getClient().world.getPlayers().parallelStream()
                .filter(e -> OnyxClient.getClient().player != e)
                .filter(e -> e.squaredDistanceTo(OnyxClient.getClient().player) < 25)
                .noneMatch(e -> e.hurtTime > 0);
    }

    private boolean isEntityInAir() {
        return OnyxClient.getClient().world.getPlayers().parallelStream()
                .filter(e -> OnyxClient.getClient().player != e)
                .filter(e -> e.squaredDistanceTo(OnyxClient.getClient().player) < 25)
                .noneMatch(e -> !e.isOnGround());
    }




    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnRenderEvent": {
                OnRenderEvent e = ((OnRenderEvent) event);
                HitResult hit = OnyxClient.getClient().crosshairTarget;
                if (hit.getType() != HitResult.Type.ENTITY)
                    return;
                Entity target = ((EntityHitResult) hit).getEntity();
                if (!(target instanceof PlayerEntity)) {
                    return;
                }
                if (((PlayerEntity) target).hurtTime > 0 || !target.isOnGround()) {
                    RenderUtils.renderBox(target, e.tickDelta, e.mStack, false, 0);
                }
            }
        }
    }
}

