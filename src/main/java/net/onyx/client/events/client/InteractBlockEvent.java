package net.onyx.client.events.client;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.onyx.client.events.Event;

public class InteractBlockEvent extends Event {
    public Hand hand;
    public BlockHitResult result;
    public boolean cancel = false;


    public InteractBlockEvent(Hand hand, BlockHitResult result) {
        this.hand = hand;
        this.result = result;
    }
}
