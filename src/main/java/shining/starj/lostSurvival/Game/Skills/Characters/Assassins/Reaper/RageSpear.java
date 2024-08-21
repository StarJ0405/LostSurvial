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


public class RageSpear extends AbstractSkill {

    public RageSpear() {
        super("레이지 스피어", Material.PURPLE_DYE, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.COOL_DOWN});
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

        new Attack(player, getDamage(level) * playerStore.getDamage()).runTaskTimer(Core.getCore(), 0, 1);
        return true;
    }

    private static class Attack extends BukkitRunnable {
        private final Player player;
        private int tick;
        private final int maxTick;
        private final double damage;

        public Attack(Player player, double damage) {
            this.tick = 0;
            this.maxTick = 10;
            this.player = player;
            this.damage = damage;
        }

        @Override
        public void run() {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                this.cancel();
                return;
            }
            this.tick++;
            if (this.tick >= this.maxTick) this.cancel();

            Location loc = player.getEyeLocation();
            Vector dir = loc.getDirection().clone().setY(0).normalize();
            List<LivingEntity> list = new ArrayList<>();
            for (double d = 0d; d <= 8d; d += 0.5d) {
                Location now = loc.clone().add(dir.clone().multiply(d));

                if (this.tick == this.maxTick) {
                    EffectStore.getInstance().spawnDustParticle(now, Color.fromRGB(150, 1, 17), 0.5f, 50, 0.8d);
                    for (Entity et : now.getWorld().getNearbyEntities(now, 1, 3, 1, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                        LivingEntity le = (LivingEntity) et;
                        damage(le, player, damage);
                        list.add(le);
                    }
                    now.getWorld().playSound(now, Sound.ENTITY_BREEZE_WIND_BURST, 0.5f, 0.5f);
                } else {
                    EffectStore.getInstance().spawnDustParticle(now, Color.RED, 1f, (int) ((8 - d) * 2 + 1), (8 - d) / 10d);
                    now.getWorld().playSound(now, Sound.ENTITY_WIND_CHARGE_WIND_BURST, 0.25f, 0.5f);
                }
            }
        }
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0)
            list.addFirst(ChatColor.WHITE + String.format("전방의 적을 찔러 피해를 준다.", getDamage(level)));
        else
            list.addFirst(ChatColor.WHITE + String.format("전방의 적을 찔러 %.0f의 피해를 준다.", getDamage(level)));
        list.addFirst(ChatColor.WHITE + "순간적으로 붉은 그림자의 기운을 모은다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
