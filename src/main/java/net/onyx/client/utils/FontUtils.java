package net.onyx.client.utils;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import net.onyx.client.OnyxClient;
import net.onyx.client.interfaces.mixin.IClient;
import net.onyx.client.interfaces.mixin.IFontManager;

public class FontUtils {
    public static TextRenderer createTextRenderer(Identifier fontId) {
        IClient client = (IClient) OnyxClient.getClient();
        IFontManager fontManager = (IFontManager)client.getFontManager();

        return fontManager.createTextRendererFromIdentifier(fontId);
    }

    public static TextRenderer createTextRenderer(String fontId) {
        return createTextRenderer(new Identifier(fontId));
    }
}
