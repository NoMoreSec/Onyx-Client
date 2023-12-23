package net.onyx.client.mixin.botch;

import net.minecraft.client.option.SimpleOption;
import net.onyx.client.interfaces.mixin.SimpleOptionAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public class SimpleOptionMixin<T> implements SimpleOptionAccessor<T> {

    @Shadow private Consumer<T> changeCallback;
    @Shadow private T value;

    @Override
    public void setUnsafeValue(T value) {
        if (!Objects.equals(this.value, value)) {
            changeCallback.accept(this.value);
        }
        
        this.value = value;
    }
    
}
