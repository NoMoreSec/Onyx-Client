package net.onyx.client.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.onyx.client.OnyxClient;
import net.onyx.client.config.settings.Setting;
import net.onyx.client.config.specials.Mode;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

public class Trails extends Module {


    public Trails() {
        super("Trails");

        this.setDescription("Renders particles at the players feet");

        this.setCategory("Render");

        this.addSetting(new Setting("MustMove", true) {{
        }});

        this.addSetting(new Setting("OtherPlayers", true) {{
        }});


        this.addSetting(new Setting("Particles", new Mode("CryingObsidian", "Portal", "Cloud", "Water", "Lava", "SoulFire", "Heart")) {{
        }});

    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }


    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                if (this.getBoolSetting("MustMove")
                        && OnyxClient.getClient().player.getVelocity().x == 0
                        && OnyxClient.getClient().player.getVelocity().y == 0
                        && OnyxClient.getClient().player.getVelocity().z == 0) return;

                DefaultParticleType particle = ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
                if (this.getModeSetting("Particles").is("CryingObsidian")) {
                    particle = ParticleTypes.DRIPPING_OBSIDIAN_TEAR;
                } else if (this.getModeSetting("Particles").is("Portal")) {
                    particle = ParticleTypes.PORTAL;
                } else if (this.getModeSetting("Particles").is("Cloud")) {
                    particle = ParticleTypes.CLOUD;
                } else if (this.getModeSetting("Particles").is("Water")) {
                    particle = ParticleTypes.DRIPPING_WATER;
                } else if (this.getModeSetting("Particles").is("Lava")) {
                    particle = ParticleTypes.LAVA;
                } else if (this.getModeSetting("Particles").is("SoulFire")) {
                    particle = ParticleTypes.SOUL_FIRE_FLAME;
                } else if (this.getModeSetting("Particles").is("Heart")) {
                    particle = ParticleTypes.HEART;
                }

                OnyxClient.getClient().world.addParticle(particle, OnyxClient.getClient().player.getX(), OnyxClient.getClient().player.getY(), OnyxClient.getClient().player.getZ(), 0, 0, 0);
                if (this.getBoolSetting("OtherPlayers")) {
                    Iterable<Entity> ents = OnyxClient.getClient().world.getEntities();
                    for (Entity entity : ents) {
                        PlayerEntity player = (PlayerEntity)entity;
                        OnyxClient.getClient().world.addParticle(particle, player.getX(), player.getY(), player.getZ(), 0, 0, 0);
                        break;
                    }
                }
            }
        }
    }
}

