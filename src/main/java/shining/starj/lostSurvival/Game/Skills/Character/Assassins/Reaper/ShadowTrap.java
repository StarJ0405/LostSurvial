package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class ShadowTrap extends AbstractSkill {

    public ShadowTrap() {
        super("쉐도우 트랩", Material.LIGHT_BLUE_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private double getStun(int level) {
        return 2;
    }

    @Override
    public boolean Use(Player player, int level) {
        if (!player.isOnGround()) return false;
        PlayerStore playerStore = PlayerStore.getStore(player);

        Location loc = player.getLocation();
        player.setVelocity(new Vector(0, 1, 0));
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead())
                return;
            player.setVelocity(player.getLocation().getDirection().clone().setY(0).normalize().multiply(-1d));
        }, 2);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, loc, getDamage(level) * playerStore.getDamage(), getStun(level)), Math.min(5, getCoolDownTick(level) / 2));
        return true;
    }

    private void Attack(Player player, Location loc, double damage, double stun) {
        if (PlayerStore.getStore(player).isDead()) return;
        for (Entity et : loc.getWorld().getNearbyEntities(loc, 2, 2, 2, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
            LivingEntity le = (LivingEntity) et;
            CustomEntities.setStun(et, stun);
            damage(le, player, damage);
        }
        EffectStore.getInstance().spawnDustParticle(loc, Color.PURPLE, 1f, 100, 1, 0.1, 1);
        for (int x = -4; x <= 4; x++)
            for (int z = -4; z <= 4; z++) {
                if (x == 0 && z == 0) continue;
                Vector vector = new Vector(x, 0, z).normalize().multiply(2);
                Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 1, 0);
            }
        loc.getWorld().playSound(loc, Sound.BLOCK_END_PORTAL_SPAWN, 1f, 10f);
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        list.addFirst(ChatColor.WHITE + String.format("주변에 %.0f의 피해를 주고, %.1f초간 기절시킨다.", getDamage(level), getStun(level)));
        list.addFirst(ChatColor.WHITE + "자신의 위치에 그림자 덫을 소환하며, 뒤로 이동한 뒤 그림자를 터트린다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
