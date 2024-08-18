package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Buffs.AbstractBuff;
import shining.starj.lostSurvival.Buffs.BuffType;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BlackMist extends AbstractSkill {

    public BlackMist() {
        super("블랙 미스트", Material.CYAN_DYE, Upgrade.DURATION, new Upgrade[]{Upgrade.DAMAGE, Upgrade.PROJECTILE, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 10;
    }

    private double getDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private double getDuration(int level) {
        return 6;
    }

    public double getReduce(int level) {
        return 0.2d;
    }

    @Override
    public boolean Use(Player player, int level) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        Buff buff = new Buff(this.name, getDuration(level) * playerStore.getDuration(), player, getDamage(level) * playerStore.getDamage(), getReduce(level));
        buff.start();
        return true;
    }

    private static class Buff extends AbstractBuff {

        private final double damage;
        private final double reduce;

        public Buff(String name, double duration, Player target, double damage, double reduce) {
            super(name, duration, Arrays.stream(new Player[]{target}).toList(), BuffType.POSITIVE);
            this.damage = damage;
            this.reduce = reduce;
        }

        @Override
        public void start(Player player) {
            player.setInvisible(true);
            PlayerStore.getStore(player).setReduce(this.name, reduce);
        }

        @Override
        public void cancel(Player player) {
            player.setInvisible(false);
            PlayerStore.getStore(player).removeReduce(this.name);
        }

        @Override
        public void tick(Player player) {
            if (PlayerStore.getStore(player).isDead()) {
                AbstractBuff.remove(player, this.name);
                return;
            }
            super.tick(player);
            Location loc = player.getLocation();
            EffectStore.getInstance().spawnDustParticle(loc.clone().add(0, 1, 0), Color.PURPLE, 1f, 200, 2, 0.1, 2);
            for (int x = -4; x <= 4; x++)
                for (int z = -4; z <= 4; z++) {
                    if (x == 0 && z == 0)
                        continue;
                    Vector vector = new Vector(x, 1, z).normalize().multiply(4);
                    Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                    EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 1, 0, 0.1, 0);
                }
            for (Entity et : loc.getWorld().getNearbyEntities(loc, 4, 2, 4, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, damage);
            }
            loc.getWorld().playSound(loc, Sound.BLOCK_PORTAL_TRAVEL, 0.1f, 10f);
        }

        @Override
        public void end(Player player) {
            player.setInvisible(false);
            PlayerStore.getStore(player).removeReduce(this.name);
        }
    }


    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        list.addFirst(ChatColor.WHITE + String.format("스킬 시전 중 받는 모든 피해가 %.0f 감소하며, 총 %.0f의 피해를 준다.", getReduce(level) * 100d, getDamage(level)));
        list.addFirst(ChatColor.WHITE + "빠르게 검은 그림자 속에 몸을 감춘 뒤, 관통 이동하며 적을 공격한다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
