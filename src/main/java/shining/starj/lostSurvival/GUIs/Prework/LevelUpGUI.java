package shining.starj.lostSurvival.GUIs.Prework;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.InventorySize;
import shining.starj.lostSurvival.Game.*;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Skills.SkillInfo;
import shining.starj.lostSurvival.Items.Items;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.util.*;

public class LevelUpGUI extends AbstractGUI {
    private static final HashMap<UUID, List<ItemStack>> opens = new HashMap<>();

    public LevelUpGUI() {
        super("level_up", "레벨업", InventorySize.Three);
    }

    public boolean isClosed() {
        return opens.isEmpty();
    }

    public void choice() {
        Set<UUID> keys = opens.keySet();
        for (UUID uuid : keys) {
            Player player = Bukkit.getPlayer(uuid);

            List<ItemStack> list = opens.get(uuid);
            if (!list.isEmpty()) {
                Collections.shuffle(list);
                selectItemStack(player, list.getFirst());
            }
            opens.remove(uuid);
            player.closeInventory();
        }
        GameStore gameStore = GameStore.getInstance();
        gameStore.setExp(gameStore.getExp());
    }

    private final static NamespacedKey key = new NamespacedKey(Core.getCore(), "type");

    public boolean selectItemStack(Player player, ItemStack item) {
        if (item == null)
            return false;
        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        if (container.has(key, PersistentDataType.STRING)) {
            String value = container.get(key, PersistentDataType.STRING);
            if (value.contains("$")) {
                String[] splits = value.split("\\$");
                String type = splits[0];
                PlayerStore playerStore = PlayerStore.getStore(player);
                if (type.equalsIgnoreCase("upgrade")) {
                    playerStore.setUpgrade(Upgrade.valueOf(splits[1]));
                    return true;
                } else if (type.equalsIgnoreCase("skill")) {
                    playerStore.setSkill(AbstractSkill.valueOf(splits[1]));
                    return true;
                }
            }
        }
        return false;
    }

    private List<ItemStack> getList(Player player) {
        List<ItemStack> list = new ArrayList<>();
        PlayerStore playerStore = PlayerStore.getStore(player);
        UpgradeInfo[] upgradeInfos = playerStore.getUpgrades();
        List<Upgrade> upgrades = new ArrayList<>(List.of(Upgrade.values()));
        for (UpgradeInfo upgradeInfo : upgradeInfos)
            if (upgradeInfo != null) {
                if (upgradeInfo.getLevel() < upgradeInfo.getUpgrade().getMaxLevel())
                    list.add(upgradeInfo.getUpgradeItemStack());
                upgrades.remove(upgradeInfo.getUpgrade());
            }
        if (upgradeInfos[upgradeInfos.length - 1] == null) for (Upgrade upgrade : upgrades)
            list.add(upgrade.getUpgradeItemStack(0));

        SkillInfo[] skillInfos = playerStore.getSkillInfos();
        List<AbstractSkill> skills = new ArrayList<>(List.of(playerStore.getCharacter().getSkills()));
        for (SkillInfo skillInfo : skillInfos)
            if (skillInfo != null) {
                int level = skillInfo.getLevel();
                int maxLevel = skillInfo.getSkill().getMaxLevel();
                if (level < maxLevel) {
                    list.add(skillInfo.getUpgradeItemStack());
                } else if (!skillInfo.isUpgraded() && level == maxLevel && Arrays.stream(upgradeInfos).filter(Objects::nonNull).map(UpgradeInfo::getUpgrade).toList().contains(skillInfo.getSkill().getMain()))
                    list.add(skillInfo.getUpgradeItemStack(true));
                skills.remove(skillInfo.getSkill());
            }
        if (skillInfos[skillInfos.length - 1] == null) for (AbstractSkill skill : skills)
            list.add(skill.getUpgradeItemStack(0, false));
        return list;
    }

    @Override
    public Inventory openInv(Player player) {
        if (GameStore.getInstance().getStatus().equals(GameStatus.END)) return null;
        UUID uuid = player.getUniqueId();
        boolean has = opens.containsKey(uuid);
        List<ItemStack> list = has ? opens.get(uuid) : getList(player);
        if (list.isEmpty()) return null;
        Inventory inv = super.openInv(player);

        for (int i = 0; i < inv.getSize(); i++)
            inv.setItem(i, Items.empty.getItemStack());

        switch (list.size()) {
            case 1 -> {
                inv.setItem(4 + 9, list.getFirst());
                if (!has) opens.put(uuid, list);
            }
            case 2 -> {
                inv.setItem(3 + 9, list.getFirst());
                inv.setItem(5 + 9, list.getLast());
                if (!has) opens.put(uuid, list);
            }
            case 3 -> {
                inv.setItem(2 + 9, list.get(0));
                inv.setItem(4 + 9, list.get(1));
                inv.setItem(6 + 9, list.get(2));
                if (!has) opens.put(uuid, list);
            }
            default -> {
                Collections.shuffle(list);
                for (int i = 0; i < 3; i++)
                    inv.setItem((i + 1) * 2 + 9, list.get(i));
                if (!has) opens.put(uuid, list.subList(0, 3));
            }
        }
        return inv;
    }


    @Override
    public void closeInv(Player player) {
        super.closeInv(player);
        if (opens.containsKey(player.getUniqueId())) {
            player.closeInventory();
            Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> openInv(player), 0);
        }
    }

    @Override
    public boolean clickInventory(Player player, InventoryView view, Inventory clicked, ItemStack currentItemStack, ItemStack cursorItemstack, ClickType type, InventoryAction action, int slot, int rawSlot, int hotbarButton, InventoryType.SlotType slotType) {
        if (selectItemStack(player, currentItemStack)) {
            opens.remove(player.getUniqueId());
            player.closeInventory();
            if (opens.isEmpty()) {
                GameStore gameStore = GameStore.getInstance();
                gameStore.setStatus(GameStatus.START);
                gameStore.setRemain(0);
                for (MessageStore.BossBarInfo info : MessageStore.getBossBars())
                    if (info.getBar().getTitle().contains("레벨업")) {
                        info.setTick(info.getMaxTick());
                        info.getBar().removeAll();
                    }
                gameStore.setExp(gameStore.getExp());
            }
        }
        return false;
    }

    @Override
    public boolean dragInventory(Player player, InventoryView view, ItemStack oldCursorItemStack, ItemStack cursorItemstack, DragType type, Set<Integer> slot, Set<Integer> rawSlot, Map<Integer, ItemStack> newItems) {
        return false;
    }
}
