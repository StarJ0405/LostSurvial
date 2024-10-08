package shining.starj.lostSurvival.Predicates.Conditions;

import lombok.Builder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shining.starj.lostSurvival.Predicates.ConditionInterface;
import shining.starj.lostSurvival.Systems.ScoreboardStore;

@Builder
public record ScoreCondition(@NotNull String name, @Nullable Integer min,
                             @Nullable Integer max) implements ConditionInterface {
    @Override
    public boolean is(@NotNull Entity entity) {
        if (min == null && max == null) return true;
        if (entity instanceof Player player) {
            Objective objective = player.getScoreboard().getObjective(name);
            int value = objective.getScore(player.getName()).getScore();
            return (min == null || value >= min) && (max == null || value <= max);
        } else {
            Objective objective = ScoreboardStore.getInstance().getObjective(name);
            int value = objective.getScore(entity.getUniqueId().toString()).getScore();
            return (min == null || value >= min) && (max == null || value <= max);
        }
    }
}
