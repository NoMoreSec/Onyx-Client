package net.onyx.client.modules.packet;

import net.minecraft.block.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.InteractBlockEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.BlockUtils;


public class NoDesyncPlace extends Module {
    public NoDesyncPlace() {
        super("NoDesyncPlace");

        this.setDescription("Removes delay between the client and the server");
        this.addSetting(new Setting("RenderSwing", true) {{
        }});
        this.setCategory("Packet");
    }

    @Override
    public void activate() {
        this.addListen(InteractBlockEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(InteractBlockEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "InteractBlockEvent": {
                InteractBlockEvent e = ((InteractBlockEvent) event);
                if (e.hand != null && e.result != null) {
                    ItemStack stack = OnyxClient.getClient().player.getStackInHand(e.hand);
                    BlockPos pos = e.result.getBlockPos();
                    //stops anchors being filled more than once
                    if (BlockUtils.isBlock(Blocks.RESPAWN_ANCHOR, pos))
                        return;
                    if (stack.getItem() instanceof BlockItem item
                            && (!isClickable(OnyxClient.getClient().world.getBlockState(pos).getBlock()) || OnyxClient.getClient().player.isSneaking())
                            && canPlace(pos.offset(e.result.getSide()), item.getBlock().getDefaultState())) {

                        e.cancel = true;

                        place(e.result, e.hand);
                    }
                }
                break;
            }
        }
    }

    private boolean isClickable(Block block) {
        return block instanceof CraftingTableBlock || block instanceof AnvilBlock || block instanceof AbstractPressurePlateBlock || block instanceof BlockWithEntity || block instanceof BedBlock || block instanceof FenceGateBlock || block instanceof DoorBlock || block instanceof NoteBlock || block instanceof TrapdoorBlock;
    }

    private void place(BlockHitResult result, Hand hand) {
        if (hand != null && result != null) {
            OnyxClient.getClient().getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(hand, result, 0));

            if (OnyxClient.getClient().player.getStackInHand(hand).getItem() instanceof BlockItem item) {
                Block block = item.getBlock();
                BlockSoundGroup group = block.getSoundGroup(block.getDefaultState());

                OnyxClient.getClient().getSoundManager().play(new PositionedSoundInstance(group.getPlaceSound(), SoundCategory.BLOCKS, (group.getVolume() + 1.0F) / 8.0F, group.getPitch() * 0.5F, Random.create(), result.getBlockPos()));
            }

            if (this.getBoolSetting("RenderSwing")) {
                OnyxClient.getClient().player.swingHand(hand);
            } else {
                OnyxClient.getClient().getNetworkHandler().sendPacket(new HandSwingC2SPacket(hand));
            }
        }
    }

    private boolean canPlace(BlockPos pos, BlockState state) {
        if (pos == null || OnyxClient.getClient().world == null || !World.isValid(pos) || !OnyxClient.getClient().world.getBlockState(pos).getMaterial().isReplaceable()) return false;
        return OnyxClient.getClient().world.getWorldBorder().contains(pos) && OnyxClient.getClient().world.canPlace(state, pos, ShapeContext.absent());
    }
}
