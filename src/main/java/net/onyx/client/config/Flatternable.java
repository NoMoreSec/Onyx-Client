package net.onyx.client.config;

import java.util.HashMap;

public interface Flatternable {
    HashMap<String, String> flatten();
    void lift(HashMap<String, String> flat);
}
