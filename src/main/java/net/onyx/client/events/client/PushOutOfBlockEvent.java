package net.onyx.client.events.client;

import net.onyx.client.events.Event;

public class PushOutOfBlockEvent extends Event {
    public double x;
    public double d;
    public boolean cancel = false;


    public PushOutOfBlockEvent(double d, double x) {
        this.x = x;
        this.d = d;
    }
}
