package net.onyx.client.events.client;

import net.minecraft.world.chunk.WorldChunk;
import net.onyx.client.events.Event;

public class ChunkDataEvent extends Event {
    public WorldChunk chunk;


    public ChunkDataEvent(WorldChunk chunk) {
        this.chunk = chunk;
    }
}


