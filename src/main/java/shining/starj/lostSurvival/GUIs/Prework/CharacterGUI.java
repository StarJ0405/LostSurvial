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
import shining.starj.lostSurvival.Game.Character;
import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;

import java.util.Map;
import java.util.Set;

public class CharacterGUI extends AbstractGUI {
    public CharacterGUI() {
        super("character", "캐릭터창", InventorySize.Three);
    }


    @Override
    public Inventory openInv(Player player) {
        Inventory inv = super.openInv(player);
        Character[] characters = Character.values();
        for (int i = 0; i < characters.length; i++) {
            Character character = characters[i];
            AbstractCharacterSkill.Engraving engraving = character.getEngraving2();
            if (engraving != null && engraving.getUltimate() != null) inv.setItem(i, character.getInfo());
        }
        return inv;
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        Character[] characters = Character.values();
        if (rawSlot < view.getTopInventory().getSize() && rawSlot < characters.length) {
            player.closeInventory();
            AbstractGUI.INFO_GUI.setInfo(player, new InfoGUI.CharacterInfo(characters[rawSlot])).openInv(player);
        }
        return false;
    }

    @Override
    public boolean dragInventory(Player player, InventoryView view, ItemStack oldCursorItemStack, ItemStack cursorItemstack, DragType type, Set<Integer> slot, Set<Integer> rawSlot, Map<Integer, ItemStack> newItems) {
        return false;
    }
}
