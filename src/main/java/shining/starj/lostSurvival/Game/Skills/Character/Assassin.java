package shining.starj.lostSurvival.Game.Skills.Character;

import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper.*;

public class Assassin {
    public final Blade BLADE = new Blade();
    public final Demonic DEMONIC = new Demonic();
    public final Reaper REAPER = new Reaper();
    public final SoulEater SOUL_EATER = new SoulEater();

    public static class Blade extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity0() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity1() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity2() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate1() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }

    public static class Demonic extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity0() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity1() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity2() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate1() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }


    public static class Reaper extends AbstractCharacterSkill {
        public final ShadowDot SHADOW_DOT = new ShadowDot();
        public final Persona PERSONA = new Persona();
        public final SpiritCatch SPIRIT_CATCH = new SpiritCatch();
        public final SpinningDagger SPINNING_DAGGER = new SpinningDagger();
        public final SabelStinger SABEL_STINGER = new SabelStinger();
        public final Evelisto EVELISTO = new Evelisto();
        public final PhantomDancer PHANTOM_DANCER = new PhantomDancer();
        public final DeathSide DEATH_SIDE = new DeathSide();
        public final Distortion DISTORTION = new Distortion();
        public final CallOfKnives CALL_OF_KNIVES = new CallOfKnives();
        public final ShadowStorm SHADOW_STORM = new ShadowStorm();
        public final Blink BLINK = new Blink();
        public final BlackMist BLACK_MIST = new BlackMist();
        public final ShadowTrap SHADOW_TRAP = new ShadowTrap();
        public final LastGraffiti LAST_GRAFFITI = new LastGraffiti();
        public final RageSpear RAGE_SPEAR = new RageSpear();
        public final DancingOfFury DANCING_OF_FURY = new DancingOfFury();

        @Override
        public AbstractSkill getBasic() {
            return SHADOW_DOT;
        }

        @Override
        public AbstractSkill getIdentity0() {
            return PERSONA;
        }

        @Override
        public AbstractSkill getIdentity1() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity2() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate1() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[]{
                    SPIRIT_CATCH, SPINNING_DAGGER, SABEL_STINGER, EVELISTO, PHANTOM_DANCER, DEATH_SIDE, DISTORTION, CALL_OF_KNIVES, SHADOW_STORM, BLINK, BLACK_MIST, SHADOW_TRAP, LAST_GRAFFITI, RAGE_SPEAR, DANCING_OF_FURY
            };
        }
    }

    public static class SoulEater extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity0() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity1() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity2() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate1() {
            return null;
        }

        @Override
        public AbstractSkill getUltimate2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }
}
