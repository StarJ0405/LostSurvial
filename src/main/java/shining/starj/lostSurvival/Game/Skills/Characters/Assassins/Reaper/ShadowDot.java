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


public class ShadowDot extends AbstractSkill {

    public ShadowDot() {
        super("쉐도우 닷", Material.WOODEN_SWORD, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 5;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    public int getReduceCoolTick() {
        return (int) (1.5d * 20);
    }

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        Attack(player, damage);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, damage), 10);
        if (upgrade) {
            applyReduceCoolDown(player, AbstractSkill.ASSASSIN.REAPER.LAST_GRAFFITI.getType());
            applyReduceCoolDown(player, AbstractSkill.ASSASSIN.REAPER.RAGE_SPEAR.getType());
            applyReduceCoolDown(player, AbstractSkill.ASSASSIN.REAPER.SILENT_SMASHER.getType());
            applyReduceCoolDown(player, AbstractSkill.ASSASSIN.REAPER.DANCING_OF_FURY.getType());
        }
        return true;
    }

    public void applyReduceCoolDown(Player player, Material material) {
        if (player.hasCooldown(material))
            player.setCooldown(material, Math.max(0, player.getCooldown(material) - getReduceCoolTick()));
    }

    private void Attack(Player player, double damage) {
        if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START))
            return;
        Location loc = player.getEyeLocation();
        List<LivingEntity> list = new ArrayList<>();
        for (int x = 0; x <= 4; x++)
            for (int z = -4; z <= 4; z++) {
                if (x == 0 && z == 0) continue;
                Vector vector = new Vector(x, 0, z).normalize();
                Location now = RLocation.getLocation(loc, 1d + vector.getX(), -0.5, vector.getZ());
                EffectStore.getInstance().spawnDustParticle(now, Color.BLACK, 1f, 2, 0.1, 0, 0.1);

                for (Entity et : loc.getWorld().getNearbyEntities(now, 0.5, 2, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, player, damage);
                    list.add(le);
                }
            }
        for (Entity et : loc.getWorld().getNearbyEntities(loc, 0.5, 2, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
            LivingEntity le = (LivingEntity) et;
            damage(le, player, damage);
            list.add(le);
        }
        loc.getWorld().playSound(loc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1f, 1f);
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        double damage = getDamage(level);
        if (level == 0)
            list.addFirst(ChatColor.WHITE + "힘을 모아 강하게 돌며 피해를 준다.");
        else {
            if (upgrade)
                list.addFirst(ChatColor.YELLOW + String.format("스킬 사용 시 급습 스킬들의 재사용 대기시간이 %.1f초 감소한다.", getReduceCoolTick() / 20d));
            list.addFirst(ChatColor.WHITE + String.format("힘을 모아 강하게 돌며 %.0f, %.0f의 피해를 준다.", damage, damage));
        }

        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        List<String> list = new ArrayList<>();
        if (upgrade)
            list.add(ChatColor.WHITE + String.format("스킬 사용 시 급습 스킬들의 재사용 대기시간이 %.1f초 감소한다.", getReduceCoolTick() / 20d));
        else list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
