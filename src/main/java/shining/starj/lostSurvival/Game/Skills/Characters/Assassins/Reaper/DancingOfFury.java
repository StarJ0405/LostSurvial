package shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
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
import java.util.Collections;
import java.util.List;


public class DancingOfFury extends AbstractSkill {

    public DancingOfFury() {
        super("댄싱 오브 퓨리", Material.MAGENTA_DYE, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private int getTick(int level) {
        return 5;
    }

    private double getLastDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        int tick = getTick(level);
        new Attack(player, getDamage(level) * playerStore.getDamage(), tick).runTaskTimer(Core.getCore(), 0, 2);
        new Attack(player, getLastDamage(level) * playerStore.getDamage()).runTaskLater(Core.getCore(), 2L * (getTick(level) + 2));
        return true;
    }

    final static Vector[] vectors = new Vector[]{new Vector(1, 0.5, -1.5), new Vector(3, 2.5, 1.5), //
            new Vector(0.5, 0.5, 1.5), new Vector(2, 1.5, -1.5), //
            new Vector(0.5, 2, 1.5), new Vector(2, 0.5, 1.5), //
            new Vector(0.5, 2, -1), new Vector(2, 0.5, 1) //
    };

    private static class Attack extends BukkitRunnable {
        private final Player player;
        private int tick;
        private final int maxTick;
        private final double damage;
        private final boolean last;

        public Attack(Player player, double damage, int maxTick) {
            this(player, damage, maxTick, false);
        }

        public Attack(Player player, double damage) {
            this(player, damage, 1, true);
        }

        private Attack(Player player, double damage, int maxTick, boolean last) {
            this.tick = 0;
            this.maxTick = maxTick;
            this.player = player;
            this.damage = damage;
            this.last = last;
        }

        @Override
        public void run() {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                this.cancel();
                return;
            }
            this.tick++;
            if (this.tick >= this.maxTick) {
                this.cancel();
            }
            Location loc = player.getLocation();
            Vector dir = loc.getDirection();
            List<Vector> list = new ArrayList<>(List.of(vectors));
            Collections.shuffle(list);
            for (int i = 0; i < list.size(); i += 2) {
                Vector vec1 = list.get(i);
                Vector vec2 = list.get(i + 1);
                Location start = RLocation.getLocation(loc, dir, vec1.getX(), vec1.getY(), vec1.getZ());
                Location end = RLocation.getLocation(loc, dir, vec2.getX(), vec2.getY(), vec2.getZ());
                double length = end.distance(start);
                Vector add = end.clone().subtract(start).toVector().normalize();
                for (double d = 0; d <= length; d += 0.25d) {
                    Location now = start.clone().add(add.clone().multiply(d));
                    if (last)
                        EffectStore.getInstance().spawnDustParticle(now, Color.fromRGB(150, 1, 17), 0.5f, 30, 0.5d);
                    else
                        EffectStore.getInstance().spawnDustParticle(now, Color.RED, 1f, 1);
                }
            }

            Location center = loc.clone().add(0, 1.5, 0).add(dir.clone().setY(0).normalize().multiply(1.5d));
            if (last)
                center.getWorld().playSound(center, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 2f, 0.1f);
            else
                center.getWorld().playSound(center, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 5f);
            for (Entity et : loc.getWorld().getNearbyEntities(center, 2, 3, 2, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
            }
        }
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "그 후 제자리로 돌아와 마무리 피해를 준다.");
            list.addFirst(ChatColor.WHITE + "붉은 그림자의 기운을 모아 전방 주변을 그림자 처럼 빠르게 움직이며 피해를 준다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("그 후 제자리로 돌아와 %.0f의 마무리 피해를 준다.", getLastDamage(level)));
            list.addFirst(ChatColor.WHITE + String.format("붉은 그림자의 기운을 모아 전방 주변을 그림자 처럼 빠르게 움직이며 총 %.0f의 피해를 준다.", getDamage(level) * getTick(level)));
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
