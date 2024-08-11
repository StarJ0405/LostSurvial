package shining.starj.lostSurvival.Listeners.Entity;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;

public class ItemEntityListener extends AbstractEventListener {
    // 아이템이 사라질 때 발생
    @EventHandler
    public void Events(ItemDespawnEvent e){

    }
    // 아이템이 겹쳐서 합쳐질 때 발생
    @EventHandler
    public void Events(ItemMergeEvent e){

    }
    // 아이템이 소환되면 발생
    @EventHandler
    public void Events(ItemSpawnEvent e){

    }
}
