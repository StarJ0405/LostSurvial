package shining.starj.lostSurvival.GUIs.Prework;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.InventorySize;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class LevelUpGUI extends AbstractGUI {
    public LevelUpGUI() {
        super("level_up", "레벨업", InventorySize.Three);
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        return false;
    }

    @Override
    public boolean dragInventory(Player player, InventoryView view, ItemStack oldCursorItemStack, ItemStack cursorItemstack, DragType type, Set<Integer> slot, Set<Integer> rawSlot, Map<Integer, ItemStack> newItems) {
        return false;
    }

    @Override
    public boolean sortInventory(Player player, InventoryView view, Inventory inventory, List<ItemStack> sorted, InventoryType inventoryType, int rawSlot, int slot) {
        return false;
    }
}
