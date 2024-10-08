package shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class LastGraffiti extends AbstractSkill {

    public LastGraffiti() {
        super("라스트 그래피티", Material.BLUE_DYE, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
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
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea();

//        player.setVelocity(new Vector(0, 1, 0));
//        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
//            Vector dir = player.getLocation().getDirection().clone().setY(0).normalize().multiply(1.5d).setY(0.5);
//            player.setVelocity(dir);
//        }, 1);

        for (int i = 0; i < 3; i++)
            Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> player.setVelocity(new Vector(0, 1, 0)), i);

        new Attack(player, getDamage(level), range).runTaskTimer(Core.getCore(), 3, 1);
        return true;
    }

    private static class Attack extends BukkitRunnable {
        private int tick;
        private final int maxTick;
        private final Player player;
        private final double damage;
        private final double range;

        public Attack(Player player, double damage, double range) {
            this.tick = 0;
            this.maxTick = 20 * 20;
            this.player = player;
            this.damage = damage;
            this.range = range;
        }

        @Override
        public void run() {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                player.setVelocity(new Vector());
                this.cancel();
                return;
            }
            this.tick++;
            if (this.tick >= this.maxTick) {
                this.cancel();
            }
            Location loc = player.getLocation();
            for (int x = -4; x <= 4; x++)
                for (int z = -4; z <= 4; z++) {
                    if (x == 0 && z == 0) continue;
                    Vector vector = new Vector(x, 0, z).normalize().multiply(3 * range);
                    Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                    EffectStore.getInstance().spawnDustParticle(now, Color.RED, 1f, 1, 0);
                }
            if (this.tick >= 10)
                player.setVelocity(player.getLocation().getDirection().clone().setY(-0.5).normalize().multiply(1.25d));

            if (this.player.isOnGround()) {
                for (Entity et : loc.getWorld().getNearbyEntities(loc, range * 3, 1, range * 3, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, damage);
                }
                loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 1f, 0.5f);
                this.cancel();
            }
        }
    }


    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0)
            list.addFirst(ChatColor.WHITE + "연기처럼 사라져 이동 후 강하게 내려찍어 피해를 준다.");
        else
            list.addFirst(ChatColor.WHITE + String.format("연기처럼 사라져 이동 후 강하게 내려찍어 %.0f의 피해를 준다.", getDamage(level)));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
