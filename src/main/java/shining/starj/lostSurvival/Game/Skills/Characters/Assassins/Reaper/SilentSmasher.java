package shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
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


public class SilentSmasher extends AbstractSkill {

    public SilentSmasher() {
        super("사일런트 스매셔", Material.PINK_DYE, Upgrade.DAMAGE, new Upgrade[]{Upgrade.DAMAGE, Upgrade.COOL_DOWN});
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
        for (int i = 0; i < 3; i++)
            new Attack(player, getDamage(level)).runTaskLater(Core.getCore(), (i + 1) * 6);
        return true;
    }

    private static class Attack extends BukkitRunnable {
        private final Player player;
        private final double damage;
        private final List<LivingEntity> list;
        private final double maxDistance;

        public Attack(Player player, double damage) {
            this.player = player;

            this.damage = damage;
            this.list = new ArrayList<>();
            this.maxDistance = 20;
        }

        @Override
        public void run() {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                this.cancel();
                return;
            }
            Location loc = player.getLocation().clone().add(0, 3, 0);
            Vector dir = loc.getDirection().setY(0).normalize().setY(-0.5);
            RayTraceResult result = loc.getWorld().rayTraceBlocks(loc, dir, maxDistance, FluidCollisionMode.ALWAYS);
            Location end;
            if (result == null)
                end = loc.clone().add(dir.clone().multiply(maxDistance)).getBlock().getLocation().add(0.5, 0.25, 0.5);
            else {
                Block target = result.getHitBlock();
                end = target.getLocation().subtract(dir).getBlock().getLocation().add(0.5, 0.25, 0.5);
            }
            double length = end.distance(loc);
            for (double d = 0; d <= length; d += 0.5d) {
                Location now = loc.clone().add(dir.clone().multiply(d));
                EffectStore.getInstance().spawnDustParticle(now, Color.RED, 1f, 7, 0.1);
                for (Entity et : now.getWorld().getNearbyEntities(now, 0.5, 3, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, damage);
                    list.add(le);
                }
            }

            loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 5f);
            end.getWorld().playSound(end, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 3f, 5f);
        }
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0)
            list.addFirst(ChatColor.WHITE + "기운이 전방으로 빠르게 이동하며 베어 강력한 피해를 준다.");
        else
            list.addFirst(ChatColor.WHITE + String.format("기운이 전방으로 빠르게 이동하며 베어 %.0f의 강력한 피해를 준다.", getDamage(level)));
        list.addFirst(ChatColor.WHITE + "공중에서 어둠의 기운을 모은다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
