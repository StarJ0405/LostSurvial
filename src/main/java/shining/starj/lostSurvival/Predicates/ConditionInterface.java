package shining.starj.lostSurvival.Predicates;

import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public interface ConditionInterface {
    boolean is(@NotNull Entity entity);
}
