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


public class SpinningDagger extends AbstractSkill {

    public SpinningDagger() {
        super("스피닝 대거", Material.LIGHT_GRAY_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level) {
        if (!player.isOnGround()) return false;
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea();
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, range, damage);
        player.setVelocity(new Vector(0, 1, 0));
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead())
                return;
            player.setVelocity(player.getLocation().getDirection().clone().setY(0).normalize().multiply(0.75d));
        }, 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), Math.min(10, getCoolDownTick(level) / 2));
        return true;
    }

    Vector[] vectors = new Vector[]{ //
            new Vector(0.5, 0, -0.2), new Vector(0.7, 0, -0.4), //
            new Vector(0.9, 0, -0.6), new Vector(1.1, 0, -0.8), //
            new Vector(1.3, 0, -1), new Vector(1.5, 0, -1.2), //
            new Vector(1.7, 0, -1.4), new Vector(1.9, 0, -1.6),
            //
            new Vector(0.5, 0, -0.1), new Vector(0.7, 0, -0.2), //
            new Vector(0.9, 0, -0.3), new Vector(1.1, 0, -0.4), //
            new Vector(1.3, 0, -0.5), new Vector(1.5, 0, -0.6), //
            new Vector(1.7, 0, -0.7), new Vector(1.9, 0, -0.8), //
            //
            new Vector(0.5, 0, 0), new Vector(0.7, 0, 0), //
            new Vector(0.9, 0, 0), new Vector(1.1, 0, 0), //
            new Vector(1.3, 0, 0), new Vector(1.5, 0, 0), //
            new Vector(1.7, 0, 0), new Vector(1.9, 0, 0), //
            //
            new Vector(0.5, 0, 0.1), new Vector(0.7, 0, 0.2), //
            new Vector(0.9, 0, 0.3), new Vector(1.1, 0, 0.4), //
            new Vector(1.3, 0, 0.5), new Vector(1.5, 0, 0.6), //
            new Vector(1.7, 0, 0.7), new Vector(1.9, 0, 0.8), //
            //
            new Vector(0.5, 0, 0.2), new Vector(0.7, 0, 0.4), //
            new Vector(0.9, 0, 0.6), new Vector(1.1, 0, 0.8), //
            new Vector(1.3, 0, 1), new Vector(1.5, 0, 1.2), //
            new Vector(1.7, 0, 1.4), new Vector(1.9, 0, 1.6)};

    private void Attack(Player player, double range, double damage) {
        if (PlayerStore.getStore(player).isDead())
            return;
        player.setVelocity(new Vector());
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();

        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, vector.getX() * range, vector.getY() * range - 0.5d, vector.getZ() * range);
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
        list.addFirst(ChatColor.WHITE + String.format("전방으로 5m 돌진하여 주변에 단검을 던져 %.0f의 피해를 주고, 착지 시 한번 더 적을 베어 %.0f의 피해를 준다.", damage, damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
