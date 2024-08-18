package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.Buffs.AbstractBuff;
import shining.starj.lostSurvival.Buffs.BuffType;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Persona extends AbstractSkill {
    public Persona() {
        super("페르소나", Material.COAL, null, new Upgrade[]{Upgrade.COOL_DOWN, Upgrade.DURATION});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 15;
    }

    private double getDuration(int level) {
        return 7.5d;
    }

    @Override
    public boolean Use(Player player, int level) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        Location loc = player.getLocation();
        Entity puppet = CustomEntities.PUPPET.spawn(loc);
        Buff buff = new Buff(puppet, 5 * playerStore.getDuration(), player);
        buff.start();
        puppet.setRotation(loc.getYaw(), loc.getPitch());
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
            if (PlayerStore.getStore(player).isDead()) {
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
    public List<String> getLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("어둠 게이지를 가득 채우면 페르소나를 소환해 %.1f초 동안 은신할 수 있습니다.", getDuration(level)));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        return new ArrayList<>();
    }
}
