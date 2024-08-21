package shining.starj.lostSurvival.Game.Skills.Characters;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.lostSurvival.Game.Passives.AbstractPassive;
import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper.*;

import java.util.ArrayList;
import java.util.List;

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

    public static class Demonic extends AbstractCharacterSkill {
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


    public static class Reaper extends AbstractCharacterSkill {
        //피해량
        //치확
        //쿨감	쉐닷
        //투사체
        //이속
        //체력
        //방어
        //체젠
        //범위
        //지속
        //획득
        //경험
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
        public final SilentSmasher SILENT_SMASHER = new SilentSmasher();
        public final Nightmare NIGHTMARE = new Nightmare();
        //
        public final SoundOfTheMoon SOUND_OF_THE_MOON = new SoundOfTheMoon();
        public final Thirst THIRST = new Thirst();

        @Override
        public AbstractSkill getBasic() {
            return SHADOW_DOT;
        }

        @Override
        public AbstractSkill getIdentity() {
            return PERSONA;
        }

        @Override
        public Engraving getEngraving1() {
            return SOUND_OF_THE_MOON;
        }

        @Override
        public Engraving getEngraving2() {
            return THIRST;
        }

        public static class SoundOfTheMoon extends Engraving {
            @Override
            public AbstractPassive getPassive() {
                return null;
            }

            @Override
            public AbstractSkill getIdentity() {
                return null;
            }

            @Override
            public AbstractSkill getUltimate() {
                return null;
            }

            @Override
            public ItemStack getChoice() {
                ItemStack item = new ItemStack(Material.BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "달의 소리");
                List<String> lore = new ArrayList<>();
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }
        }

        public static class Thirst extends Engraving {
            @Override
            public AbstractPassive getPassive() {
                return null;
            }

            @Override
            public AbstractSkill getIdentity() {
                return null;
            }

            @Override
            public AbstractSkill getUltimate() {
                return null;
            }

            @Override
            public ItemStack getChoice() {
                ItemStack item = new ItemStack(Material.BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.AQUA + "갈증");
                List<String> lore = new ArrayList<>();
                meta.setLore(lore);
                item.setItemMeta(meta);
                return item;
            }
        }

        @Override
        public AbstractSkill[] values() {
            return new AbstractSkill[]{SPIRIT_CATCH, SPINNING_DAGGER, SABEL_STINGER, EVELISTO, PHANTOM_DANCER, DEATH_SIDE, DISTORTION, CALL_OF_KNIVES, SHADOW_STORM, BLINK, BLACK_MIST, SHADOW_TRAP, LAST_GRAFFITI, RAGE_SPEAR, DANCING_OF_FURY, SILENT_SMASHER, NIGHTMARE};
        }
    }

    public static class SoulEater extends AbstractCharacterSkill {
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
