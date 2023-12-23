package net.onyx.client.modules.utilities.discordrpcutils;

import com.google.gson.JsonObject;

public record Packet(Opcode opcode, JsonObject data) {
}
