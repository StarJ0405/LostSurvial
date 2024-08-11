package shining.starj.lostSurvival.Predicates.Conditions;

import lombok.Builder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import shining.starj.lostSurvival.Predicates.ConditionInterface;

@Builder
public record DistanceCondition(@NotNull CommandSender sender, @Nullable Double minDistance,
                                @Nullable Double maxDistance) implements ConditionInterface {
    // distance=..5 / distance=5 / distance=5..9
    @Override
    public boolean is(@NotNull Entity entity) {
        if (this.minDistance == null && this.maxDistance == null && sender instanceof Entity) return true;
        Double distance = ((Entity) sender).getLocation().distance(entity.getLocation());
        return (this.minDistance == null || this.minDistance.compareTo(distance) <= 0) && (this.maxDistance == null || this.maxDistance.compareTo(distance) >= 0);
    }
}
