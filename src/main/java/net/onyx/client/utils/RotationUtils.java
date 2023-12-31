package net.onyx.client.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;

import static net.onyx.client.utils.WorldUtils.mc;

public enum RotationUtils
{
    ;

    public static float serverPitch;
    public static float serverYaw;

    public static int rotationTimer;






    public static float[] getViewingRotation(Entity entity, double x, double y, double z) {
        double diffX = x - entity.getX();
        double diffY = y - entity.getEyeY();
        double diffZ = z - entity.getZ();

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        return new float[] {
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90f,
                (float) -Math.toDegrees(Math.atan2(diffY, diffXZ)) };
    }

    public static void facePos(double x, double y, double z) {
        float[] rot = getViewingRotation(mc.player, x, y, z);

        mc.player.setYaw(mc.player.getYaw() + MathHelper.wrapDegrees(rot[0] - mc.player.getYaw()));
        mc.player.setPitch(mc.player.getPitch() + MathHelper.wrapDegrees(rot[1] - mc.player.getPitch()));
    }

    public static void facePosPacket(double x, double y, double z) {
        float[] rot = getViewingRotation(mc.player, x, y, z);

        if (!mc.player.hasVehicle()) {
            mc.player.headYaw = mc.player.getYaw() + MathHelper.wrapDegrees(rot[0] - mc.player.getYaw());
            mc.player.bodyYaw = mc.player.headYaw;
            mc.player.renderPitch = mc.player.getPitch() + MathHelper.wrapDegrees(rot[1] - mc.player.getPitch());
        }

        mc.player.networkHandler.sendPacket(
                new PlayerMoveC2SPacket.LookAndOnGround(
                        mc.player.getYaw() + MathHelper.wrapDegrees(rot[0] - mc.player.getYaw()),
                        mc.player.getPitch() + MathHelper.wrapDegrees(rot[1] - mc.player.getPitch()), mc.player.isOnGround()));
    }





    public static void tick() {
        rotationTimer++;
    }

    public static Vec3d getEyesPos()
    {
        ClientPlayerEntity player = OnyxClient.getClient().player;

        return new Vec3d(player.getX(),
                player.getY() + player.getEyeHeight(player.getPose()),
                player.getZ());
    }

    public static Vec3d getClientLookVec()
    {
        ClientPlayerEntity player = OnyxClient.getClient().player;
        float f = 0.017453292F;
        float pi = (float)Math.PI;

        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
        float f3 = -MathHelper.cos(-player.getPitch() * f);
        float f4 = MathHelper.sin(-player.getPitch() * f);

        return new Vec3d(f2 * f3, f4, f1 * f3);
    }




    public static Rotation getNeededRotations(Vec3d vec)
    {
        Vec3d eyesPos = getEyesPos();

        double diffX = vec.x - eyesPos.x;
        double diffY = vec.y - eyesPos.y;
        double diffZ = vec.z - eyesPos.z;

        double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);

        float yaw = (float)Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F;
        float pitch = (float)-Math.toDegrees(Math.atan2(diffY, diffXZ));

        return Rotation.wrapped(yaw, pitch);
    }

    public static double getAngleToLookVec(Vec3d vec)
    {
        Rotation needed = getNeededRotations(vec);

        ClientPlayerEntity player = OnyxClient.getClient().player;
        float currentYaw = MathHelper.wrapDegrees(player.getYaw());
        float currentPitch = MathHelper.wrapDegrees(player.getPitch());

        float diffYaw = MathHelper.wrapDegrees(currentYaw - needed.yaw);
        float diffPitch = MathHelper.wrapDegrees(currentPitch - needed.pitch);

        return Math.sqrt(diffYaw * diffYaw + diffPitch * diffPitch);
    }








    public static float getHorizontalAngleToLookVec(Vec3d vec)
    {
        Rotation needed = getNeededRotations(vec);
        return MathHelper.wrapDegrees(OnyxClient.getClient().player.getYaw())
                - needed.yaw;
    }

    /**
     * Returns the next rotation that the player should be facing in order to
     * slowly turn towards the specified end rotation, at a rate of roughly
     * <code>maxChange</code> degrees per tick.
     */
    public static Rotation slowlyTurnTowards(Rotation end, float maxChange)
    {
        Entity player = OnyxClient.getClient().player;
        float startYaw = player.prevYaw;
        float startPitch = player.prevPitch;
        float endYaw = end.getYaw();
        float endPitch = end.getPitch();

        float yawChange = Math.abs(MathHelper.wrapDegrees(endYaw - startYaw));
        float pitchChange =
                Math.abs(MathHelper.wrapDegrees(endPitch - startPitch));

        float maxChangeYaw =
                Math.min(maxChange, maxChange * yawChange / pitchChange);
        float maxChangePitch =
                Math.min(maxChange, maxChange * pitchChange / yawChange);

        float nextYaw = limitAngleChange(startYaw, endYaw, maxChangeYaw);
        float nextPitch =
                limitAngleChange(startPitch, endPitch, maxChangePitch);

        return new Rotation(nextYaw, nextPitch);
    }

    /**
     * Limits the change in angle between the current and intended rotation to
     * the specified maximum change. Useful for smoothing out rotations and
     * making combat hacks harder to detect.
     *
     * <p>
     * For best results, do not wrap the current angle before calling this
     * method!
     */
    public static float limitAngleChange(float current, float intended,
                                         float maxChange)
    {
        float currentWrapped = MathHelper.wrapDegrees(current);
        float intendedWrapped = MathHelper.wrapDegrees(intended);

        float change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped);
        change = MathHelper.clamp(change, -maxChange, maxChange);

        return current + change;
    }

    /**
     * Removes unnecessary changes in angle caused by wrapping. Useful for
     * making combat hacks harder to detect.
     *
     * <p>
     * For example, if the current angle is 179 degrees and the intended angle
     * is -179 degrees, you only need to turn 2 degrees to face the intended
     * angle, not 358 degrees.
     *
     * <p>
     * DO NOT wrap the current angle before calling this method! You will get
     * incorrect results if you do.
     */
    public static float limitAngleChange(float current, float intended)
    {
        float currentWrapped = MathHelper.wrapDegrees(current);
        float intendedWrapped = MathHelper.wrapDegrees(intended);

        float change = MathHelper.wrapDegrees(intendedWrapped - currentWrapped);

        return current + change;
    }

    public static final class Rotation
    {
        private final float yaw;
        private final float pitch;

        public Rotation(float yaw, float pitch)
        {
            this.yaw = yaw;
            this.pitch = pitch;
        }

        public static Rotation wrapped(float yaw, float pitch)
        {
            return new Rotation(MathHelper.wrapDegrees(yaw),
                    MathHelper.wrapDegrees(pitch));
        }

        public float getYaw()
        {
            return yaw;
        }

        public float getPitch()
        {
            return pitch;
        }
    }
}
