package net.onyx.client.onyxevent.events;

import net.minecraft.entity.Entity;
import net.onyx.client.onyxevent.Event;
import net.onyx.client.onyxevent.Listener;

import java.util.ArrayList;

public interface EntitySpawnListener extends Listener {
    void onEntitySpawn(Entity entity);

    class EntitySpawnEvent extends Event<EntitySpawnListener> {

        private final Entity entity;

        public EntitySpawnEvent(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void fire(ArrayList<EntitySpawnListener> listeners) {
            listeners.forEach(e -> e.onEntitySpawn(entity));
        }

        @Override
        public Class<EntitySpawnListener> getListenerType() {
            return EntitySpawnListener.class;
        }
    }
}