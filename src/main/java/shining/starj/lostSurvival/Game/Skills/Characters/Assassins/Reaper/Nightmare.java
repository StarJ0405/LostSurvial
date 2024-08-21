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
import java.util.Collection;
import java.util.List;


public class Nightmare extends AbstractSkill {

    public Nightmare() {
        super("나이트 메어", Material.BRICK, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 5;
    }

    private double getExplodeDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private double getHitDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = playerStore.getDamage();
        new Attack(player, getHitDamage(level) * damage, getExplodeDamage(level) * damage).runTaskTimer(Core.getCore(), 0, 1);
        return true;
    }

    static class Attack extends BukkitRunnable {
        private final Player player;
        private final Location loc;
        private final Vector dir;
        private int tick;
        private final int maxTick;
        private final double hit;
        private final double explode;
        private final List<LivingEntity> list;

        public Attack(Player player, double hit, double explode) {
            this.player = player;
            this.loc = player.getEyeLocation().clone().subtract(0, 0.5, 0);
            this.dir = loc.getDirection().setY(0).normalize().multiply(0.5d);
            this.tick = 0;
            this.maxTick = 10;
            this.hit = hit;
            this.explode = explode;
            this.list = new ArrayList<>();
            loc.getWorld().playSound(loc, Sound.ENTITY_ARROW_SHOOT, 1f, 5f);
        }

        @Override
        public void run() {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                this.cancel();
                return;
            }
            this.tick++;
            if (this.tick >= maxTick) this.cancel();

            if (check()) check();
        }

        private boolean check() {
            Collection<Entity> check = loc.getWorld().getNearbyEntities(loc, 0.5, 2, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity);
            if (!check.isEmpty() || this.isCancelled()) {
                if (!check.isEmpty()) {
                    loc.getWorld().playSound(loc, Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f, 0.1f);
                    player.playSound(player, Sound.ENTITY_ARROW_HIT_PLAYER, 0.5f, 0.1f);
                }
                for (Entity et : check) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, hit);
                }
                Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
                    for (Entity et : loc.getWorld().getNearbyEntities(loc, 3, 2, 3, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                        LivingEntity le = (LivingEntity) et;
                        damage(le, player, explode);
                        list.add(le);
                    }
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 5f);
                    player.playSound(player, Sound.ENTITY_GENERIC_EXPLODE, 0.5f, 5f);
                    EffectStore.getInstance().spawnDustParticle(loc, Color.BLACK, 1f, 50, 1.5, 0.1, 1.5);
                }, 2);
                this.cancel();
                return false;
            } else EffectStore.getInstance().spawnDustParticle(loc, Color.BLACK, 1f, 3, 0.1);
            loc.add(dir);
            return true;
        }
    }


    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getExplodeDamage(level);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "적중 혹은 최대 거리 도달시 터져 주변에 피해를 입힌다.");
            list.addFirst(ChatColor.WHITE + "지정된 방향으로 빠르게 단검을 던져 피해를 입힌다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("적중 혹은 최대 거리 도달시 터져 주변에 %.0f의 피해를 입힌다.", damage));
            list.addFirst(ChatColor.WHITE + String.format("지정된 방향으로 빠르게 단검을 던져 %.0f의 피해를 입힌다.", damage));
        }
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getExplodeDamage(level - 1), getExplodeDamage(level)));
        return list;
    }
}
