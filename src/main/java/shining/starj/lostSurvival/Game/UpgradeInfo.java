package shining.starj.lostSurvival.Game;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

@Getter
public class UpgradeInfo {
    private final Upgrade upgrade;
    private int level;

    @Builder
    public UpgradeInfo(Upgrade upgrade) {
        this.upgrade = upgrade;
        this.level = 1;
    }

    public void setLevel(int level) {
        this.level = Math.min(level, upgrade.getMaxLevel());
    }

    public double getValue() {
        return upgrade.getPerLevel() * level;
    }

    public ItemStack getItemStack() {
        return upgrade.getItemStack(level);
    }

    public ItemStack getDefaultItemStack() {
        return upgrade.getDefaultItemStack(level);
    }
}
