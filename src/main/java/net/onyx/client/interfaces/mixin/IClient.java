package net.onyx.client.interfaces.mixin;

import net.minecraft.client.font.FontManager;

public interface IClient {
    FontManager getFontManager();
    void performItemUse();
    boolean performAttack();
    void setAttackCooldown(int cooldown);
}
