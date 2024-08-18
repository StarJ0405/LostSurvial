package shining.starj.lostSurvival.Game.Skills;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import shining.starj.lostSurvival.Game.PlayerStore;

@Getter
public class SkillInfo {
    private final AbstractSkill skill;
    private int level;

    @Builder
    public SkillInfo(AbstractSkill skill) {
        this.skill = skill;
        this.level = 1;
    }

    public void setLevel(int level) {
        this.level = Math.min(level, skill.getMaxLevel());
    }

    public ItemStack getItemStack() {
        return skill.getItemStack(level);
    }

    public void Use(Player player) {
        if (skill != null && !player.hasCooldown(skill.getType()) && skill.Use(player, level))
            player.setCooldown(skill.getType(), (int) (skill.getCoolDownTick(level) * PlayerStore.getStore(player).getCoolDown()));
    }
}
