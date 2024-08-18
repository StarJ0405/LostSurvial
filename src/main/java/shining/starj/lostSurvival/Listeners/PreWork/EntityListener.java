package shining.starj.lostSurvival.Listeners.PreWork;

import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerLoadEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Events.Prework.TimerEvent;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;

@Builder
public class EntityListener extends AbstractEventListener {
    @EventHandler
    public void Events(TimerEvent e) {
        for (World world : Bukkit.getWorlds())
            for (Entity et : world.getEntities()) {
                PersistentDataContainer container = et.getPersistentDataContainer();
                NamespacedKey live = new NamespacedKey(Core.getCore(), "live");
                if (container.has(live, PersistentDataType.INTEGER)) {
                    int second = container.get(live, PersistentDataType.INTEGER) - 1;
                    if (second > 0) container.set(live, PersistentDataType.INTEGER, second);
                    else et.remove();
                }
                NamespacedKey life = new NamespacedKey(Core.getCore(), "life");
                if (container.has(life, PersistentDataType.LONG))
                    if (System.currentTimeMillis() > container.get(life, PersistentDataType.LONG))
                        et.remove();

            }
    }

    @EventHandler
    public void Events(ServerLoadEvent e) {
        for (World world : Bukkit.getWorlds())
            for (Entity et : world.getEntities())
                if (et.hasMetadata("remove")) for (MetadataValue value : et.getMetadata("remove"))
                    if (value.getOwningPlugin().equals(Core.getCore()) && value.asBoolean()) {
                        et.remove();
                        break;
                    }
    }

    @EventHandler
    public void Events(ChunkLoadEvent e) {
        for (Entity et : e.getChunk().getEntities())
            if (et.hasMetadata("remove")) for (MetadataValue value : et.getMetadata("remove"))
                if (value.getOwningPlugin().equals(Core.getCore()) && value.asBoolean()) {
                    et.remove();
                    break;
                }

    }

    @EventHandler
    public void Events(VehicleExitEvent e) {
        Vehicle vehicle = e.getVehicle();
        if (vehicle.hasMetadata("remove")) for (MetadataValue value : vehicle.getMetadata("remove"))
            if (value.getOwningPlugin().equals(Core.getCore()) && value.asBoolean()) {
                vehicle.remove();
                break;
            }
    }
}
