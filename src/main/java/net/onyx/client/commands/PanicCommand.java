package net.onyx.client.commands;

import net.onyx.client.OnyxClient;
import net.onyx.client.commands.structures.Command;
import net.onyx.client.modules.Module;

public class PanicCommand extends Command {

    public PanicCommand() {
        super("panic", "panic", "Disables all of your active mods.");

        this.commandDisplay = "Panic";
    }

    @Override
    public Boolean trigger(String[] args) {
        // Make sure that the user knows what they are doing.
        if (args.length == 0 || !args[0].toLowerCase().startsWith("y")) {
            this.displayChatMessage("Are you sure you want to disable all your mods?");
            this.displayChatMessage("Usage: panic <y/N>");
            return true;
        }

        // Disable all
        for (Module module : OnyxClient.getInstance().getModules().values()) {
            if (module.isEnabled()) module.disable();
        }
        this.displayChatMessage("All mods have been disabled.");
        return true;
    }
}
