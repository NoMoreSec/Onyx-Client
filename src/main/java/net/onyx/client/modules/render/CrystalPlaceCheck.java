package net.onyx.client.modules.render;

import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.misc.Colour;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.CrystalUtils;
import net.onyx.client.utils.RenderUtils;
import net.onyx.client.utils.RotationUtils;

public class CrystalPlaceCheck extends Module {


    public CrystalPlaceCheck() {
        super("CrystalPlaceCheck");

        this.setDescription("checks if you can place obsidian on the obsidian");

        this.setCategory("Render");


    }


    @Override
    public void activate() {
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(OnRenderEvent.class);
    }


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "OnRenderEvent": {
                OnRenderEvent e = ((OnRenderEvent) event);
                Colour cannotPlaceColour = OnyxClient.getInstance().config.unableToPlaceCrystalColour;
                Colour canPlaceColour = OnyxClient.getInstance().config.canPlaceCrystalColour;
                Vec3d camPos = OnyxClient.getClient().player.getEyePos();
                BlockHitResult blockHit = OnyxClient.getClient().world.raycast(new RaycastContext(camPos, camPos.add(RotationUtils.getClientLookVec().multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, OnyxClient.getClient().player));
                if (!BlockUtils.isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()))
                    return;
                if (CrystalUtils.canPlaceCrystalServer(blockHit.getBlockPos())) {
                    RenderUtils.renderFilledBlockBox(e.mStack, blockHit.getBlockPos(), canPlaceColour.r, canPlaceColour.g, canPlaceColour.b, canPlaceColour.a);
                } else if (!CrystalUtils.canPlaceCrystalServer(blockHit.getBlockPos())) {
                    RenderUtils.renderFilledBlockBox(e.mStack, blockHit.getBlockPos(), cannotPlaceColour.r, cannotPlaceColour.g, cannotPlaceColour.b, cannotPlaceColour.a);
                }
                break;
            }
        }
    }
}




