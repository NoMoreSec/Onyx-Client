package net.onyx.client.modules.combat;

import net.minecraft.block.Blocks;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.Input;
import net.onyx.client.utils.InventoryUtils;
import org.lwjgl.glfw.GLFW;

public class SafeAnchor extends Module {


    public SafeAnchor() {
        super("SafeAnchor");

        this.setDescription("Anchor without taking any damage");

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


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (GLFW.glfwGetMouseButton(OnyxClient.getClient().getWindow().getHandle(), 1) != 1) {
                    return;
                }
                //GENIUS
                if (OnyxClient.getClient().player.getPitch() < 15.0F) {
                    return;
                }
                if (OnyxClient.getClient().player.isUsingItem()) {
                    return;
                }
                final HitResult cr = OnyxClient.getClient().crosshairTarget;
                if (cr instanceof BlockHitResult hit) {
                    final BlockPos pos = hit.getBlockPos();
                    if (BlockUtils.isAnchorUncharged(pos)) {
                        if (OnyxClient.getClient().player.isHolding(Items.GLOWSTONE)) {
                            final ActionResult actionResult = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                            if (actionResult.isAccepted() && ActionResult.CONSUME.shouldSwingHand()) {
                                OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                            }
                            return;
                        }
                        InventoryUtils.selectItemFromHotbar(Items.GLOWSTONE);
                        final ActionResult actionResult = OnyxClient.getClient().interactionManager.interactBlock(OnyxClient.getClient().player, Hand.MAIN_HAND, hit);
                        if (actionResult.isAccepted() && ActionResult.CONSUME.shouldSwingHand()) {
                            OnyxClient.getClient().player.swingHand(Hand.MAIN_HAND);
                        }
                        if (BlockUtils.isAnchorCharged(pos)) {
                            setPressed(OnyxClient.getClient().options.sneakKey, true);
                        }
                    } else if (BlockUtils.isBlock(Blocks.GLOWSTONE, pos)) {
                        InventoryUtils.selectItemFromHotbar(Items.TOTEM_OF_UNDYING);
                        ItemStack mainHand = OnyxClient.getClient().player.getMainHandStack();
                        if (mainHand.isOf(Items.TOTEM_OF_UNDYING)) {
                            unpress();
                        } else unpress();
                    }
                }
            }
        }
    }

    private void unpress() {
        setPressed(OnyxClient.getClient().options.sneakKey, false);
    }

    private void setPressed(KeyBinding key, boolean pressed) {
        key.setPressed(pressed);
        Input.setKeyState(key, pressed);
    }
}

