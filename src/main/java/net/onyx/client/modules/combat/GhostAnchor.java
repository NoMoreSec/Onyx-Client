package net.onyx.client.modules.combat;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.InventoryUtils;
import net.onyx.client.utils.RotationUtils;

public class GhostAnchor extends Module {


    public GhostAnchor() {
        super("GhostAnchor");

        this.setDescription("Allows you to use anchors without holding them");

        this.addSetting(new Setting("NoHold", false) {{
        }});

        this.setCategory("Combat");

    }


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    private boolean hasPlacedRespawnAnchor;
    private boolean hasPlacedGlowstone;

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (!OnyxClient.getClient().options.useKey.isPressed()) {
                    hasPlacedRespawnAnchor = false;
                    hasPlacedGlowstone = false;
                }
                if (hasPlacedRespawnAnchor && this.getBoolSetting("NoHold"))
                    return;

                if (hasPlacedGlowstone && this.getBoolSetting("NoHold"))
                    return;
                ItemStack mainHand = OnyxClient.getClient().player.getMainHandStack();
                if (mainHand.isOf(Items.RESPAWN_ANCHOR))
                    return;
                Vec3d camPos = OnyxClient.getClient().player.getEyePos();
                BlockHitResult blockHit = OnyxClient.getClient().world.raycast(new RaycastContext(camPos, camPos.add(RotationUtils.getClientLookVec().multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, OnyxClient.getClient().player));
                BlockPos pos = blockHit.getBlockPos();
                if (BlockUtils.isBlock(Blocks.AIR, blockHit.getBlockPos()))
                    return;
                if (BlockUtils.isAnchorCharged(pos)) {
                    if (OnyxClient.getClient().options.useKey.isPressed())
                        return;
                }
                ItemStack item = OnyxClient.getClient().player.getStackInHand(OnyxClient.getClient().player.getActiveHand());
                Item type = item.getItem();
                if (OnyxClient.getClient().options.useKey.isPressed()) {
                    if (InventoryUtils.nameContains("totem") || InventoryUtils.nameContains("sword")) {
                        if (BlockUtils.isAnchorUncharged(blockHit.getBlockPos())) {
                            InventoryUtils.search(Items.GLOWSTONE);
                            BlockUtils.interact(pos, blockHit.getSide());
                            hasPlacedGlowstone = true;
                            InventoryUtils.search(type);
                        }
                        if (BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, blockHit.getBlockPos()))
                            return;
                        InventoryUtils.search(Items.RESPAWN_ANCHOR);
                        BlockUtils.interact(pos, blockHit.getSide());
                        hasPlacedRespawnAnchor = true;
                        InventoryUtils.search(type);
                    }
                }
            }
        }
    }
}








