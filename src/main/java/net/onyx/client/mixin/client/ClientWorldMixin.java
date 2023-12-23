package net.onyx.client.mixin.client;

import net.minecraft.client.network.PendingUpdateManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.client.GetEntitiesEvent;
import net.onyx.client.interfaces.mixin.IClientWorld;
import net.onyx.client.modules.render.Ambience;
import net.onyx.client.onyxevent.EventManager;
import net.onyx.client.onyxevent.events.EntityDespawnListener;
import net.onyx.client.onyxevent.events.EntitySpawnListener;
import net.onyx.client.utils.Color;
import net.onyx.client.utils.Dimension;
import net.onyx.client.utils.PlayerUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin implements IClientWorld {
    private final int skyRed = Ambience.skyRed;
    private final int skyGreen = Ambience.skyGreen;
    private final int skyBlue = Ambience.skyBlue;

    private final int netherSkyRed = Ambience.netherSkyRed;
    private final int netherSkyGreen = Ambience.netherSkyGreen;
    private final int netherSkyBlue = Ambience.netherSkyBlue;

    private final int endSkyRed = Ambience.endSkyRed;
    private final int endSkyGreen = Ambience.endSkyGreen;
    private final int endSkyBlue = Ambience.endSkyBlue;
    @Shadow @Final private PendingUpdateManager pendingUpdateManager;

    @Inject(at = @At("HEAD"), method = "getEntities", cancellable = true)
    private void onGetEntities(CallbackInfoReturnable<Iterable<Entity>> cir) {
        OnyxClient.getInstance().emitter.triggerEvent(new GetEntitiesEvent(cir));
    }

    @Inject(method = "addEntityPrivate", at = @At("TAIL"))
    private void onAddEntityPrivate(int id, Entity entity, CallbackInfo info) {
        if (entity != null)
            EventManager.fire(new EntitySpawnListener.EntitySpawnEvent(entity));
    }

    @Inject(method = "removeEntity", at = @At("TAIL"))
    private void onFinishRemovingEntity(int entityId, Entity.RemovalReason removalReason, CallbackInfo info) {
        Entity entity = OnyxClient.getClient().world.getEntityById(entityId);
        if (entity != null)
            EventManager.fire(new EntityDespawnListener.EntityDespawnEvent(entity));
    }

    @Inject(method = "getSkyColor", at = @At("HEAD"), cancellable = true)
    private void onGetSkyColor(Vec3d cameraPos, float tickDelta, CallbackInfoReturnable<Vec3d> info) {
        if (Ambience.shouldSky && PlayerUtils.getDimension() == Dimension.Overworld) {
            Color skyColor = new Color(skyRed, skyGreen, skyBlue);
            Vec3d newSkyColor = new Vec3d(skyColor.r/255f, skyColor.g/255f, skyColor.b/255f);
            info.setReturnValue(newSkyColor);
            info.cancel();
        }
        if (Ambience.shouldNetherSky && PlayerUtils.getDimension() == Dimension.Nether) {
            Color netherSkyColor = new Color(netherSkyRed, netherSkyGreen, netherSkyBlue);
            Vec3d newSkyColor = new Vec3d(netherSkyColor.r/255f, netherSkyColor.g/255f, netherSkyColor.b/255f);
            info.setReturnValue(newSkyColor);
            info.cancel();
        }
        if (Ambience.shouldEndSky && PlayerUtils.getDimension() == Dimension.End) {
            Color endSkyColor = new Color(endSkyRed, endSkyGreen, endSkyBlue);
            Vec3d newSkyColor = new Vec3d(endSkyColor.r/255f, endSkyColor.g/255f, endSkyColor.b/255f);
            info.setReturnValue(newSkyColor);
            info.cancel();
        }
    }

    @Inject(method = "getCloudsColor", at = @At("HEAD"), cancellable = true)
    private void onGetCloudsColor(float tickDelta, CallbackInfoReturnable<Vec3d> cir) {
        if (Ambience.shouldCloud) {
            Color cloudColor = new Color(Ambience.cloudRed, Ambience.cloudGreen, Ambience.cloudBlue);
            Vec3d newCloudColor = new Vec3d(cloudColor.r / 255f, cloudColor.g / 255f, cloudColor.b / 255f);
            cir.setReturnValue(newCloudColor);
            cir.cancel();
        }
    }

    @Override
    public PendingUpdateManager obtainPendingUpdateManager() {
        return this.pendingUpdateManager;
    }
}
