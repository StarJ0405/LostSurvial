package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class Distortion extends AbstractSkill {

    public Distortion() {
        super("디스토션", Material.ORANGE_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
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
        double damage = getDamage(level) * playerStore.getDamage();

        Location loc = player.getLocation().clone().add(0, 0.25d, 0);
        Vector dir = loc.getDirection().setY(0);
        if (dir.length() != 0)
            dir = dir.normalize().toBlockVector();
        RayTraceResult result = loc.getWorld().rayTraceBlocks(loc, dir, 5, FluidCollisionMode.ALWAYS);

        Location end;
        if (result == null)
            end = loc.clone().add(dir.clone().multiply(5)).getBlock().getLocation().add(0.5, 0.25, 0.5);
        else {
            Block target = result.getHitBlock();
            end = target.getLocation().subtract(dir).getBlock().getLocation().add(0.5, 0.25, 0.5);
            if (!end.getBlock().getType().isAir())
                end.add(0, 1, 0);
        }
        end.setDirection(loc.getDirection());

        player.teleport(end);
        double length = loc.distance(end);
        Vector add = end.clone().subtract(loc).toVector().normalize();
        List<LivingEntity> list = new ArrayList<>();
        for (double times = 0; times < length; times += 0.5d) {
            Location now = loc.clone().add(add.clone().multiply(times));
            EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 10, 0);
            for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 0.5, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
                list.add(le);
            }
        }
        loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 1f, 1f);
        return true;
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        double damage = getDamage(level);
        list.addFirst(ChatColor.WHITE + String.format("왜곡된 공간의 어둠을 만든 후 빠르게 10m 전방으로 이동하며 도착 시 이동거리에 있던 대상에게 %.0f의 피해를 추가로 준다.", damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
