package shining.starj.lostSurvival.Game;

import lombok.Getter;
import shining.starj.lostSurvival.Game.Passives.AbstractPassive;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;

@Getter
public enum Character {
    // 슈사이어
    DESTROYER("디스트로이어", null, 2000, 0.1f, AbstractSkill.WARRIOR.DESTROYER.getBasic(), AbstractSkill.WARRIOR.DESTROYER.getIdentity0(), AbstractSkill.WARRIOR.DESTROYER.getIdentity1(), AbstractSkill.WARRIOR.DESTROYER.getIdentity2(), AbstractSkill.WARRIOR.DESTROYER.getUltimate1(), AbstractSkill.WARRIOR.DESTROYER.getUltimate2(), AbstractSkill.WARRIOR.DESTROYER.values()), //
    WARLORD("워로드", null, 2000, 0.1f, AbstractSkill.WARRIOR.WARLORD.getBasic(), AbstractSkill.WARRIOR.WARLORD.getIdentity0(), AbstractSkill.WARRIOR.WARLORD.getIdentity1(), AbstractSkill.WARRIOR.WARLORD.getIdentity2(), AbstractSkill.WARRIOR.WARLORD.getUltimate1(), AbstractSkill.WARRIOR.WARLORD.getUltimate2(), AbstractSkill.WARRIOR.WARLORD.values()), //
    BERSERKER("버서커", null, 2000, 0.1f, AbstractSkill.WARRIOR.BERSERKER.getBasic(), AbstractSkill.WARRIOR.BERSERKER.getIdentity0(), AbstractSkill.WARRIOR.BERSERKER.getIdentity1(), AbstractSkill.WARRIOR.BERSERKER.getIdentity2(), AbstractSkill.WARRIOR.BERSERKER.getUltimate1(), AbstractSkill.WARRIOR.BERSERKER.getUltimate2(), AbstractSkill.WARRIOR.BERSERKER.values()), //
    HOLY_KNIGHT("홀리나이트", null, 2000, 0.1f, AbstractSkill.WARRIOR.HOLY_KNIGHT.getBasic(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getIdentity0(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getIdentity1(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getIdentity2(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getUltimate1(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getUltimate2(), AbstractSkill.WARRIOR.HOLY_KNIGHT.values()), //
    SLAYER("슬레이어", null, 2000, 0.1f, AbstractSkill.WARRIOR.SLAYER.getBasic(), AbstractSkill.WARRIOR.SLAYER.getIdentity0(), AbstractSkill.WARRIOR.SLAYER.getIdentity1(), AbstractSkill.WARRIOR.SLAYER.getIdentity2(), AbstractSkill.WARRIOR.SLAYER.getUltimate1(), AbstractSkill.WARRIOR.SLAYER.getUltimate2(), AbstractSkill.WARRIOR.SLAYER.values()), //
    // 애니츠
    BATTLE_MASTER("배틀마스터", null, 1500, 0.1f, AbstractSkill.FIGHTER.BATTLE_MASTER.getBasic(), AbstractSkill.FIGHTER.BATTLE_MASTER.getIdentity0(), AbstractSkill.FIGHTER.BATTLE_MASTER.getIdentity1(), AbstractSkill.FIGHTER.BATTLE_MASTER.getIdentity2(), AbstractSkill.FIGHTER.BATTLE_MASTER.getUltimate1(), AbstractSkill.FIGHTER.BATTLE_MASTER.getUltimate2(), AbstractSkill.FIGHTER.BATTLE_MASTER.values()), //
    INFIGHTER("인파이터", null, 1500, 0.1f, AbstractSkill.FIGHTER.INFIGHTER.getBasic(), AbstractSkill.FIGHTER.INFIGHTER.getIdentity0(), AbstractSkill.FIGHTER.INFIGHTER.getIdentity1(), AbstractSkill.FIGHTER.INFIGHTER.getIdentity2(), AbstractSkill.FIGHTER.INFIGHTER.getUltimate1(), AbstractSkill.FIGHTER.INFIGHTER.getUltimate2(), AbstractSkill.FIGHTER.INFIGHTER.values()), //
    SOUL_MASTER("기공사", null, 1500, 0.1f, AbstractSkill.FIGHTER.SOUL_MASTER.getBasic(), AbstractSkill.FIGHTER.SOUL_MASTER.getIdentity0(), AbstractSkill.FIGHTER.SOUL_MASTER.getIdentity1(), AbstractSkill.FIGHTER.SOUL_MASTER.getIdentity2(), AbstractSkill.FIGHTER.SOUL_MASTER.getUltimate1(), AbstractSkill.FIGHTER.SOUL_MASTER.getUltimate2(), AbstractSkill.FIGHTER.SOUL_MASTER.values()), //
    LANCE_MASTER("창술사", null, 1500, 0.1f, AbstractSkill.FIGHTER.LANCE_MASTER.getBasic(), AbstractSkill.FIGHTER.LANCE_MASTER.getIdentity0(), AbstractSkill.FIGHTER.LANCE_MASTER.getIdentity1(), AbstractSkill.FIGHTER.LANCE_MASTER.getIdentity2(), AbstractSkill.FIGHTER.LANCE_MASTER.getUltimate1(), AbstractSkill.FIGHTER.LANCE_MASTER.getUltimate2(), AbstractSkill.FIGHTER.LANCE_MASTER.values()), //
    STRIKER("스트라이커", null, 1500, 0.1f, AbstractSkill.FIGHTER.STRIKER.getBasic(), AbstractSkill.FIGHTER.STRIKER.getIdentity0(), AbstractSkill.FIGHTER.STRIKER.getIdentity1(), AbstractSkill.FIGHTER.STRIKER.getIdentity2(), AbstractSkill.FIGHTER.STRIKER.getUltimate1(), AbstractSkill.FIGHTER.STRIKER.getUltimate2(), AbstractSkill.FIGHTER.STRIKER.values()), //
    BREAKER("브레이커", null, 1500, 0.1f, AbstractSkill.FIGHTER.BREAKER.getBasic(), AbstractSkill.FIGHTER.BREAKER.getIdentity0(), AbstractSkill.FIGHTER.BREAKER.getIdentity1(), AbstractSkill.FIGHTER.BREAKER.getIdentity2(), AbstractSkill.FIGHTER.BREAKER.getUltimate1(), AbstractSkill.FIGHTER.BREAKER.getUltimate2(), AbstractSkill.FIGHTER.BREAKER.values()), //
    // 아르덴타인
    DEVIL_HUNTER("데빌헌터", null, 1000, 0.1f, AbstractSkill.HUNTER.DEVIL_HUNTER.getBasic(), AbstractSkill.HUNTER.DEVIL_HUNTER.getIdentity0(), AbstractSkill.HUNTER.DEVIL_HUNTER.getIdentity1(), AbstractSkill.HUNTER.DEVIL_HUNTER.getIdentity2(), AbstractSkill.HUNTER.DEVIL_HUNTER.getUltimate1(), AbstractSkill.HUNTER.DEVIL_HUNTER.getUltimate2(), AbstractSkill.HUNTER.DEVIL_HUNTER.values()), //
    BLASTER("블래스터", null, 1000, 0.1f, AbstractSkill.HUNTER.BLASTER.getBasic(), AbstractSkill.HUNTER.BLASTER.getIdentity0(), AbstractSkill.HUNTER.BLASTER.getIdentity1(), AbstractSkill.HUNTER.BLASTER.getIdentity2(), AbstractSkill.HUNTER.BLASTER.getUltimate1(), AbstractSkill.HUNTER.BLASTER.getUltimate2(), AbstractSkill.HUNTER.BLASTER.values()), //
    HAWKEYE("호크아이", null, 1000, 0.1f, AbstractSkill.HUNTER.HAWKEYE.getBasic(), AbstractSkill.HUNTER.HAWKEYE.getIdentity0(), AbstractSkill.HUNTER.HAWKEYE.getIdentity1(), AbstractSkill.HUNTER.HAWKEYE.getIdentity2(), AbstractSkill.HUNTER.HAWKEYE.getUltimate1(), AbstractSkill.HUNTER.HAWKEYE.getUltimate2(), AbstractSkill.HUNTER.HAWKEYE.values()), //
    SCOUTER("스카우터", null, 1000, 0.1f, AbstractSkill.HUNTER.SCOUTER.getBasic(), AbstractSkill.HUNTER.SCOUTER.getIdentity0(), AbstractSkill.HUNTER.SCOUTER.getIdentity1(), AbstractSkill.HUNTER.SCOUTER.getIdentity2(), AbstractSkill.HUNTER.SCOUTER.getUltimate1(), AbstractSkill.HUNTER.SCOUTER.getUltimate2(), AbstractSkill.HUNTER.SCOUTER.values()), //
    GUNSLINGER("건슬링어", null, 1000, 0.1f, AbstractSkill.HUNTER.GUNSLINGER.getBasic(), AbstractSkill.HUNTER.GUNSLINGER.getIdentity0(), AbstractSkill.HUNTER.GUNSLINGER.getIdentity1(), AbstractSkill.HUNTER.GUNSLINGER.getIdentity2(), AbstractSkill.HUNTER.GUNSLINGER.getUltimate1(), AbstractSkill.HUNTER.GUNSLINGER.getUltimate2(), AbstractSkill.HUNTER.GUNSLINGER.values()), //
    // 실린
    BARD("바드", null, 1000, 0.1f, AbstractSkill.MAGICIAN.BARD.getBasic(), AbstractSkill.MAGICIAN.BARD.getIdentity0(), AbstractSkill.MAGICIAN.BARD.getIdentity1(), AbstractSkill.MAGICIAN.BARD.getIdentity2(), AbstractSkill.MAGICIAN.BARD.getUltimate1(), AbstractSkill.MAGICIAN.BARD.getUltimate2(), AbstractSkill.MAGICIAN.BARD.values()), //
    SUMMONER("서모너", null, 1000, 0.1f, AbstractSkill.MAGICIAN.SUMMONER.getBasic(), AbstractSkill.MAGICIAN.SUMMONER.getIdentity0(), AbstractSkill.MAGICIAN.SUMMONER.getIdentity1(), AbstractSkill.MAGICIAN.SUMMONER.getIdentity2(), AbstractSkill.MAGICIAN.SUMMONER.getUltimate1(), AbstractSkill.MAGICIAN.SUMMONER.getUltimate2(), AbstractSkill.MAGICIAN.SUMMONER.values()), //
    ARCANA("아르카나", null, 1000, 0.1f, AbstractSkill.MAGICIAN.ARCANA.getBasic(), AbstractSkill.MAGICIAN.ARCANA.getIdentity0(), AbstractSkill.MAGICIAN.ARCANA.getIdentity1(), AbstractSkill.MAGICIAN.ARCANA.getIdentity2(), AbstractSkill.MAGICIAN.ARCANA.getUltimate1(), AbstractSkill.MAGICIAN.ARCANA.getUltimate2(), AbstractSkill.MAGICIAN.ARCANA.values()), //
    SORCERESS("소서리스", null, 1000, 0.1f, AbstractSkill.MAGICIAN.SORCERESS.getBasic(), AbstractSkill.MAGICIAN.SORCERESS.getIdentity0(), AbstractSkill.MAGICIAN.SORCERESS.getIdentity1(), AbstractSkill.MAGICIAN.SORCERESS.getIdentity2(), AbstractSkill.MAGICIAN.SORCERESS.getUltimate1(), AbstractSkill.MAGICIAN.SORCERESS.getUltimate2(), AbstractSkill.MAGICIAN.SORCERESS.values()), //
    // 데런
    DEMONIC("데모닉", null, 1200, 0.1f, AbstractSkill.ASSASSIN.DEMONIC.getBasic(), AbstractSkill.ASSASSIN.DEMONIC.getIdentity0(), AbstractSkill.ASSASSIN.DEMONIC.getIdentity1(), AbstractSkill.ASSASSIN.DEMONIC.getIdentity2(), AbstractSkill.ASSASSIN.DEMONIC.getUltimate1(), AbstractSkill.ASSASSIN.DEMONIC.getUltimate2(), AbstractSkill.ASSASSIN.DEMONIC.values()), //
    BLADE("블레이드", null, 1200, 0.1f, AbstractSkill.ASSASSIN.BLADE.getBasic(), AbstractSkill.ASSASSIN.BLADE.getIdentity0(), AbstractSkill.ASSASSIN.BLADE.getIdentity1(), AbstractSkill.ASSASSIN.BLADE.getIdentity2(), AbstractSkill.ASSASSIN.BLADE.getUltimate1(), AbstractSkill.ASSASSIN.BLADE.getUltimate2(), AbstractSkill.ASSASSIN.BLADE.values()), //
    REAPER("리퍼", null, 750, 0.1f, AbstractSkill.ASSASSIN.REAPER.getBasic(), AbstractSkill.ASSASSIN.REAPER.getIdentity0(), AbstractSkill.ASSASSIN.REAPER.getIdentity1(), AbstractSkill.ASSASSIN.REAPER.getIdentity2(), AbstractSkill.ASSASSIN.REAPER.getUltimate1(), AbstractSkill.ASSASSIN.REAPER.getUltimate2(), AbstractSkill.ASSASSIN.REAPER.values()), //
    SOUL_EATER("소울이터", null, 1000, 0.1f, AbstractSkill.ASSASSIN.SOUL_EATER.getBasic(), AbstractSkill.ASSASSIN.SOUL_EATER.getIdentity0(), AbstractSkill.ASSASSIN.SOUL_EATER.getIdentity1(), AbstractSkill.ASSASSIN.SOUL_EATER.getIdentity2(), AbstractSkill.ASSASSIN.SOUL_EATER.getUltimate1(), AbstractSkill.ASSASSIN.SOUL_EATER.getUltimate2(), AbstractSkill.ASSASSIN.SOUL_EATER.values()), //
    // 스페셜리스트
    ARTIST("도화가", null, 1000, 0.1f, AbstractSkill.SPECIALIST.ARTIST.getBasic(), AbstractSkill.SPECIALIST.ARTIST.getIdentity0(), AbstractSkill.SPECIALIST.ARTIST.getIdentity1(), AbstractSkill.SPECIALIST.ARTIST.getIdentity2(), AbstractSkill.SPECIALIST.ARTIST.getUltimate1(), AbstractSkill.SPECIALIST.ARTIST.getUltimate2(), AbstractSkill.SPECIALIST.ARTIST.values()), //
    AEROMANCER("기상술사", null, 1000, 0.1f, AbstractSkill.SPECIALIST.AEROMANCER.getBasic(), AbstractSkill.SPECIALIST.AEROMANCER.getIdentity0(), AbstractSkill.SPECIALIST.AEROMANCER.getIdentity1(), AbstractSkill.SPECIALIST.AEROMANCER.getIdentity2(), AbstractSkill.SPECIALIST.AEROMANCER.getUltimate1(), AbstractSkill.SPECIALIST.AEROMANCER.getUltimate2(), AbstractSkill.SPECIALIST.AEROMANCER.values()) //
    //
    ;
    private final String name;
    private final AbstractPassive passive;
    private final double maxHealth; // 기본체력
    private final float moveSpeed; // 기본 이동속도
    private final AbstractSkill basic;
    private final AbstractSkill identity0;
    private final AbstractSkill identity1;
    private final AbstractSkill identity2;
    private final AbstractSkill ultimate1;
    private final AbstractSkill ultimate2;
    private final AbstractSkill[] skills;

    Character(String name, AbstractPassive passive, double maxHealth, float moveSpeed, AbstractSkill basic, AbstractSkill identity0, AbstractSkill identity1, AbstractSkill identity2, AbstractSkill ultimate1, AbstractSkill ultimate2, AbstractSkill[] skills) {
        this.name = name;
        this.passive = passive;
        this.maxHealth = maxHealth;
        this.moveSpeed = moveSpeed;
        this.basic = basic;
        this.identity0 = identity0;
        this.identity1 = identity1;
        this.identity2 = identity2;
        this.ultimate1 = ultimate1;
        this.ultimate2 = ultimate2;
        this.skills = skills;
    }
}
