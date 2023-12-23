package net.onyx.client.modules.render;

import net.minecraft.client.particle.ParticleManager;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.AddParticleEmitterEvent;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.events.render.AddParticleEvent;
import net.onyx.client.interfaces.mixin.IParticleManager;
import net.onyx.client.modules.Module;

public class NoParticles extends Module {

    public NoParticles() {
        super("NoParticles");

        this.setDescription("Blocks any particles from being rendered.");

        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(AddParticleEmitterEvent.class);
        this.addListen(AddParticleEvent.class);
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
        this.removeListen(AddParticleEvent.class);
        this.removeListen(AddParticleEmitterEvent.class);
    }

    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                ParticleManager pm = OnyxClient.getClient().particleManager;
                IParticleManager iPm = (IParticleManager)(pm);

                iPm.clearAll();

                break;
            }
            case "AddParticleEvent": {
                AddParticleEvent e = (AddParticleEvent)event;
                e.ci.cancel();

                break;
            }

            case "AddParticleEmitterEvent": {
                AddParticleEmitterEvent e = (AddParticleEmitterEvent)(event);
                e.ci.cancel();

                break;
            }
        }
    }


    }

