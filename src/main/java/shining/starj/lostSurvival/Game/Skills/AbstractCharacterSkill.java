package shining.starj.lostSurvival.Game.Skills;

import org.bukkit.inventory.ItemStack;
import shining.starj.lostSurvival.Game.Passives.AbstractPassive;

public abstract class AbstractCharacterSkill {
    public abstract AbstractSkill getBasic();

    public abstract AbstractSkill getIdentity();

    public abstract Engraving getEngraving1();

    public abstract Engraving getEngraving2();

    public abstract AbstractSkill[] values();

    public static abstract class Engraving {
        public abstract AbstractPassive getPassive();

        public abstract AbstractSkill getIdentity();

        public abstract AbstractSkill getUltimate();

        public abstract ItemStack getChoice();
    }
}
