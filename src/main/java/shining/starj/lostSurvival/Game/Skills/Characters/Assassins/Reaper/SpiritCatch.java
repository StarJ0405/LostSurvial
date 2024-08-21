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
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, damage);

        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage), 10);
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
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
            return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();

        for (Vector vector : vectors) {
            Location now = RLocation.getLocation(loc, 0.5d + vector.getX(), vector.getY() - 0.5d, vector.getZ());
            EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, 1f, 3, 0.05d);

            for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 0.5, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
                list.add(le);
            }
        }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getDamage(level);
        if (level == 0) {
            list.addFirst(ChatColor.WHITE + "그 후 돌진하여 더욱 깊게 찔러 피해를 준다.");
            list.addFirst(ChatColor.WHITE + "전방을 향해 빠르게 찔러 피해를 준다.");
        } else {
            list.addFirst(ChatColor.WHITE + String.format("그 후 돌진하여 더욱 깊게 찔러 %.0f의 피해를 준다.", damage));
            list.addFirst(ChatColor.WHITE + String.format("전방을 향해 빠르게 찔러 %.0f의 피해를 준다.", damage));
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
