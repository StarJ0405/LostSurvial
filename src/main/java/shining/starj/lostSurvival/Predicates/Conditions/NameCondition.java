package shining.starj.lostSurvival.Predicates.Conditions;

import lombok.Builder;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Predicates.ConditionInterface;

import java.util.Optional;
@Builder
public record NameCondition(@NotNull String name) implements ConditionInterface {
    @Override
    public boolean is(@NotNull Entity entity) {
        return entity instanceof Player ? ((Player) entity).getName().equals(name) : Optional.ofNullable(entity.getCustomName()).orElse("").equals(name);
    }
}
