package net.onyx.client.modules.render;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.render.OnRenderEvent;
import net.onyx.client.modules.Module;
import net.onyx.client.utils.RenderUtils;

public class ObiHightlight extends Module {
    public ObiHightlight() {
        super("ObiHightlight");

        this.setDescription("");


        this.setCategory("Render");
    }


    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
        this.addListen(OnRenderEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(OnRenderEvent.class);

    }

    private Vec3d targetPosAtPlaceCrystal;
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                for (Entity entity : OnyxClient.getClient().world.getEntities()) {
                    if (entity instanceof PlayerEntity player) {
                        Vec3d targetKB = calcTargetKB(player);
                        targetPosAtPlaceCrystal = simulatePos(player.getPos(), targetKB);

                    }
                }
                break;
            }
            case "OnRenderEvent": {
                OnRenderEvent e = ((OnRenderEvent) event);
                RenderUtils.renderFilledBlockBox(e.mStack, targetPosAtPlaceCrystal, 255, 0, 130, 255);
                break;
            }
        }
    }

    private Vec3d simulatePos(Vec3d start, Vec3d velocity)
    {
            double j, k, l;
            j = velocity.getX();
            k = velocity.getY();
            l = velocity.getZ();
            if (Math.abs(j) < 0.003)
                j = 0;
            if (Math.abs(k) < 0.003)
                k = 0;
            if (Math.abs(l) < 0.003)
                l = 0;
            velocity = new Vec3d(j, k, l);
            double g = 0;
            g -= 0.08;
            velocity = velocity.add(0.0D, g * 0.98, 0.0D);
            velocity = velocity.multiply(0.91, 1, 0.91);
            start = start.add(velocity);
        return start;
    }

    private Vec3d calcTargetKB(PlayerEntity target)
    {
        float h = OnyxClient.getClient().player.getAttackCooldownProgress(0.5F);
        int i = EnchantmentHelper.getKnockback(OnyxClient.getClient().player);
        if (OnyxClient.getClient().player.isSprinting() && h > 0.9)
            i += 1;
        double strength = (float) i * 0.5F;
        double x = MathHelper.sin(OnyxClient.getClient().player.getYaw() * 0.017453292F);
        double z = -MathHelper.cos(OnyxClient.getClient().player.getYaw() * 0.017453292F);
        Iterable<ItemStack> armors = target.getArmorItems();
        double kbRes = 0;
        for (ItemStack e : armors)
        {
            Item item = e.getItem();
            if (!(item instanceof ArmorItem armorItem))
                continue;
            if (armorItem.getMaterial() == ArmorMaterials.NETHERITE)
                kbRes += 0.1;
        }
        strength *= 1.0D - target.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE) - kbRes;
        Vec3d result = Vec3d.ZERO;
        if (strength > 0.0)
        {
            Vec3d vec3d = new Vec3d(target.getX() - target.prevX, target.getY() - target.prevY, target.getZ() - target.prevZ);
            Vec3d vec3d2 = (new Vec3d(x, 0.0D, z)).normalize().multiply(strength);
            result = new Vec3d(vec3d.x / 2.0 - vec3d2.x, target.isOnGround() ? Math.min(0.4D, vec3d.y / 2.0D + strength) : vec3d.y, vec3d.z / 2.0D - vec3d2.z);
        }
        return result;
    }
}


