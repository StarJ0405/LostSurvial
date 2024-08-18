package shining.starj.lostSurvival.Game.Skills;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.lostSurvival.Atrributes.AttributeModifiers;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.Character.*;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class AbstractSkill {
    public final static Warrior WARRIOR = new Warrior();
    public final static Fighter FIGHTER = new Fighter();
    public final static Hunter HUNTER = new Hunter();
    public final static Magician MAGICIAN = new Magician();
    public final static Assassin ASSASSIN = new Assassin();
    public final static Specialist SPECIALIST = new Specialist();
    //
    protected final String name;
    protected final Material type;
    protected final Upgrade main;
    protected final Upgrade[] upgrades;

    public AbstractSkill(String name, Material type, Upgrade main, Upgrade[] upgrades) {
        this.name = name;
        this.type = type;
        this.main = main;
        this.upgrades = upgrades;
    }

    public abstract int getCoolDownTick(int level);

    public abstract boolean Use(Player player, int level);

    public List<String> getLore(int level) {
        List<String> list = new ArrayList<>();
        if (main != null) {
            list.add(ChatColor.GRAY + "");
            list.add(ChatColor.DARK_PURPLE + "진화 : " + ChatColor.WHITE + main.getName());
        }
        StringBuilder builder = new StringBuilder();
        for (Upgrade upgrade : upgrades) {
            if (!builder.isEmpty())
                builder.append(", ");
            builder.append(upgrade.getName());
        }
        if (!builder.isEmpty()) {
            list.add(ChatColor.GRAY + "");
            list.add(ChatColor.GRAY + builder.toString());
        }
        return list;
    }

    public ItemStack getItemStack(int level) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + this.name);
        meta.setLore(getLore(level));
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        AttributeModifiers.builder().attribute(Attribute.GENERIC_ATTACK_DAMAGE).name("damage").amount(0).slot(EquipmentSlot.HAND).uuid(UUID.randomUUID()).operation(AttributeModifier.Operation.ADD_NUMBER).build().apply(item, true);
        AttributeModifiers.builder().attribute(Attribute.GENERIC_ATTACK_SPEED).name("attack_speed").amount(0).slot(EquipmentSlot.HAND).uuid(UUID.randomUUID()).operation(AttributeModifier.Operation.ADD_NUMBER).build().apply(item, true);
        return item;
    }

    public ItemStack upgrade(Player player, int level) {
        ItemStack item = new ItemStack(this.type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        item.setItemMeta(meta);
        return item;
    }

    public abstract List<String> getUpgradeLore(int level);

    public int getMaxLevel() {
        return 5;
    }

    public static void damage(LivingEntity vic, Player att, double damage) {
        if (!CustomEntities.isNpc(vic)) {
            int ndt = vic.getNoDamageTicks();
            vic.setNoDamageTicks(10);
            vic.damage(damage, att);
            vic.setNoDamageTicks(ndt);
            PlayerStore.getStore(att).sendActionBar();
        }
    }
}
