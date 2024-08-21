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
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea();
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, range, damage);
        long tick = 10;
        long delay = 4;
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), delay);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), tick);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), tick + delay);
        return true;
    }


    private void Attack(Player player, double range, double damage) {
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
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
                if (x != 0 || z != 0) vector = vector.normalize();
                vector = vector.clone().multiply(2);
                Location now = RLocation.getLocation(loc, vector.getX() * range, vector.getY() * range - 0.5d, vector.getZ() * range);
                EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) range, 1, 0);
            }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getDamage(level);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "잠시 후 추가로 피해를 준다.");
            list.addFirst(ChatColor.WHITE + "전방으로 춤을 추듯 돌며 피해를 주며 이동한다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("잠시 후 추가로 %.0f, %.0f의 피해를 준다..", damage, damage));
            list.addFirst(ChatColor.WHITE + String.format("전방으로 춤을 추듯 돌며 %.0f, %.0f의 피해를 주며 이동한다.", damage, damage));
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
