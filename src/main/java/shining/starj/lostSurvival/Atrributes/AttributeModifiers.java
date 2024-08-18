package shining.starj.lostSurvival.Atrributes;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class AttributeModifiers extends Attributes {
    protected final UUID uuid;
    protected final String name;
    protected final Operation operation;
    protected final EquipmentSlot slot;


    public AttributeModifiers(Attribute attribute, double amount, UUID uuid, String name, Operation operation,
                              EquipmentSlot slot) {
        super(attribute, amount);
        this.uuid = uuid;
        this.name = name;
        this.operation = operation;
        this.slot = slot;
    }

    public static AttributeModifiersBuilder builder() {
        return new AttributeModifiersBuilder();
    }

    public static class AttributeModifiersBuilder extends Attributes.AttributesBuilder {
        protected Attribute attribute;
        protected double amount;
        protected UUID uuid;
        protected String name;
        protected Operation operation;
        protected EquipmentSlot slot;

        @Override
        public AttributeModifiersBuilder attribute(Attribute attribute) {
            this.attribute = attribute;
            return this;
        }

        @Override
        public AttributeModifiersBuilder amount(double amount) {
            this.amount = amount;
            return this;
        }

        public AttributeModifiersBuilder uuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public AttributeModifiersBuilder name(String name) {
            this.name = name;
            return this;
        }

        public AttributeModifiersBuilder operation(Operation operation) {
            this.operation = operation;
            return this;
        }

        public AttributeModifiersBuilder slot(EquipmentSlot slot) {
            this.slot = slot;
            return this;
        }

        public AttributeModifiers build() {
            return new AttributeModifiers(attribute, amount, uuid, name, operation, slot);
        }
    }

    private AttributeModifier getAttributeModifier() {
        if (this.slot == null)
            return new AttributeModifier(this.uuid, this.name, this.amount, this.operation);
        else
            return new AttributeModifier(this.uuid, this.name, this.amount, this.operation, this.slot);
    }

    @Override
    public void apply(LivingEntity livingEntity) {
        livingEntity.getAttribute(this.attribute).removeModifier(getAttributeModifier());
        if (amount != 0)
            livingEntity.getAttribute(this.attribute).addModifier(getAttributeModifier());
    }

    public void apply(ItemStack item) {
        apply(item, false);
    }

    public void apply(ItemStack item, boolean force) {
        if (item != null) {
            ItemMeta meta = item.getItemMeta();
            if (amount == 0 && !force)
                meta.removeAttributeModifier(this.attribute, getAttributeModifier());
            else
                meta.addAttributeModifier(this.attribute, getAttributeModifier());
            item.setItemMeta(meta);
        }
    }
}
