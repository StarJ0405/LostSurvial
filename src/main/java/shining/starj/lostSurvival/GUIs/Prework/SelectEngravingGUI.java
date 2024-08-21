package shining.starj.lostSurvival.GUIs.Prework;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.InventorySize;
import shining.starj.lostSurvival.Game.Character;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Items.Items;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.util.*;

public class SelectEngravingGUI extends AbstractGUI {
    private static final HashSet<UUID> opens = new HashSet<>();

    public SelectEngravingGUI() {
        super("select_engraving", "직업 각인 선택", InventorySize.Three);
    }

    public boolean isClosed() {
        return opens.isEmpty();
    }

    public void choice() {
        Random r = new Random();
        for (UUID uuid : opens) {
            Player player = Bukkit.getPlayer(uuid);
            selectItemStack(player, 1 + r.nextInt(2));
            opens.remove(uuid);
            player.closeInventory();
            AbstractGUI.LEVEL_UP_GUI.openInv(player);
        }
    }

    private final static NamespacedKey key = new NamespacedKey(Core.getCore(), "type");

    public boolean selectItemStack(Player player, int num) {
        PlayerStore.getStore(player).setEngraving(num);
        player.playSound(player, Sound.ITEM_TOTEM_USE, 1f, 1f);
        return true;
    }

    @Override
    public Inventory openInv(Player player) {
        if (GameStore.getInstance().getStatus().equals(GameStatus.END)) return null;
        UUID uuid = player.getUniqueId();
        Inventory inv = super.openInv(player);
        Character character = PlayerStore.getStore(player).getCharacter();
        if (character == null) return null;
        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Items.empty.getItemStack());
        if (character.getEngraving1() != null) inv.setItem(3 + 9, character.getEngraving1().getChoice());
        if (character.getEngraving2() != null) inv.setItem(5 + 9, character.getEngraving2().getChoice());
        opens.add(uuid);
        return inv;
    }


    @Override
    public void closeInv(Player player) {
        super.closeInv(player);
        if (opens.contains(player.getUniqueId())) {
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> openInv(player), 0);
        }
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        if (switch (rawSlot) {
            case (3 + 9) -> selectItemStack(player, 1);
            case (5 + 9) -> selectItemStack(player, 2);
            default -> false;
        }) {
            opens.remove(player.getUniqueId());
            player.closeInventory();
            if (opens.isEmpty()) {
                GameStore gameStore = GameStore.getInstance();
                for (Player p : gameStore.getPlayers()) {
                    PlayerStore.getStore(p).updateItem();
                    AbstractGUI.LEVEL_UP_GUI.openInv(p);
                }
                for (MessageStore.BossBarInfo info : MessageStore.getBossBars())
                    if (info.getBar().getTitle().contains("직업 각인")) {
                        info.setTick(info.getMaxTick());
                        info.getBar().removeAll();
                    }
                int seconds = 30;
                if (AbstractGUI.LEVEL_UP_GUI.isClosed()) {
                    int exp = gameStore.getExp();
                    if (exp >= gameStore.getNeedExp())
                        gameStore.setExp(exp);
                    gameStore.setStatus(GameStatus.START);
                } else {
                    MessageStore.getMessageStore().sendBroadcastBossBar(true, seconds, "레벨업 선택 시간", BarColor.BLUE, BarStyle.SOLID);
                    gameStore.setRemain(4 * seconds);
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
