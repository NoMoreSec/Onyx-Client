package net.onyx.client.mixin.botch;

import net.minecraft.entity.Entity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.onyx.client.interfaces.mixin.IEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements IEntity {
    @Shadow
    protected boolean inNetherPortal;

    @Shadow
    @Final
    protected static TrackedData<Byte> FLAGS;

    @Shadow
    @Final
    protected DataTracker dataTracker;

    @Override
    public boolean getInNetherPortal() {
        return this.inNetherPortal;
    }

    @Override
    public boolean getEntFlag(int index) {
        return (this.dataTracker.get(FLAGS) & 1 << index) != 0;
    }
}
