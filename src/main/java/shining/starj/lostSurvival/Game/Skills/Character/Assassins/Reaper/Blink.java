package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.bukkit.FluidCollisionMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftZombie;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Entities.Prework.Shadow;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.List;


public class Blink extends AbstractSkill {

    public Blink() {
        super("블링크", Material.GREEN_DYE, Upgrade.DURATION, new Upgrade[]{Upgrade.DAMAGE, Upgrade.PROJECTILE, Upgrade.COOL_DOWN});
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

    @Override
    public boolean Use(Player player, int level) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double damage = getDamage(level) * playerStore.getDamage();
        int projectile = 1 + playerStore.getProjectTile();
        Location loc = player.getLocation().clone().add(0, 0.25d, 0);
        Vector dir = loc.getDirection().setY(0);
        double maxDistance = 10d;
        RayTraceResult result = loc.getWorld().rayTraceBlocks(loc, dir, maxDistance, FluidCollisionMode.ALWAYS);

        Location end;
        if (result == null)
            end = loc.clone().add(dir.clone().multiply(maxDistance)).getBlock().getLocation().add(0.5, 0.25, 0.5);
        else {
            Block target = result.getHitBlock();
            end = target.getLocation().subtract(dir).getBlock().getLocation().add(0.5, 0.25, 0.5);
            if (!end.getBlock().getType().isAir()) end.add(0, 1, 0);
        }
        dir = dir.clone().multiply(-1);
        end.setDirection(dir);
        Entity shadow = CustomEntities.SHADOW.spawn(end);
        shadow = CustomEntities.setLiveTick(shadow, (int) (getDuration(level) * playerStore.getDuration()));
        shadow.customName(Component.keybind(ChatColor.GREEN + player.getName() + ChatColor.WHITE + "님의 그림자"));
        Shadow.CustomZombie custom = (Shadow.CustomZombie) ((CraftZombie) shadow).getHandle();
        custom.initial(player, damage, projectile);
        return true;
    }


    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        list.addFirst(ChatColor.WHITE + String.format("1초마다 분신이 플레이어 방향으로 표창을 던져 %.0f의 피해를 준다.", getDamage(level)));
        list.addFirst(ChatColor.WHITE + String.format("마우스 지점으로 %.1f초간 유지되는 그림자 분신을 소환한다.", getDuration(level)));
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getDamage(level - 1), getDamage(level)));
        return list;
    }
}
