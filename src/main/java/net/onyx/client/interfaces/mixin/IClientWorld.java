package net.onyx.client.interfaces.mixin;

import net.minecraft.client.network.PendingUpdateManager;

public interface IClientWorld {
    PendingUpdateManager obtainPendingUpdateManager();
}
