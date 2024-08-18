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


public class SpiritCatch extends AbstractSkill {

    public SpiritCatch() {
        super("스피릿 캐치", Material.WHITE_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 3;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    @Override
    public boolean Use(Player player, int level) {
        if (!player.isOnGround()) return false;
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, damage);
        player.setVelocity(new Vector(0, 1, 0));
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead()) return;
            player.setVelocity(player.getLocation().getDirection().setY(0).normalize());
        }, 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage), Math.min(10, getCoolDownTick(level) / 2));
        return true;
    }

    private final Vector[] vectors = { //
            new Vector(0, 0, 0), new Vector(0.25, 0, 0), //
            new Vector(0.5, 0, 0), new Vector(0.75, 0, 0), //
            new Vector(1, 0, 0), new Vector(1.25, 0, 0), //
            new Vector(1.5, 0, 0), new Vector(1.75, 0, 0), //
            new Vector(2, 0, 0), new Vector(2.25, 0, 0), //
            new Vector(2.5, 0, 0), new Vector(2.75, 0, 0), //
            new Vector(3, 0, 0), new Vector(3.25, 0, 0), //
            new Vector(3.5, 0, 0), new Vector(3.75, 0, 0), //
    };

    private void Attack(Player player, double damage) {
        if (PlayerStore.getStore(player).isDead()) return;
        player.setVelocity(new Vector());
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();

        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, 0.5d + vector.getX(), vector.getY() - 0.5d, vector.getZ());
            EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, (float) 0.5, 1, 0);

            for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 0.5, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
                list.add(le);
            }
        }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        double damage = getDamage(level);
        list.addFirst(ChatColor.WHITE + String.format("전방을 향해 빠르게 찔러 %.0f의 피해를 주고, 돌진하여 더욱 깊게 찔러 %.0f의 피해를 주고 적을 넘어 트린다.", damage, damage));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
