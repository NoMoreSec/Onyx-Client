package net.onyx.client.misc.attributes.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;
import net.onyx.client.misc.attributes.Attribute;

public class ArmourAttribute extends Attribute {

    public ArmourAttribute(LivingEntity entity) {
        super(entity);
    }
    
    @Override
    public Text getText() {
        return Text.of(String.valueOf(this.getEntity().getArmor()));
    }
}
