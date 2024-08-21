package shining.starj.lostSurvival.Game.Skills.Characters;

import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;

public class Specialist {
    public final Artist ARTIST = new Artist();
    public final Aeromancer AEROMANCER = new Aeromancer();

    public final static class Artist extends AbstractCharacterSkill {
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

    public final static class Aeromancer extends AbstractCharacterSkill {
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
