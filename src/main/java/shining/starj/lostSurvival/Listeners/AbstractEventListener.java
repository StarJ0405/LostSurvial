package shining.starj.lostSurvival.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Listeners.PreWork.*;

public abstract class AbstractEventListener implements Listener {

    private static final PluginManager plugin_manager = Bukkit.getPluginManager();

    public AbstractEventListener() {
        plugin_manager.registerEvents(this, Core.getCore());
    }

    public static void initial() {
        // 이벤트 선언

        // 글로벌
        // 플레이어

        // 사전 작업용
        BasicProtectListener.builder().build();
        BuffListener.builder().build();
        EntityListener.builder().build();
        GUIListener.builder().build();
        ItemInteractListener.builder().build();
        MessageListener.builder().build();
        PermissionListener.builder().build();
        GameListener.builder().build();
        TargetSelectorListener.builder().build();
        TimerListener.builder().build();
    }
}
