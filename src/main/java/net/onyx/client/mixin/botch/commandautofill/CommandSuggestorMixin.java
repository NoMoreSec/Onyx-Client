package net.onyx.client.mixin.botch.commandautofill;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.command.CommandSource;
import net.onyx.client.OnyxClient;
import net.onyx.client.commands.structures.CommandHandler;
import net.onyx.client.modules.chat.CommandAutoFill;
import net.onyx.client.utils.ChatUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.CompletableFuture;

@Mixin(ChatInputSuggestor.class)
public abstract class CommandSuggestorMixin {
    @Shadow TextFieldWidget textField;
    @Shadow CompletableFuture<Suggestions> pendingSuggestions;

    private CommandAutoFill getCommandAutoFill() {
        return (CommandAutoFill) OnyxClient.getInstance().getModules().get("chatsuggestion");
    }

    @Shadow abstract void show(boolean narrateFirstSuggestion);
    
    @Inject(at = @At("TAIL"), method="refresh()V", cancellable = true)
    void onRefresh(CallbackInfo ci) {
        if (!this.getCommandAutoFill().isEnabled()) return;

        String text = this.textField.getText();

        // Make sure that the command starts with a . or ,
        CommandHandler handler = OnyxClient.getInstance().commandHandler;
        if (!text.startsWith(handler.delimiter)) return;

        // We're taking control here!
        ci.cancel();

        // Get the command dispatcher
        String dispatcher = text.substring(0, this.textField.getCursor());
        Integer wordStart = ChatUtils.getStartOfCurrentWord(text);

        // Get the matching suggestions to the current input
        this.pendingSuggestions = CommandSource.suggestMatching(
            this.getCommandAutoFill().getSuggestions(dispatcher, handler),
            new SuggestionsBuilder(text, wordStart)
        );
        
        this.pendingSuggestions.thenRun(() -> {
            if (!this.pendingSuggestions.isDone()) {
                return;
            }
            
            // We cannot use show since it is not a typical command
            this.show(true);
        });
    }
}
