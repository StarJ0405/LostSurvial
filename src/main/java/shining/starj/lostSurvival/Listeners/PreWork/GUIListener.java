package shining.starj.lostSurvival.Listeners.PreWork;

import lombok.Builder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;

@Builder
public class GUIListener extends AbstractEventListener {
    /*
     * GUI
     */
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void Events(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        AbstractGUI gui = AbstractGUI.getGUI(player);
        if (gui != null)
            e.setCancelled(gui.clickInventory(player, e.getView(), e.getClickedInventory(), e.getCurrentItem(), e.getCursor(), e.getClick(), e.getAction(), e.getSlot(), e.getRawSlot(), e.getHotbarButton(), e.getSlotType()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Events(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        AbstractGUI gui = AbstractGUI.getGUI(player);
        if (gui != null) gui.closeInv(player);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void Events(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        AbstractGUI gui = AbstractGUI.getGUI(player);
        if (gui != null)
            e.setCancelled(gui.dragInventory(player, e.getView(), e.getOldCursor(), e.getCursor(), e.getType(), e.getInventorySlots(), e.getRawSlots(), e.getNewItems()));
    }
}
