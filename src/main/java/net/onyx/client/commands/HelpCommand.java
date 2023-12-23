package net.onyx.client.commands;

import net.onyx.client.OnyxClient;
import net.onyx.client.commands.structures.Command;
import net.onyx.client.utils.ChatUtils;

import java.util.HashMap;


public class HelpCommand extends Command {
    public HelpCommand(HashMap<String, Command> refCommands) {
        super("help", "If you need help with help then get help.", "Lists all the commands with their descriptions.");
    
        this.refCommands = refCommands;
    }

    @Override
    public Boolean trigger(String[] args) {
        String displayText = "All of the commands:";

        // Gather the information.
        Integer i = 1;
        for (HashMap.Entry<String, Command> entry : refCommands.entrySet()) {
            displayText += String.format("\n%s%s%s - %s", ChatUtils.GREEN, entry.getKey(), ChatUtils.WHITE, entry.getValue().getDescription());

            i++;
        }

        // Tell them how it be.
        OnyxClient.displayChatMessage(String.format("%s%s", ChatUtils.WHITE, displayText));

        // How can help go wrong?!
        return true;
    }

    private final HashMap<String, Command> refCommands;

    
}
