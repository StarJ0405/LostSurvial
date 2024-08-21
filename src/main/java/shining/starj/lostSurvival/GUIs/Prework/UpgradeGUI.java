package shining.starj.lostSurvival.GUIs.Prework;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shining.starj.lostSurvival.DBs.PlayerMoney;
import shining.starj.lostSurvival.DBs.Upgrade;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.InventorySize;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UpgradeGUI extends AbstractGUI {
    public UpgradeGUI() {
        super("upgrade", "업그레이드", InventorySize.Five);
    }

    @Override
    public Inventory openInv(Player player) {
        Inventory inv = super.openInv(player);
        update(inv, player);
        return inv;
    }

    public void update(Inventory inv, Player player) {
        shining.starj.lostSurvival.Game.Upgrade[] upgrades = shining.starj.lostSurvival.Game.Upgrade.values();
        String player_name = player.getName();
        PlayerMoney pm = PlayerMoney.builder().player_name(player_name).build();
        long money;
        try {
            money = pm.getMoney();
        } catch (SQLException sqlException) {
            money = 0L;
        }
        for (int i = 0; i < upgrades.length; i++)
            try {
                shining.starj.lostSurvival.Game.Upgrade upgrade = upgrades[i];
                int level = Upgrade.builder().upgrade(upgrade.name()).player_name(player_name).build().createTable().getLevel();
                ItemStack item = upgrade.getUpgradeDefaultItemStack(level);
                if (level < upgrade.getDefaultMaxLevel()) {
                    ItemMeta meta = item.getItemMeta();
                    List<String> lore = meta.hasLore() ? meta.getLore() : new ArrayList<>();
                    lore.add(ChatColor.GRAY + String.format("현재 소지금 : %d", money));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                switch (i % 4) {
                    case 0 -> inv.setItem(1 + 9 * (i / 4) * 2, item);
                    case 1 -> inv.setItem(3 + 9 * (i / 4) * 2, item);
                    case 2 -> inv.setItem(5 + 9 * (i / 4) * 2, item);
                    case 3 -> inv.setItem(7 + 9 * (i / 4) * 2, item);
                }
            } catch (SQLException ignored) {
            }
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        if (GameStore.getInstance().getStatus().equals(GameStatus.END) && rawSlot < view.getTopInventory().getSize()) {
            int check = rawSlot % 9;
            int now = switch (check) {
                case 1 -> (rawSlot / 18) * 4;
                case 3 -> 1 + (rawSlot / 18) * 4;
                case 5 -> 2 + (rawSlot / 18) * 4;
                case 7 -> 3 + (rawSlot / 18) * 4;
                default -> -1;
            };
            if (now == -1) return false;
            shining.starj.lostSurvival.Game.Upgrade[] upgrades = shining.starj.lostSurvival.Game.Upgrade.values();
            String player_name = player.getName();
            shining.starj.lostSurvival.Game.Upgrade upgrade = upgrades[now];
            Upgrade db = Upgrade.builder().player_name(player_name).upgrade(upgrade.name()).level(1).build();
            int level;
            try {
                level = db.getLevel();
            } catch (SQLException ex) {
                level = 0;
            }
            if (level < upgrade.getDefaultMaxLevel()) {
                long price = upgrade.getDefaultUpgradeMoney(level);
                PlayerMoney pm = PlayerMoney.builder().player_name(player_name).money(price).build();
                try {
                    if (price <= pm.getMoney()) {
                        pm.removeMoney();
                        db.addLevel();
                        update(clicked, player);
                        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    }
                } catch (SQLException ignored) {
                    ignored.printStackTrace();
                }
            }

        }
        return false;
    }

    @Override
    public boolean dragInventory(Player player, InventoryView view, ItemStack oldCursorItemStack, ItemStack cursorItemstack, DragType type, Set<Integer> slot, Set<Integer> rawSlot, Map<Integer, ItemStack> newItems) {
        return false;
    }
}
