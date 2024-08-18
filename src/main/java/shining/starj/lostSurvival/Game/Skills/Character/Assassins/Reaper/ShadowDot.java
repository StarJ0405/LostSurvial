package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Effects.RLocation;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class ShadowDot extends AbstractSkill {

    public ShadowDot() {
        super("쉐도우 닷", Material.WOODEN_SWORD, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 5;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = 0.5d * playerStore.getArea();
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, range, damage);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), Math.min(7, getCoolDownTick(level) / 2));
        return true;
    }

    private final Vector[] vectors = {new Vector(0, 0, -2), new Vector(0.2, 0, -1.6), //
            new Vector(0.4, 0, -1.2), new Vector(0.6, 0, -0.8), //
            new Vector(0.8, 0, -0.4), //
            new Vector(0.85, 0, 0), //
            new Vector(0.8, 0, 0.4), //
            new Vector(0.6, 0, 0.8), new Vector(0.4, 0, 1.2), //
            new Vector(0.2, 0, 1.6), new Vector(0, 0, 2)//
    };

    private void Attack(Player player, double range, double damage) {
        if (PlayerStore.getStore(player).isDead()) return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, 1d + vector.getX() * range, vector.getY() * range - 0.5d, vector.getZ() * range);
            EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) range, 1, 0);

            for (Entity et : loc.getWorld().getNearbyEntities(now, range, range, range, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
                list.add(le);
            }
        }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        double damage = getDamage(level);
        list.addFirst(ChatColor.WHITE + String.format("힘을 모아 강하게 돌며 %.0f, %.0f의 피해를 준다.", damage, damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
