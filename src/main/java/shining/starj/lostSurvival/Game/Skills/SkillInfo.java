package shining.starj.lostSurvival.Game.Skills;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@Getter
public class SkillInfo {
    private final AbstractSkill skill;
    @Setter
    private boolean upgraded;
    private int level;

    @Builder
    public SkillInfo(AbstractSkill skill) {
        this.skill = skill;
        this.upgraded = false;
        this.level = 1;
    }

    public void setLevel(int level) {
        this.level = Math.min(level, skill.getMaxLevel());
    }

    public ItemStack getItemStack() {
        return skill.getItemStack(level, upgraded);
    }

    public ItemStack getUpgradeItemStack() {
        return getUpgradeItemStack(false);
    }

    public ItemStack getUpgradeItemStack(boolean upgraded) {
        return skill.getUpgradeItemStack(level, upgraded);
    }

    public void Use(Player player) {
        Use(player, skill, level, upgraded);
    }

    // 아덴 및 궁극기용
    public static void Use(Player player, AbstractSkill skill, int level, boolean upgrade) {
        if (skill != null && !player.hasCooldown(skill.getType()) && skill.Use(player, level, upgrade))
            player.setCooldown(skill.getType(), skill.getCoolDown(player, level));
    }

    public static void Use(Player player, AbstractSkill skill, int level) {
        if (skill != null && !player.hasCooldown(skill.getType()) && skill.Use(player, level, false))
            player.setCooldown(skill.getType(), skill.getCoolDown(player, level));
    }
}
