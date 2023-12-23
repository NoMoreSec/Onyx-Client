package net.onyx.client.mixin.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EndCrystalItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.onyx.client.OnyxClient;
import net.onyx.client.modules.combat.AntiCrystalBounce;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin({EndCrystalItem.class})
public class EndCrystalItemMixin {


    /**
     * @Author Walksy
     */

    private Vec3d getPlayerLookVec(PlayerEntity player) {
        float f = 0.017453292F;
        float pi = 3.1415927F;
        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
        float f3 = -MathHelper.cos(-player.getPitch() * f);
        float f4 = MathHelper.sin(-player.getPitch() * f);
        return (new Vec3d((f2 * f3), f4, (f1 * f3))).normalize();
    }

    private Vec3d getClientLookVec() {
        return getPlayerLookVec(OnyxClient.getClient().player);
    }

    private boolean isBlock(Block block, BlockPos pos) {
        return (getBlockState(pos).getBlock() == block);
    }

    private BlockState getBlockState(BlockPos pos) {
        return OnyxClient.getClient().world.getBlockState(pos);
    }

    private boolean canPlaceCrystalServer(BlockPos block) {
        BlockState blockState = OnyxClient.getClient().world.getBlockState(block);
        if (!blockState.isOf(Blocks.OBSIDIAN) && !blockState.isOf(Blocks.BEDROCK))
            return false;
        BlockPos blockPos2 = block.up();
        if (!OnyxClient.getClient().world.isAir(blockPos2))
            return false;
        double d = blockPos2.getX();
        double e = blockPos2.getY();
        double f = blockPos2.getZ();
        List<Entity> list = OnyxClient.getClient().world.getOtherEntities(null, new Box(d, e, f, d + 1.0D, e + 2.0D, f + 1.0D));
        return list.isEmpty();
    }




    /**
     * Stops crystals from decreasing too much
     * PS: does not work on singleplayer
     */

    @Inject(method = {"useOnBlock"}, at = {@At("HEAD")}, cancellable = true)
    private void modifyDecrementAmount(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir) {
        if (!AntiCrystalBounce.enabled) return;
        ItemStack mainHandStack = OnyxClient.getClient().player.getMainHandStack();
        if (mainHandStack.isOf(Items.END_CRYSTAL)) {
            Vec3d camPos = OnyxClient.getClient().player.getEyePos();
            BlockHitResult blockHit = OnyxClient.getClient().world.raycast(new RaycastContext(camPos, camPos.add(getClientLookVec().multiply(4.5)), RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, OnyxClient.getClient().player));
            if (isBlock(Blocks.OBSIDIAN, blockHit.getBlockPos()) || isBlock(Blocks.BEDROCK, blockHit.getBlockPos())) {
                HitResult hitResult = OnyxClient.getClient().crosshairTarget;
                if (hitResult instanceof BlockHitResult hit) {
                    BlockPos block = hit.getBlockPos();
                    if (canPlaceCrystalServer(block))
                        context.getStack().decrement(-1);
                }
            }
        }
    }
}
