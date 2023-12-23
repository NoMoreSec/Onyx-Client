package net.onyx.client.misc.attributes.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.onyx.client.misc.attributes.Attribute;

public class NameAttribute extends Attribute {

    public NameAttribute(LivingEntity entity) {
        super(entity);
    }

    @Override
    public Text getText() {
        return this.getEntity().getName();
    }    
}
