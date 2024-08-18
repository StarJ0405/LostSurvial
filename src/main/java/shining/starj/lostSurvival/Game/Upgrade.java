package shining.starj.lostSurvival.Game;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.lostSurvival.DBs.UpgradeTable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Getter
public enum Upgrade {
    DAMAGE("피해량", 0.1, 0.1), CRITICAL_CHANCE("치명타 확률", 0.08, 0.02),//
    COOL_DOWN("스킬 가속", 0.08, 0.02), PROJECTILE("투사체", 1, 1), //
    MOVE_SPEED("이동 속도", 0.08f, 0.02f), MAX_HEALTH("최대 체력", 200, 0.05), //
    ARMOR("방어력", 0.08, 0.05), REGEN("체력 재생", 4, 1), //
    AREA("범위 크기", 0.12, 0.04), DURATION("지속시간", 0.12, 0.04), //
    PICKUP("획득 반경", 0.3, 0.05), EXP("경험치", 0.1, 0.02) //
    //
    ;
    private final String name;
    private final int maxLevel;
    private final double perLevel;
    private final int defaultMaxLevel;
    private final double defaultPerLevel;

    Upgrade(String name, double perLevel, double defaultPerLevel) {
        this.name = name;
        this.maxLevel = 5;
        this.perLevel = perLevel;
        this.defaultMaxLevel = 10;
        this.defaultPerLevel = defaultPerLevel;
    }

    public double getDefaultUpgradeMoney(int level) {
        return level * 100;
    }

    public double getDefaultValue(Player player) {
        double defaultUpgrade = 0d;
        try {
            List<UpgradeTable> tables = UpgradeTable.builder().upgrade(Upgrade.DAMAGE.name()).player(player.getName()).build().selectAll().stream().map(table -> (UpgradeTable) table).toList();
            for (UpgradeTable table : tables)
                defaultUpgrade += table.getLevel() * Upgrade.DAMAGE.getDefaultPerLevel();
        } catch (SQLException ignored) {

        }
        return defaultUpgrade;
    }

    public ItemStack getItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + String.format("%s이 %.0f 증가합니다.", this.name, this.perLevel * level));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getDefaultItemStack(int level) {
        ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.WHITE + String.format("%s이 %.0f 증가합니다.", this.name, this.defaultPerLevel * level));
        if (level < this.defaultMaxLevel - 1)
            lore.add(ChatColor.YELLOW + String.format("업그레이드 비용 %.0f 골드", getDefaultUpgradeMoney(level)));
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
