package net.onyx.client.modules.combat;

import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;
import net.onyx.client.utils.WorldUtils;

public class AutoAnchorBreak extends Module {
   public AutoAnchorBreak() {
      super("AutoAnchorBreak");

      this.setDescription("Anchor like marlow :3");
      this.setCategory("Combat");
   }

   @Override
   public void fireEvent(Event event) {
      HitResult pos = WorldUtils.mc.crosshairTarget;
      if (pos instanceof BlockHitResult hit) {
         BlockPos posx = hit.getBlockPos();
         if (BlockUtils.isAnchorCharged(posx) && !WorldUtils.mc.player.getMainHandStack().isOf(Items.RESPAWN_ANCHOR)) {
            ActionResult actionResult = WorldUtils.mc.interactionManager.interactBlock(WorldUtils.mc.player, Hand.MAIN_HAND, hit);
            if (actionResult.isAccepted() && actionResult.shouldSwingHand())
               WorldUtils.mc.player.swingHand(Hand.MAIN_HAND);
         }
      }
   }

   @Override
   public void activate() {
      this.addListen(ClientTickEvent.class);
   }

   @Override
   public void deactivate() {
      this.removeListen(ClientTickEvent.class);
   }
}