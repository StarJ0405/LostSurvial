package shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Effects.RLocation;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class Evelisto extends AbstractSkill {

    public Evelisto() {
        super("이블리스토", Material.BLACK_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    Vector[] vectors = new Vector[]{ //
            new Vector(0, 0, -0.8), new Vector(0.2, 0.1, -0.7), //
            new Vector(0.4, 0.2, -0.6), new Vector(0.4, 0.3, -0.5), //
            new Vector(0.6, 0.4, -0.4), new Vector(0.6, 0.5, -0.3), //
            new Vector(0.8, 0.6, -0.2), new Vector(0.8, 0.7, -0.1), //
            new Vector(1, 0.8, 0), //
            new Vector(0.8, 0.9, 0.1), new Vector(0.8, 1, 0.2), //
            new Vector(0.6, 1.1, 0.3), new Vector(0.6, 1.2, 0.4), //
            new Vector(0.4, 1.3, 0.5), new Vector(0.4, 1.4, 0.6), //
            new Vector(0.2, 1.5, 0.7), new Vector(0, 1.6, 0.8) //
    };

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, damage, 1);
        long tick = 10;
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage, -1), tick);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage, 1), 2 * tick);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage, -1), 3 * tick);
        return true;
    }


    private void Attack(Player player, double damage, int axis) {
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
            return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, 0.5d + vector.getX(), vector.getY() - 0.5d, vector.getZ() * axis);
            EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) 0.5, 1, 0);
            for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 0.5, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
                list.add(le);
            }
        }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getDamage(level);
        if (level == 0)
            list.addFirst(ChatColor.WHITE + "전방으로 4회 광기어린 난도질을 하여 피해를 준다.");
        else
            list.addFirst(ChatColor.WHITE + String.format("전방으로 4회 광기어린 난도질을 하여 %.0f, %.0f, %.0f, %.0f 피해를 준다.", damage, damage, damage, damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
