package shining.starj.lostSurvival.Game.Skills.Characters;


import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;

public class Fighter {
    public final BattleMaster BATTLE_MASTER = new BattleMaster();
    public final Infighter INFIGHTER = new Infighter();
    public final SoulMaster SOUL_MASTER = new SoulMaster();
    public final LanceMaster LANCE_MASTER = new LanceMaster();
    public final Striker STRIKER = new Striker();
    public final Breaker BREAKER = new Breaker();

    public final static class BattleMaster extends AbstractCharacterSkill {
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

    public final static class Infighter extends AbstractCharacterSkill {
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

    public final static class SoulMaster extends AbstractCharacterSkill {
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

    public final static class LanceMaster extends AbstractCharacterSkill {
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

    public final static class Striker extends AbstractCharacterSkill {
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

    public final static class Breaker extends AbstractCharacterSkill {
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
