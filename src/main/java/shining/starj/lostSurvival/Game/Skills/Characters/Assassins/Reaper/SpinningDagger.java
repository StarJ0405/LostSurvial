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
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double range = playerStore.getArea();
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, range, damage);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, range, damage), 12);
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
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
            return;

        List<LivingEntity> list = new ArrayList<>();
        Location loc = player.getEyeLocation();
        for (int i = 0; i < vectors.length; i++) {
            Vector vector = vectors[i];
            boolean sound = i % 8 == 0;
            Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
                Location now = RLocation.getLocation(loc, vector.getX() * range, vector.getY() * range - 0.5d, vector.getZ() * range);
                EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) range, 1, 0);
                for (Entity et : loc.getWorld().getNearbyEntities(now, range, range, range, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, damage);
                    list.add(le);
                }
                if (sound)
                    now.getWorld().playSound(now, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 0.7f, 5f);
            }, i / 8L + 1);


        }

    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getDamage(level);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "착지 시 한번 더 적을 베어 피해를 준다.");
            list.addFirst(ChatColor.WHITE + "전방으로 돌진하여 주변에 단검을 던져 피해를 준다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("착지 시 한번 더 적을 베어 %.0f의 피해를 준다.", damage));
            list.addFirst(ChatColor.WHITE + String.format("전방으로 돌진하여 주변에 단검을 던져 %.0f의 피해를 준다.", damage));
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
