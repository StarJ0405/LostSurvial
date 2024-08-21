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


public class ShadowStorm extends AbstractSkill {

    public ShadowStorm() {
        super("쉐도우 스톰", Material.LIME_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }

    private double getSpawnDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private int getTick(int level) {
        return 10;
    }

    private double getTickDamage(int level) {
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea() * 1.5d;

        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) return;
            Location loc = player.getLocation();
            loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 1f, 4f);
            final int size = vectors.length;
            for (int i = 0; i < getTick(level); i++) {
                Vector vec1 = vectors[(i * 2) % size];
                Vector vec2 = vectors[(i * 2 + 2) % size];
                Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
                    if (playerStore.isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) return;
                    Location start = RLocation.getLocation(loc, range * 1.5d, vec1);
                    Location end = RLocation.getLocation(loc, range * 1.5d, vec2);
                    Attack(player, loc, start, end, range, getTickDamage(level));
                }, i * 5L);
            }
            for (int x = -4; x <= 4; x++)
                for (int z = -4; z <= 4; z++) {
                    if (x == 0 && z == 0) continue;
                    Vector vector = new Vector(x, 0, z).normalize().multiply(1.2 * range);
                    Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                    EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 40, 0.2, 1, 0.2);
                }
            for (Entity et : loc.getWorld().getNearbyEntities(loc, range * 1.5, range * 1.5, range * 1.5, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, getSpawnDamage(level) * playerStore.getDamage());
            }
        }, 5);
        return true;
    }

    Vector[] vectors = new Vector[]{ //
            new Vector(1, 0, 0),//
            new Vector(Math.sin(Math.PI / 10), 0, Math.cos(Math.PI / 10)),//
            new Vector(-Math.sin(Math.PI / 10 * 3), 0, Math.cos(Math.PI / 10 * 3)),//
            new Vector(-Math.sin(Math.PI / 10 * 3), 0, -Math.cos(Math.PI / 10 * 3)),//
            new Vector(Math.sin(Math.PI / 10), 0, -Math.cos(Math.PI / 10))//
    };

    private void Attack(Player player, Location loc, Location start, Location end, double range, double damage) {
        double length = end.distance(start);
        Vector dir = end.clone().subtract(start).toVector().normalize();
        for (double i = 0; i <= length; i += 0.25d) {
            Location now = start.clone().add(dir.clone().multiply(i));
            EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, 1f, 16, 0, 1, 0);
            now.getWorld().playSound(now, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 5f);
        }
        for (Entity et : loc.getWorld().getNearbyEntities(loc, range * 1.5, range * 1.5, range * 1.5, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
            LivingEntity le = (LivingEntity) et;
            damage(le, player, damage);
        }
        for (int x = -4; x <= 4; x++)
            for (int z = -4; z <= 4; z++) {
                if (x == 0 && z == 0) continue;
                Vector vector = new Vector(x, 0, z).normalize().multiply(1.5 * range);
                Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 3, 0);
            }

    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "그 후 그림자를 소환하여 난무를 펼친다.");
            list.addFirst(ChatColor.WHITE + "전방으로 이동하며 빠르게 찔러 피해를 준다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("그 후 그림자를 소환하여 총 %d회에 걸쳐 최대 %.0f의 난무를 펼친다.", getTick(level), getTick(level) * getTickDamage(level)));
            list.addFirst(ChatColor.WHITE + String.format("전방으로 이동하며 빠르게 찔러 %.0f의 피해를 준다.", getSpawnDamage(level)));
        }
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getSpawnDamage(level - 1), getSpawnDamage(level)));
        return list;
    }
}
