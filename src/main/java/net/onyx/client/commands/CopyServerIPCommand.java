package net.onyx.client.commands;

import net.minecraft.client.network.ServerInfo;
import net.onyx.client.OnyxClient;
import net.onyx.client.commands.structures.Command;
import net.onyx.client.utils.ChatUtils;
import net.onyx.client.utils.ServerUtils;

public class CopyServerIPCommand extends Command {
    public CopyServerIPCommand() {
        super("copyip", "<no args>", "copies the last server's address to the clipboard");
    }

    @Override
    public Boolean trigger(String[] args) {
        ServerInfo lastServer = ServerUtils.getLastServer();

        if (lastServer == null) {
            this.displayChatMessage(
                String.format("%sUnable to find server.", ChatUtils.RED)
            );

            return true;
        }

        // Get server address and print it
        String ip = lastServer.address;
        this.displayChatMessage(String.format("Copied server address to clipboard: %s%s", ChatUtils.GREEN, ip));

        // Set the clipboard
        OnyxClient.getClient().keyboard.setClipboard(ip);

        return true;
    }
}
