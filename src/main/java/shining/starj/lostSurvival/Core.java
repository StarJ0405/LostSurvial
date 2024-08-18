package shining.starj.lostSurvival;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import shining.starj.lostSurvival.Commands.AbstractCommand;
import shining.starj.lostSurvival.DBs.AbstractTableInstance;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Events.AbstractEvent;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.Game.Maps;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Items.Items;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;
import shining.starj.lostSurvival.Recipes.CustomRecipe;
import shining.starj.lostSurvival.Systems.MessageStore;
import shining.starj.lostSurvival.Systems.OsType;
import shining.starj.lostSurvival.Systems.ScoreboardStore;
import shining.starj.lostSurvival.Systems.WorldStore;

public class Core extends JavaPlugin {


    @Getter
    private static Core core;
    @Getter
    private static OsType osType;

    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 시작되었습니다.");
        core = this;
        osType = OsType.getOsType();
        //
        AbstractEvent.initial(); // 이벤트
        AbstractCommand.initial(); // 명령어
        Items.initial(); // 아이템
        CustomRecipe.initial(); // 레시피
        AbstractGUI.initial(); // GUI
        AbstractEventListener.initial(); // 이벤트 리스너
        AbstractTableInstance.connect(); // DB
        CustomEntities.values();
        //
        for (String name : new String[]{"world", "world_nether", "world_the_end"})
            WorldStore.name(name).doDaylightCycle(false).doWeatherCycle(false).doTileDrops(false).doEntityDrops(false).spawnRadius(0).doMobSpawning(false).doPatrolSpawning(false).doTraderSpawning(false).doWardenSpawning(false).doImmediateRespawn(true).naturalRegeneration(false).keepInventory(true).build();
        Maps.values();
        //
        Bukkit.getOnlinePlayers().forEach(player -> {
            PlayerStore.getStore(player).reset();
            ScoreboardStore.getInstance().joinPlayer(player);
        });
        NamespacedKey key = new NamespacedKey(this, "key");
        Bukkit.getWorlds().forEach(world -> world.getEntities().forEach(et -> {
            PersistentDataContainer container = et.getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                CustomEntities customEntities = CustomEntities.valueOf(container.get(key, PersistentDataType.STRING));
                customEntities.renew(et);
            }
        }));
        //
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 정상적으로 불러와졌습니다.");
    }


    public void onDisable() {
        // 초기화
        for (MessageStore.BoosBarInfo info : MessageStore.getBossBars())
            info.bar().removeAll();
        // DB연결 종료
        AbstractTableInstance.disconnect();
        //
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + this.getName() + "이 종료되었습니다.");
    }

}
