package shining.starj.lostSurvival.Game;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.lostSurvival.Game.Passives.AbstractPassive;
import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;

import java.util.List;

@Getter
public enum Character {
    // 슈사이어
    DESTROYER("디스트로이어", null, 2000, 0.1f, AbstractSkill.WARRIOR.DESTROYER.getBasic(), AbstractSkill.WARRIOR.DESTROYER.getIdentity(), AbstractSkill.WARRIOR.DESTROYER.getEngraving1(), AbstractSkill.WARRIOR.DESTROYER.getEngraving2(), AbstractSkill.WARRIOR.DESTROYER.values()), //
    WARLORD("워로드", null, 2000, 0.1f, AbstractSkill.WARRIOR.WARLORD.getBasic(), AbstractSkill.WARRIOR.WARLORD.getIdentity(), AbstractSkill.WARRIOR.WARLORD.getEngraving1(), AbstractSkill.WARRIOR.WARLORD.getEngraving2(), AbstractSkill.WARRIOR.WARLORD.values()), //
    BERSERKER("버서커", null, 2000, 0.1f, AbstractSkill.WARRIOR.BERSERKER.getBasic(), AbstractSkill.WARRIOR.BERSERKER.getIdentity(), AbstractSkill.WARRIOR.BERSERKER.getEngraving1(), AbstractSkill.WARRIOR.BERSERKER.getEngraving2(), AbstractSkill.WARRIOR.BERSERKER.values()), //
    HOLY_KNIGHT("홀리나이트", null, 2000, 0.1f, AbstractSkill.WARRIOR.HOLY_KNIGHT.getBasic(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getIdentity(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getEngraving1(), AbstractSkill.WARRIOR.HOLY_KNIGHT.getEngraving2(), AbstractSkill.WARRIOR.HOLY_KNIGHT.values()), //
    SLAYER("슬레이어", null, 2000, 0.1f, AbstractSkill.WARRIOR.SLAYER.getBasic(), AbstractSkill.WARRIOR.SLAYER.getIdentity(), AbstractSkill.WARRIOR.SLAYER.getEngraving1(), AbstractSkill.WARRIOR.SLAYER.getEngraving2(), AbstractSkill.WARRIOR.SLAYER.values()), //
    // 애니츠
    BATTLE_MASTER("배틀마스터", null, 1500, 0.1f, AbstractSkill.FIGHTER.BATTLE_MASTER.getBasic(), AbstractSkill.FIGHTER.BATTLE_MASTER.getIdentity(), AbstractSkill.FIGHTER.BATTLE_MASTER.getEngraving1(), AbstractSkill.FIGHTER.BATTLE_MASTER.getEngraving2(), AbstractSkill.FIGHTER.BATTLE_MASTER.values()), //
    INFIGHTER("인파이터", null, 1500, 0.1f, AbstractSkill.FIGHTER.INFIGHTER.getBasic(), AbstractSkill.FIGHTER.INFIGHTER.getIdentity(), AbstractSkill.FIGHTER.INFIGHTER.getEngraving1(), AbstractSkill.FIGHTER.INFIGHTER.getEngraving2(), AbstractSkill.FIGHTER.INFIGHTER.values()), //
    SOUL_MASTER("기공사", null, 1500, 0.1f, AbstractSkill.FIGHTER.SOUL_MASTER.getBasic(), AbstractSkill.FIGHTER.SOUL_MASTER.getIdentity(), AbstractSkill.FIGHTER.SOUL_MASTER.getEngraving1(), AbstractSkill.FIGHTER.SOUL_MASTER.getEngraving2(), AbstractSkill.FIGHTER.SOUL_MASTER.values()), //
    LANCE_MASTER("창술사", null, 1500, 0.1f, AbstractSkill.FIGHTER.LANCE_MASTER.getBasic(), AbstractSkill.FIGHTER.LANCE_MASTER.getIdentity(), AbstractSkill.FIGHTER.LANCE_MASTER.getEngraving1(), AbstractSkill.FIGHTER.LANCE_MASTER.getEngraving2(), AbstractSkill.FIGHTER.LANCE_MASTER.values()), //
    STRIKER("스트라이커", null, 1500, 0.1f, AbstractSkill.FIGHTER.STRIKER.getBasic(), AbstractSkill.FIGHTER.STRIKER.getIdentity(), AbstractSkill.FIGHTER.STRIKER.getEngraving1(), AbstractSkill.FIGHTER.STRIKER.getEngraving2(), AbstractSkill.FIGHTER.STRIKER.values()), //
    BREAKER("브레이커", null, 1500, 0.1f, AbstractSkill.FIGHTER.BREAKER.getBasic(), AbstractSkill.FIGHTER.BREAKER.getIdentity(), AbstractSkill.FIGHTER.BREAKER.getEngraving1(), AbstractSkill.FIGHTER.BREAKER.getEngraving2(), AbstractSkill.FIGHTER.BREAKER.values()), //
    // 아르덴타인
    DEVIL_HUNTER("데빌헌터", null, 1000, 0.1f, AbstractSkill.HUNTER.DEVIL_HUNTER.getBasic(), AbstractSkill.HUNTER.DEVIL_HUNTER.getIdentity(), AbstractSkill.HUNTER.DEVIL_HUNTER.getEngraving1(), AbstractSkill.HUNTER.DEVIL_HUNTER.getEngraving2(), AbstractSkill.HUNTER.DEVIL_HUNTER.values()), //
    BLASTER("블래스터", null, 1000, 0.1f, AbstractSkill.HUNTER.BLASTER.getBasic(), AbstractSkill.HUNTER.BLASTER.getIdentity(), AbstractSkill.HUNTER.BLASTER.getEngraving1(), AbstractSkill.HUNTER.BLASTER.getEngraving2(), AbstractSkill.HUNTER.BLASTER.values()), //
    HAWKEYE("호크아이", null, 1000, 0.1f, AbstractSkill.HUNTER.HAWKEYE.getBasic(), AbstractSkill.HUNTER.HAWKEYE.getIdentity(), AbstractSkill.HUNTER.HAWKEYE.getEngraving1(), AbstractSkill.HUNTER.HAWKEYE.getEngraving2(), AbstractSkill.HUNTER.HAWKEYE.values()), //
    SCOUTER("스카우터", null, 1000, 0.1f, AbstractSkill.HUNTER.SCOUTER.getBasic(), AbstractSkill.HUNTER.SCOUTER.getIdentity(), AbstractSkill.HUNTER.SCOUTER.getEngraving1(), AbstractSkill.HUNTER.SCOUTER.getEngraving2(), AbstractSkill.HUNTER.SCOUTER.values()), //
    GUNSLINGER("건슬링어", null, 1000, 0.1f, AbstractSkill.HUNTER.GUNSLINGER.getBasic(), AbstractSkill.HUNTER.GUNSLINGER.getIdentity(), AbstractSkill.HUNTER.GUNSLINGER.getEngraving1(), AbstractSkill.HUNTER.GUNSLINGER.getEngraving2(), AbstractSkill.HUNTER.GUNSLINGER.values()), //
    // 실린
    BARD("바드", null, 1000, 0.1f, AbstractSkill.MAGICIAN.BARD.getBasic(), AbstractSkill.MAGICIAN.BARD.getIdentity(), AbstractSkill.MAGICIAN.BARD.getEngraving1(), AbstractSkill.MAGICIAN.BARD.getEngraving2(), AbstractSkill.MAGICIAN.BARD.values()), //
    SUMMONER("서모너", null, 1000, 0.1f, AbstractSkill.MAGICIAN.SUMMONER.getBasic(), AbstractSkill.MAGICIAN.SUMMONER.getIdentity(), AbstractSkill.MAGICIAN.SUMMONER.getEngraving1(), AbstractSkill.MAGICIAN.SUMMONER.getEngraving2(), AbstractSkill.MAGICIAN.SUMMONER.values()), //
    ARCANA("아르카나", null, 1000, 0.1f, AbstractSkill.MAGICIAN.ARCANA.getBasic(), AbstractSkill.MAGICIAN.ARCANA.getIdentity(), AbstractSkill.MAGICIAN.ARCANA.getEngraving1(), AbstractSkill.MAGICIAN.ARCANA.getEngraving2(), AbstractSkill.MAGICIAN.ARCANA.values()), //
    SORCERESS("소서리스", null, 1000, 0.1f, AbstractSkill.MAGICIAN.SORCERESS.getBasic(), AbstractSkill.MAGICIAN.SORCERESS.getIdentity(), AbstractSkill.MAGICIAN.SORCERESS.getEngraving1(), AbstractSkill.MAGICIAN.SORCERESS.getEngraving2(), AbstractSkill.MAGICIAN.SORCERESS.values()), //
    // 데런
    DEMONIC("데모닉", null, 1200, 0.1f, AbstractSkill.ASSASSIN.DEMONIC.getBasic(), AbstractSkill.ASSASSIN.DEMONIC.getIdentity(), AbstractSkill.ASSASSIN.DEMONIC.getEngraving1(), AbstractSkill.ASSASSIN.DEMONIC.getEngraving2(), AbstractSkill.ASSASSIN.DEMONIC.values()), //
    BLADE("블레이드", null, 1200, 0.1f, AbstractSkill.ASSASSIN.BLADE.getBasic(), AbstractSkill.ASSASSIN.BLADE.getIdentity(), AbstractSkill.ASSASSIN.BLADE.getEngraving1(), AbstractSkill.ASSASSIN.BLADE.getEngraving2(), AbstractSkill.ASSASSIN.BLADE.values()), //
    REAPER("리퍼", null, 700, 0.1f, AbstractSkill.ASSASSIN.REAPER.getBasic(), AbstractSkill.ASSASSIN.REAPER.getIdentity(), AbstractSkill.ASSASSIN.REAPER.getEngraving1(), AbstractSkill.ASSASSIN.REAPER.getEngraving2(), AbstractSkill.ASSASSIN.REAPER.values(), ChatColor.YELLOW + "[근접 공격]", ChatColor.GREEN + "빠른 기동성과 강한 공력력", ChatColor.RED + "낮은 체력"), //
    SOUL_EATER("소울이터", null, 1000, 0.1f, AbstractSkill.ASSASSIN.SOUL_EATER.getBasic(), AbstractSkill.ASSASSIN.SOUL_EATER.getIdentity(), AbstractSkill.ASSASSIN.SOUL_EATER.getEngraving1(), AbstractSkill.ASSASSIN.SOUL_EATER.getEngraving2(), AbstractSkill.ASSASSIN.SOUL_EATER.values()), //
    // 스페셜리스트
    ARTIST("도화가", null, 1000, 0.1f, AbstractSkill.SPECIALIST.ARTIST.getBasic(), AbstractSkill.SPECIALIST.ARTIST.getIdentity(), AbstractSkill.SPECIALIST.ARTIST.getEngraving1(), AbstractSkill.SPECIALIST.ARTIST.getEngraving2(), AbstractSkill.SPECIALIST.ARTIST.values()), //
    AEROMANCER("기상술사", null, 1000, 0.1f, AbstractSkill.SPECIALIST.AEROMANCER.getBasic(), AbstractSkill.SPECIALIST.AEROMANCER.getIdentity(), AbstractSkill.SPECIALIST.AEROMANCER.getEngraving1(), AbstractSkill.SPECIALIST.AEROMANCER.getEngraving2(), AbstractSkill.SPECIALIST.AEROMANCER.values()) //
    //
    ;
    private final String name;
    private final AbstractPassive passive;
    private final double maxHealth; // 기본체력
    private final float moveSpeed; // 기본 이동속도
    private final AbstractSkill basic;
    private final AbstractSkill identity;
    private final AbstractCharacterSkill.Engraving engraving1;
    private final AbstractCharacterSkill.Engraving engraving2;
    private final AbstractSkill[] skills;
    private final List<String> lore;

    Character(String name, AbstractPassive passive, double maxHealth, float moveSpeed, AbstractSkill basic, AbstractSkill identity, AbstractCharacterSkill.Engraving engraving1, AbstractCharacterSkill.Engraving engraving2, AbstractSkill[] skills, String... lore) {
        this.name = name;
        this.passive = passive;
        this.maxHealth = maxHealth;
        this.moveSpeed = moveSpeed;
        this.basic = basic;
        this.identity = identity;
        this.engraving1 = engraving1;
        this.engraving2 = engraving2;
        this.skills = skills;
        this.lore = List.of(lore);
    }

    public static Character valueFromKorean(String name) {
        for (Character character : values())
            if (character.getName().equals(name))
                return character;
        return null;
    }

    public ItemStack getInfo() {
        ItemStack item = new ItemStack(Material.AMETHYST_SHARD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE + this.name);
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}
