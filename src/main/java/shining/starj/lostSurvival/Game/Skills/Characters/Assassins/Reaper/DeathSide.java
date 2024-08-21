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


public class DeathSide extends AbstractSkill {

    public DeathSide() {
        super("데스 사이드", Material.RED_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
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
            new Vector(0, 0, 0), new Vector(0.2, 0.1, 0), //
            new Vector(0.4, 0.2, 0), new Vector(0.4, 0.3, 0), //
            new Vector(0.6, 0.4, 0), new Vector(0.6, 0.5, 0), //
            new Vector(0.8, 0.6, 0), new Vector(0.8, 0.7, 0), //
            new Vector(1, 0.8, 0), //
            new Vector(0.8, 0.9, 0), new Vector(0.8, 1, 0), //
            new Vector(0.6, 1.1, 0), new Vector(0.6, 1.2, 0), //
            new Vector(0.4, 1.3, 0), new Vector(0.4, 1.4, 0), //
            new Vector(0.2, 1.5, 0), new Vector(0, 1.6, 0) //
    };

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        AttackHorizon(player, damage);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> AttackVertical(player, damage), 10);
        return true;
    }

    private void AttackHorizon(Player player, double damage) {
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
            return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (int x = 0; x <= 4; x++)
            for (int z = -4; z <= 4; z++) {
                Vector vector = new Vector(x, 0, z);
                if (x != 0 || z != 0)
                    vector = vector.normalize();
                Location now = RLocation.getLocation(loc, 0.5d + vector.getX(), vector.getY() - 0.5d, vector.getZ());
                EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) 0.5, 1, 0);
                for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 0.5, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, damage);
                    list.add(le);
                }
            }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    private void AttackVertical(Player player, double damage) {
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, 0.5d + vector.getX(), vector.getY() - 0.5d, vector.getZ());
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
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "잠시 후 더욱 빠르게 찔러 피해를 준다.");
            list.addFirst(ChatColor.WHITE + "적의 빈틈을 노려 날카롭게 찔러 피해를 준다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("잠시 후 더욱 빠르게 찔러 %.0f의 피해를 준다.", damage));
            list.addFirst(ChatColor.WHITE + String.format("적의 빈틈을 노려 날카롭게 찔러 %.0f의 피해를 준다.", damage));
        }
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
