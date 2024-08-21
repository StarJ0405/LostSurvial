package shining.starj.lostSurvival.Game.Skills.Characters.Assassins.Reaper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.Buffs.AbstractBuff;
import shining.starj.lostSurvival.Buffs.BuffType;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Entities.User.Puppet;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Persona extends AbstractSkill {
    public Persona() {
        super("페르소나", Material.COAL, null, new Upgrade[]{Upgrade.COOL_DOWN, Upgrade.DURATION, Upgrade.MAX_HEALTH});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 15;
    }

    private double getDuration(int level) {
        return 4.5d + level * 0.5d;
    }

    private double getMaxHealth(int level) {
        return 200d + level * 50d;
    }

    @Override
    public boolean Use(Player player, int level, boolean upgrade) {
        if (AbstractBuff.has(player, this.name)) return false;
        PlayerStore playerStore = PlayerStore.getStore(player);
        Location loc = player.getLocation();
        Entity entity = CustomEntities.PUPPET.spawn(loc);
        Buff buff = new Buff(entity, 5 * playerStore.getDuration(), player);
        buff.start();
        entity.setRotation(loc.getYaw(), loc.getPitch());
        Puppet.CustomZombie puppet = (Puppet.CustomZombie) ((CraftZombie) entity).getHandle();
        puppet.setOwner(player, playerStore.getMaxHealthUp(getMaxHealth(level)));
        for (LivingEntity livingEntity : player.getWorld().getLivingEntities())
            if (livingEntity instanceof Monster monster && monster.getTarget() == player)
                monster.setTarget((LivingEntity) entity);
        return true;
    }

    private static class Buff extends AbstractBuff {
        private final Entity puppet;

        public Buff(Entity puppet, double duration, Player target) {
            super("페르소나", duration, Arrays.stream(new Player[]{target}).toList(), BuffType.IDENTITY);
            this.puppet = puppet;
        }

        @Override
        public void start(Player player) {
            player.setInvisible(true);
        }

        @Override
        public void tick(Player player) {
            if (PlayerStore.getStore(player).isDead() || !GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                AbstractBuff.remove(player, this.name);
                puppet.remove();
                return;
            }
            super.tick(player);
            player.setInvisible(true);
        }

        @Override
        public void cancel(Player player) {
            player.setInvisible(false);
            if (!puppet.isDead()) puppet.remove();
        }

        @Override
        public void end(Player player) {
            player.setInvisible(false);
            if (!puppet.isDead()) puppet.remove();
        }
    }

    @Override
    public List<String> getLore(int level, boolean upgrade) {
        List<String> list = super.getLore(level, upgrade);
        if (level == 0) {
            list.add(ChatColor.WHITE + "잠시동안 은신할 수 있습니다.");
            list.add(ChatColor.WHITE + "지속시간동안 페르소나를 소환합니다.");
        } else {
            list.add(ChatColor.WHITE + String.format("%.1f초 동안 은신할 수 있습니다.", getDuration(level)));
            list.add(ChatColor.WHITE + String.format("지속시간동안 체력 %.0f의 페르소나를 소환합니다.", getMaxHealth(level)));
        }
        list.add(ChatColor.GRAY + "페르소나는 자신의 어그로를 가져가며 주변 적의 타겟이 됩니다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level, boolean upgrade) {
        return new ArrayList<>();
    }
}
