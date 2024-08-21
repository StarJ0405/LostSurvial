package shining.starj.lostSurvival.Game.Skills;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Atrributes.AttributeModifiers;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.Characters.*;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public abstract class AbstractSkill {
    private static final List<AbstractSkill> list = new ArrayList<>();
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
        list.add(this);
    }

    public abstract int getCoolDownTick(int level);

    public abstract boolean Use(Player player, int level, boolean upgrade);

    public List<String> getLore(int level, boolean upgraded) {
        List<String> list = new ArrayList<>();
        if (main != null) {
            list.add(ChatColor.GRAY + "");
            list.add(ChatColor.DARK_PURPLE + "진화 : " + ChatColor.WHITE + main.getName());
        }
        StringBuilder builder = new StringBuilder();
        for (Upgrade upgrade : upgrades) {
            if (!builder.isEmpty()) builder.append(", ");
            builder.append(upgrade.getName());
        }
        if (!builder.isEmpty()) {
            list.add(ChatColor.GRAY + "");
            list.add(ChatColor.GRAY + builder.toString());
        }
        return list;
    }

    public ItemStack getItemStack(int level, boolean upgraded) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + this.name);
        List<String> lore = getLore(level, upgraded);
        if (level > 0) {
            lore.addFirst(ChatColor.GRAY + String.format("재사용 대기시간 : %.2fs", getCoolDownTick(level) / 20d));
            if (main != null) if (upgraded) lore.addFirst(ChatColor.DARK_PURPLE + "강화");
            else if (level == getMaxLevel()) lore.addFirst(ChatColor.GREEN + "최대 레벨");
            else lore.addFirst(ChatColor.GREEN + String.format("레벨 %d", level));
        }
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
        AttributeModifiers.builder().attribute(Attribute.GENERIC_ATTACK_DAMAGE).name("damage").amount(0).slot(EquipmentSlot.HAND).uuid(UUID.randomUUID()).operation(AttributeModifier.Operation.ADD_NUMBER).build().apply(item, true);
        AttributeModifiers.builder().attribute(Attribute.GENERIC_ATTACK_SPEED).name("attack_speed").amount(0).slot(EquipmentSlot.HAND).uuid(UUID.randomUUID()).operation(AttributeModifier.Operation.ADD_NUMBER).build().apply(item, true);
        return item;
    }

    public ItemStack getUpgradeItemStack(int level, boolean upgraded) {
        ItemStack item = new ItemStack(this.type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + this.name);
        List<String> lore = getUpgradeLore(level, upgraded);
        if (upgraded) lore.addFirst(ChatColor.GREEN + "일반 -> 강화 ");
        else if (level == getMaxLevel() - 1) lore.addFirst(ChatColor.GREEN + String.format("레벨 %d -> MAX ", level));
        else lore.addFirst(ChatColor.GREEN + String.format("레벨 %d -> %d ", level, level + 1));
        meta.setLore(lore);
        meta.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "type"), PersistentDataType.STRING, "skill$" + this.name);
        item.setItemMeta(meta);
        return item;
    }

    public abstract List<String> getUpgradeLore(int level, boolean upgraded);

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

    public static List<AbstractSkill> values() {
        return new ArrayList<>(list);
    }

    public static AbstractSkill valueOf(String name) {
        for (AbstractSkill skill : list)
            if (skill.name.equalsIgnoreCase(name)) return skill;
        return null;
    }

    public int getCoolDown(Player player, int level) {
        return (int) (getCoolDownTick(level) * PlayerStore.getStore(player).getCoolDown());
    }
}
