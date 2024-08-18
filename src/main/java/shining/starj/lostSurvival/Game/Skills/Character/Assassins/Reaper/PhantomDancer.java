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


public class PhantomDancer extends AbstractSkill {

    public PhantomDancer() {
        super("팬텀 댄서", Material.BROWN_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
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
        if (!player.isOnGround())
            return false;
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea();
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, range, damage);
        player.setVelocity(new Vector(0, 1, 0));
        long tick = Math.min(10, getCoolDownTick(level) / 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> player.setVelocity(player.getLocation().getDirection().clone().setY(0).normalize().multiply(0.75d)), 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead())
                return;
            Attack(player, range, damage);
            player.setVelocity(new Vector());
        }, tick);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), tick + 2);
        return true;
    }


    private void Attack(Player player, double range, double damage) {
        if (PlayerStore.getStore(player).isDead())
            return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (Entity et : loc.getWorld().getNearbyEntities(loc, range * 2, range, range * 2, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
            LivingEntity le = (LivingEntity) et;
            damage(le, player, damage);
            list.add(le);
        }
        for (int x = -4; x <= 4; x++)
            for (int z = -4; z <= 4; z++) {
                Vector vector = new Vector(x, 0, z);
                if (x != 0 || z != 0)
                    vector = vector.normalize();
                vector = vector.clone().multiply(2);
                Location now = RLocation.getLocation(loc, vector.getX() * range, vector.getY() * range - 0.5d, vector.getZ() * range);
                EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) range, 1, 0);
            }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        double damage = getDamage(level);
        list.addFirst(ChatColor.WHITE + String.format("마우스 방향으로 춤을 추듯 돌며 %.0f, %.0f의 피해를 주며 5m 이동하고, 스킬을 다시 한번 입력 시 추가로 %.0f, %.0f의 피해를 주며 5m 이동한다.", damage, damage, damage, damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
