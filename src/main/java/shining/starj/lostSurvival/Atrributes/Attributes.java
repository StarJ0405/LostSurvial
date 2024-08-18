package shining.starj.lostSurvival.Atrributes;

import lombok.Builder;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;

public class Attributes {
    protected final Attribute attribute;
    protected final double amount;

    @Builder
    public Attributes(Attribute attribute, double amount) {
        this.attribute = attribute;
        this.amount = amount;
    }

    public void apply(LivingEntity livingEntity) {
        livingEntity.getAttribute(this.attribute).setBaseValue(this.amount);
    }

}
