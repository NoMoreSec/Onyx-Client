package net.onyx.client.mixin.botch;

import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.onyx.client.OnyxClient;
import net.onyx.client.modules.chat.InfChat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class InfChatMixin {
    @Shadow
    protected TextFieldWidget chatField;

    @Inject(at = @At("TAIL"), method="init()V")
    private void onInit(CallbackInfo ci) {
        InfChat infChat = (InfChat)(OnyxClient.getInstance().getModules().get("infchat"));
        if (!infChat.isEnabled()) return;

        chatField.setMaxLength(Integer.MAX_VALUE);
    }

}
