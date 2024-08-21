package shining.starj.lostSurvival.Game.Skills.Characters;

import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;

public class Magician {
    public final Bard BARD = new Bard();
    public final Summoner SUMMONER = new Summoner();
    public final Arcana ARCANA = new Arcana();
    public final Sorceress SORCERESS = new Sorceress();

    public final static class Bard extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity() {
            return null;
        }

        @Override
        public Engraving getEngraving1() {
            return null;
        }

        @Override
        public Engraving getEngraving2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }

    public final static class Summoner extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity() {
            return null;
        }

        @Override
        public Engraving getEngraving1() {
            return null;
        }

        @Override
        public Engraving getEngraving2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }

    public final static class Arcana extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity() {
            return null;
        }

        @Override
        public Engraving getEngraving1() {
            return null;
        }

        @Override
        public Engraving getEngraving2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }

    public final static class Sorceress extends AbstractCharacterSkill {
        @Override
        public AbstractSkill getBasic() {
            return null;
        }

        @Override
        public AbstractSkill getIdentity() {
            return null;
        }

        @Override
        public Engraving getEngraving1() {
            return null;
        }

        @Override
        public Engraving getEngraving2() {
            return null;
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[0];
        }
    }
}
