package net.onyx.client.interfaces.mixin;

import net.minecraft.world.chunk.BlockEntityTickInvoker;

import java.util.List;

public interface IWorld {
    List<BlockEntityTickInvoker> getBlockEntityTickers();
}
