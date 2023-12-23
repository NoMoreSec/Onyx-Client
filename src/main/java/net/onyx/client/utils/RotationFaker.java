package net.onyx.client.utils;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;

public final class RotationFaker
{
    private static boolean fakeRotation;
    private static float serverYaw;
    private static float serverPitch;
    private static float realYaw;
    private static float realPitch;

    public static void onPreMotion()
    {
        if(!fakeRotation)
            return;

        ClientPlayerEntity player = OnyxClient.getClient().player;
        realYaw = player.getYaw();
        realPitch = player.getPitch();
        player.setYaw(serverYaw);
        player.setPitch(serverPitch);
    }


    public static void onPostMotion()
    {
        if(!fakeRotation)
            return;

        ClientPlayerEntity player = OnyxClient.getClient().player;
        player.setYaw(realYaw);
        player.setPitch(realPitch);
        fakeRotation = false;
    }

    public void faceVectorPacket(Vec3d vec)
    {
        RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);
        ClientPlayerEntity player = OnyxClient.getClient().player;

        fakeRotation = true;
        serverYaw =
                RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw());
        serverPitch = needed.getPitch();
    }

    public void faceVectorClient(Vec3d vec)
    {
        RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);

        ClientPlayerEntity player = OnyxClient.getClient().player;
        player.setYaw(
                RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw()));
        player.setPitch(needed.getPitch());
    }

    public void faceVectorClientIgnorePitch(Vec3d vec)
    {
        RotationUtils.Rotation needed = RotationUtils.getNeededRotations(vec);

        ClientPlayerEntity player = OnyxClient.getClient().player;
        OnyxClient.getClient().player.setYaw(
                RotationUtils.limitAngleChange(player.getYaw(), needed.getYaw()));
        OnyxClient.getClient().player.setPitch(0);
    }

    public float getServerYaw()
    {
        return fakeRotation ? serverYaw : OnyxClient.getClient().player.getYaw();
    }

    public float getServerPitch()
    {
        return fakeRotation ? serverPitch : OnyxClient.getClient().player.getPitch();
    }
}
