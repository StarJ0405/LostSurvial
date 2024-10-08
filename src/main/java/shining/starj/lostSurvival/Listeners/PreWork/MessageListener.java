package shining.starj.lostSurvival.Listeners.PreWork;

import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TextDisplay;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.metadata.MetadataValue;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Events.Prework.TimerEvent;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.util.List;
import java.util.UUID;

@Builder
public class MessageListener extends AbstractEventListener {
    @EventHandler
    public void Events(TimerEvent e) {
        List<MessageStore.BossBarInfo> list = MessageStore.getBossBars();
        for (int i = list.size() - 1; i >= 0; i--) {
            MessageStore.BossBarInfo info = list.get(i);
            BossBar bar = info.getBar();
            int tick = info.getTick();
            if (tick == info.getMaxTick()) {
                bar.removeAll();
                list.remove(i);
            } else {
                tick++;
                info.setTick(tick);
                if (info.isReverse())
                    bar.setProgress(1 - tick * 1.0D / info.getMaxTick());
                else
                    bar.setProgress(1 - (info.getMaxTick() - tick) * 1.0D / info.getMaxTick());
            }
        }
        for (World world : Bukkit.getWorlds())
            for (Entity entity : world.getEntities())
                if (entity instanceof TextDisplay && entity.hasMetadata("remove"))
                    for (MetadataValue value : entity.getMetadata("remove"))
                        if (value.getOwningPlugin().equals(Core.getCore()) && System.currentTimeMillis() >= value.asLong())
                            entity.remove();
        for (MessageStore.ActionBarInfo info : MessageStore.getActionBars())
            if (System.currentTimeMillis() >= info.endTime()) // 종료
                if (info.every()) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        if (info.predicate() == null || info.predicate().test(p))
                            MessageStore.getMessageStore().sendActionbar(p, "");
                } else for (UUID uuid : info.players()) {
                    OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
                    if (off.isOnline()) MessageStore.getMessageStore().sendActionbar(off.getPlayer(), "");
                }
            else { // 진행중
                if (info.every()) {
                    for (Player p : Bukkit.getOnlinePlayers())
                        if (info.predicate() == null || info.predicate().test(p))
                            MessageStore.getMessageStore().sendActionbar(p, info.msg());
                } else for (UUID uuid : info.players()) {
                    OfflinePlayer off = Bukkit.getOfflinePlayer(uuid);
                    if (off.isOnline()) MessageStore.getMessageStore().sendActionbar(off.getPlayer(), info.msg());
                }
            }
    }

    @EventHandler
    public void Events(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        for (MessageStore.BossBarInfo info : MessageStore.getBossBars())
            if (info.isEvery()) {
                if (info.getPredicate() == null || info.getPredicate().test(player)) info.getBar().addPlayer(player);
            } else {
                BossBar bar = info.getBar();
                if (info.getPlayers().contains(player.getUniqueId())) bar.addPlayer(player);
            }
    }

    @EventHandler
    public void Test(PlayerToggleSneakEvent e) {

    }
}
