package shining.starj.lostSurvival.Game;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Atrributes.Attributes;
import shining.starj.lostSurvival.Core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum Upgrade {
    DAMAGE("피해량", 0.1, 0.1, true), CRITICAL_CHANCE("치명타 확률", 0.08, 0.02, true),//
    COOL_DOWN("스킬 가속", 0.08, 0.02, true), PROJECTILE("투사체", 1, 1), //
    MOVE_SPEED("이동 속도", 0.08f, 0.02f, true) {
        @Override
        public void apply(Player player, int level) {
            PlayerStore playerStore = PlayerStore.getStore(player);
            Attributes.builder().attribute(Attribute.GENERIC_MOVEMENT_SPEED).amount(playerStore.getMoveSpeed()).build().apply(player);
        }
    },//
    MAX_HEALTH("최대 체력", 200, 0.05, false, true) {
        @Override
        public void apply(Player player, int level) {
            PlayerStore playerStore = PlayerStore.getStore(player);
            playerStore.heal(perLevel);
        }
    }, //
    ARMOR("방어력", 0.08, 0.02, true), REGEN("체력 재생", 4, 1), //
    AREA("범위 크기", 0.12, 0.04, true), DURATION("지속시간", 0.12, 0.04, true), //
    PICKUP("획득 반경", 0.3, 0.05, true), EXP("경험치", 0.1, 0.02, true) //
    //
    ;
    protected final String name;
    protected final int maxLevel;
    protected final double perLevel;
    protected final int defaultMaxLevel;
    protected final double defaultPerLevel;
    protected final boolean percent;
    protected final boolean defaultPercent;

    Upgrade(String name, double perLevel, double defaultPerLevel) {
        this(name, perLevel, defaultPerLevel, false);
    }

    Upgrade(String name, double perLevel, double defaultPerLevel, boolean percent) {
        this(name, perLevel, defaultPerLevel, percent, percent);
    }

    Upgrade(String name, double perLevel, double defaultPerLevel, boolean percent, boolean defaultPercent) {
        this.name = name;
        this.maxLevel = 5;
        this.perLevel = perLevel;
        this.defaultMaxLevel = 10;
        this.defaultPerLevel = defaultPerLevel;
        this.percent = percent;
        this.defaultPercent = defaultPercent;
    }

    public long getDefaultUpgradeMoney(long level) {
        return (level + 1) * 100;
    }

    public double getDefaultValue(Player player) {
        try {
            return shining.starj.lostSurvival.DBs.Upgrade.builder().player_name(player.getName()).upgrade(this.name()).build().createTable().getLevel() * getDefaultPerLevel();
        } catch (SQLException ignored) {
        }
        return 0d;
    }

    public ItemStack getItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + this.name);
        List<String> lore = new ArrayList<>();
        String suf = percent ? "%" : "";
        if (level == maxLevel)
            lore.add(ChatColor.GREEN + "최대 레벨");
        else
            lore.add(ChatColor.GREEN + String.format("레벨 %d", level));
        lore.add(ChatColor.WHITE + String.format("%s이 %.0f%s 증가합니다.", this.name, this.perLevel * level * (percent ? 100d : 1d), suf));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getUpgradeItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + this.name);
        List<String> lore = new ArrayList<>();
        String suf = percent ? "%" : "";
        if (level > 0) {
            if (level == maxLevel - 1)
                lore.add(ChatColor.GREEN + String.format("레벨 %d -> MAX ", level));
            else
                lore.add(ChatColor.GREEN + String.format("레벨 %d -> %d ", level, level + 1));
            lore.add(ChatColor.WHITE + String.format("%s : %.0f%s -> %.0f%s", this.name, this.perLevel * level * (percent ? 100d : 1d), suf, this.perLevel * (level + 1) * (percent ? 100d : 1d), suf));
        } else {
            lore.add(ChatColor.GREEN + String.format("레벨 %d -> %d ", 0, 1));
            lore.add(ChatColor.WHITE + String.format("%s : %.0f%s", this.name, this.perLevel * (percent ? 100d : 1d), suf));
        }
        meta.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "type"), PersistentDataType.STRING, "upgrade$" + this.name());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getDefaultItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + this.name);
        List<String> lore = new ArrayList<>();
        String suf = defaultPercent ? "%" : "";
        if (level == defaultMaxLevel)
            lore.add(ChatColor.GREEN + "최대 레벨");
        else
            lore.add(ChatColor.GREEN + String.format("레벨 %d", level));
        lore.add(ChatColor.WHITE + String.format("%s이 %.0f%s 증가합니다.", this.name, this.defaultPerLevel * level * (defaultPercent ? 100d : 1d), suf));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getUpgradeDefaultItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + this.name);
        List<String> lore = new ArrayList<>();
        String suf = defaultPercent ? "%" : "";
        if (level > 0)
            if (level == defaultMaxLevel) {
                lore.add(ChatColor.GREEN + "최대 레벨");
                lore.add(ChatColor.WHITE + String.format("%s : %.0f%s", this.name, this.defaultPerLevel * level * (defaultPercent ? 100d : 1d), suf));
            } else if (level == defaultMaxLevel - 1) {
                lore.add(ChatColor.GREEN + String.format("레벨 %d -> MAX ", level));
                lore.add(ChatColor.WHITE + String.format("%s : %.0f%s -> %.0f%s", this.name, this.defaultPerLevel * level * (defaultPercent ? 100d : 1d), suf, this.defaultPerLevel * (level + 1) * (defaultPercent ? 100d : 1d), suf));
            } else {
                lore.add(ChatColor.GREEN + String.format("레벨 %d -> %d ", level, level + 1));
                lore.add(ChatColor.WHITE + String.format("%s : %.0f%s -> %.0f%s", this.name, this.defaultPerLevel * level * (defaultPercent ? 100d : 1d), suf, this.defaultPerLevel * (level + 1) * (defaultPercent ? 100d : 1d), suf));
            }
        else {
            lore.add(ChatColor.GREEN + String.format("레벨 %d -> %d ", 0, 1));
            lore.add(ChatColor.WHITE + String.format("%s : %.0f%s", this.name, this.defaultPerLevel * (defaultPercent ? 100d : 1d), suf));
        }
        if (level < this.defaultMaxLevel)
            lore.add(ChatColor.YELLOW + String.format("업그레이드 비용 %d 골드", getDefaultUpgradeMoney(level)));
        meta.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "type"), PersistentDataType.STRING, "upgrade$" + this.name());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void apply(Player player, int level) {

    }

    public static Upgrade valueFromKorean(String name) {
        for (Upgrade upgrade : values())
            if (upgrade.name.equalsIgnoreCase(name))
                return upgrade;
        return null;
    }
}
