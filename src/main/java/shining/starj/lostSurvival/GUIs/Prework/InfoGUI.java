package shining.starj.lostSurvival.GUIs.Prework;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import shining.starj.lostSurvival.GUIs.AbstractFrameGUI;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.InventorySize;
import shining.starj.lostSurvival.Game.Character;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Items.Items;

import java.util.Map;
import java.util.Set;

public class InfoGUI extends AbstractFrameGUI {
    public InfoGUI() {
        super("info");
    }

    @Override
    public CharacterInfo getInfo(Player player) {
        return (CharacterInfo) super.getInfo(player);
    }

    @Override
    public InfoGUI setInfo(Player player, GUIInfo info) {
        return (InfoGUI) super.setInfo(player, info);
    }

    @Override
    public Inventory openInv(Player player) {
        Inventory inv = super.openInv(player);
        CharacterInfo info = getInfo(player);
        Character character = info.getCharacter();
        if (character == null) return null;
        inv.setItem(1, character.getInfo());
        if (character.getBasic() != null) inv.setItem(2, character.getBasic().getItemStack(0, false));
        if (character.getIdentity() != null) inv.setItem(3, character.getIdentity().getItemStack(0, false));

        if (GameStore.getInstance().getStatus().equals(GameStatus.END)) {
            inv.setItem(7, Items.cancel.getItemStack());
            inv.setItem(8, Items.select.getItemStack());
        } else inv.setItem(8, Items.cancel.getItemStack());

        {
            AbstractCharacterSkill.Engraving engraving = character.getEngraving1();
            if (engraving != null) {
                inv.setItem(1 + 9, engraving.getChoice());
                if (engraving.getIdentity() != null) inv.setItem(2 + 9, engraving.getIdentity().getItemStack(0, false));
                if (engraving.getUltimate() != null) inv.setItem(3 + 9, engraving.getUltimate().getItemStack(0, false));
            }
        }
        {
            AbstractCharacterSkill.Engraving engraving = character.getEngraving2();
            if (engraving != null) {
                inv.setItem(5 + 9, engraving.getChoice());
                if (engraving.getIdentity() != null) inv.setItem(6 + 9, engraving.getIdentity().getItemStack(0, false));
                if (engraving.getUltimate() != null) inv.setItem(7 + 9, engraving.getUltimate().getItemStack(0, false));
            }
        }
        AbstractSkill[] skills = character.getSkills();
        for (int i = 0; i < skills.length; i++)
            inv.setItem(9 * 3 + i, skills[i].getItemStack(0, false));

        return inv;
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        if (currentItemStack != null && !currentItemStack.getType().equals(Material.AIR))
            if (GameStore.getInstance().getStatus().equals(GameStatus.END)) {
                if (rawSlot == 7) {
                    player.closeInventory();
                    AbstractGUI.CHARACTER_GUI.openInv(player);
                } else if (rawSlot == 8) {
                    PlayerStore.getStore(player).setCharacter(getInfo(player).getCharacter());
                    player.closeInventory();
                }
            } else if (rawSlot == 8) {
                player.closeInventory();
                AbstractGUI.CHARACTER_GUI.openInv(player);
            }
        return false;
    }

    @Override
    public boolean dragInventory(Player player, InventoryView view, ItemStack oldCursorItemStack, ItemStack cursorItemstack, DragType type, Set<Integer> slot, Set<Integer> rawSlot, Map<Integer, ItemStack> newItems) {
        return false;
    }

    @Getter
    public static class CharacterInfo extends VariableInfo {
        private final Character character;

        public CharacterInfo(Character character) {
            super(ChatColor.AQUA + character.getName(), character.getSkills().length > 18 ? InventorySize.Six : InventorySize.Five);
            this.character = character;
        }


    }
}
