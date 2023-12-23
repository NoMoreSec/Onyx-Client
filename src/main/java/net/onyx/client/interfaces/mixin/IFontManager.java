package net.onyx.client.interfaces.mixin;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;

public interface IFontManager {
    TextRenderer createTextRendererFromIdentifier(Identifier id);
}
