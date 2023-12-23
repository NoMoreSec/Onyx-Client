package net.onyx.client.modules.utilities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.world.chunk.Chunk;
import net.onyx.client.events.client.ChunkDataEvent;
import net.onyx.client.modules.DummyModule;

import java.util.ArrayList;
import java.util.List;

public class StashFinder extends DummyModule {

    public StashFinder() {
        super("StashFinder");

        this.setDescription("...");


        this.setCategory("Utility");


    }

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public List<Chunk> chunks = new ArrayList<>();


    @Override
    public void activate() {
        this.addListen(ChunkDataEvent.class);
    }

    @Override
    public void deactivate() {
        this.removeListen(ChunkDataEvent.class);
    }
}


/*
    @Override
    public void fireEvent(Event event) {
        switch (event.getClass().getSimpleName()) {
            case "ChunkDataEvent": {
                ChunkDataEvent e = ((ChunkDataEvent) event);
                double chunkXAbs = Math.abs(e.chunk.getPos().x * 16);
                double chunkZAbs = Math.abs(e.chunk.getPos().z * 16);
                if (Math.sqrt(chunkXAbs * chunkXAbs + chunkZAbs * chunkZAbs) < minimumDistance.get()) return;

                Chunk chunk = new Chunk(e.chunk.getPos());

                for (BlockEntity blockEntity : event.chunk.getBlockEntities().values()) {
                    if (!storageBlocks.get().contains(blockEntity.getType())) continue;

                    if (blockEntity instanceof ChestBlockEntity) chunk.chests++;
                    else if (blockEntity instanceof BarrelBlockEntity) chunk.barrels++;
                    else if (blockEntity instanceof ShulkerBoxBlockEntity) chunk.shulkers++;
                    else if (blockEntity instanceof EnderChestBlockEntity) chunk.enderChests++;
                    else if (blockEntity instanceof AbstractFurnaceBlockEntity) chunk.furnaces++;
                    else if (blockEntity instanceof DispenserBlockEntity) chunk.dispensersDroppers++;
                    else if (blockEntity instanceof HopperBlockEntity) chunk.hoppers++;
                }

                if (chunk.getTotal() >= minimumStorageCount.get()) {
                    Chunk prevChunk = null;
                    int i = chunks.indexOf(chunk);

                    if (i < 0) chunks.add(chunk);
                    else prevChunk = chunks.set(i, chunk);

                    saveJson();
                    saveCsv();

                    if (sendNotifications.get() && (!chunk.equals(prevChunk) || !chunk.countsEqual(prevChunk))) {
                        switch (notificationMode.get()) {
                            case Chat -> info("Found stash at (highlight)%s(default), (highlight)%s(default).", chunk.x, chunk.z);
                            case Toast -> mc.getToastManager().add(new MeteorToast(Items.CHEST, title, "Found Stash!"));
                            case Both -> {
                                info("Found stash at (highlight)%s(default), (highlight)%s(default).", chunk.x, chunk.z);
                                mc.getToastManager().add(new MeteorToast(Items.CHEST, title, "Found Stash!"));
                            }
                        }
                    }
                }
                ChunkDataEvent.returnChunkDataEvent(event);
            }
        }
    }
}

 */
