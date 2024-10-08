package shining.starj.lostSurvival.Predicates.Conditions;

import lombok.Builder;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Predicates.ConditionInterface;
@Builder
public record TypeCondition(@NotNull String type) implements ConditionInterface {
    @Override
    public boolean is(@NotNull Entity entity) {
        return entity.getType().name().equalsIgnoreCase(type);
    }
}
