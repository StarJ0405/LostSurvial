package shining.starj.lostSurvival.Game.Characters;

import lombok.Getter;
import shining.starj.lostSurvival.Game.Passives.AbstractPassive;

@Getter
public abstract class AbstractCharacter {
    private final AbstractPassive passive;

    public AbstractCharacter(AbstractPassive passive) {
        this.passive = passive;
    }
}
