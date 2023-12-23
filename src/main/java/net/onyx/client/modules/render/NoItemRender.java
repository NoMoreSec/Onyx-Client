package net.onyx.client.modules.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.onyx.client.OnyxClient;
import net.onyx.client.events.Event;
import net.onyx.client.events.client.ClientTickEvent;
import net.onyx.client.modules.Module;

import java.util.ArrayList;
import java.util.List;

public class NoItemRender extends Module {
    public NoItemRender() {
        super("NoItemRender");

        this.setDescription("Hide all dropped items so then your friends cannot kill your client repeatedly.");
        this.setCategory("Render");
    }

    @Override
    public void activate() {
        this.addListen(ClientTickEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ClientTickEvent.class);
    }

    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ClientTickEvent": {
                List<Entity> items = new ArrayList<>();

                // Get all the entities
                for (Entity entity : OnyxClient.getClient().world.getEntities()) {
                    if (!(entity instanceof ItemEntity)) continue;

                    items.add(entity);
                }

                // Delete all
                for (Entity entity : items) entity.discard();

                break;
            }
        }
    }


    }
