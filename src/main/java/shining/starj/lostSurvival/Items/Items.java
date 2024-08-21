package shining.starj.lostSurvival.Items;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public class Items {
    private final static List<Items> list = new ArrayList<Items>();
    // 기타
    public final static Items timer = Items.builder().key("timer").displayName(ChatColor.WHITE + "타이머").material(Material.CLOCK).fireResistant(true).maxStackSize(60).build();
    public final static Items empty = Items.builder().key("empty").model(1).displayName(ChatColor.GRAY + "빈 슬롯").material(Material.GRAY_STAINED_GLASS_PANE).maxStackSize(1).build();
    public final static Items select = Items.builder().key("select").model(1).displayName(ChatColor.GRAY + "선택").lores(new String[]{ChatColor.WHITE + "클릭시 직업으로 선택합니다."}).material(Material.GREEN_STAINED_GLASS_PANE).maxStackSize(1).build();
    public final static Items cancel = Items.builder().key("cancel").model(1).displayName(ChatColor.GRAY + "취소").material(Material.RED_STAINED_GLASS_PANE).maxStackSize(1).build();
    //
    protected static final NamespacedKey typeNamespaceKey = new NamespacedKey(Core.getCore(), "itemType");
    protected final String key;
    protected final String displayName;
    protected final Material material;
    protected String[] lores;
    protected Integer model;
    protected final boolean interact;
    protected final boolean fireResistant;
    protected final boolean glint;
    protected final boolean hideTooltip;
    protected final boolean unbreakable;
    protected int maxStackSize;

    public Items(String key, String displayName, Material material, String[] lores, Integer model, boolean interact, boolean fireResistant, boolean hideGlint, boolean hideTooltip, boolean unbreakable, int maxStackSize) {
        this.key = key;
        this.displayName = displayName;
        this.material = material;
        this.lores = lores;
        this.model = model != null ? 1000000 + model * 1000 : null;
        this.interact = interact;
        this.fireResistant = fireResistant;
        this.glint = hideGlint;
        this.hideTooltip = hideTooltip;
        this.unbreakable = unbreakable;
        this.maxStackSize = Math.min(99, Math.max(1, maxStackSize));
        //
        list.add(this);
    }

    public static ItemsBuilder builder() {
        return new ItemsBuilder();
    }

    public static class ItemsBuilder {
        protected String key;
        protected String displayName;
        protected Material material;
        protected String[] lores;
        protected Integer model;
        protected boolean interact;
        protected boolean fireResistant;
        protected boolean glint;
        protected boolean hideTooltip;
        protected boolean unbreakable;
        protected int maxStackSize;

        public ItemsBuilder key(String key) {
            this.key = key;
            return this;
        }

        public ItemsBuilder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public ItemsBuilder material(Material material) {
            this.material = material;
            return this;
        }

        public ItemsBuilder lores(String[] lores) {
            this.lores = lores;
            return this;
        }

        public ItemsBuilder model(Integer model) {
            this.model = model;
            return this;
        }

        public ItemsBuilder interact(boolean interact) {
            this.interact = interact;
            return this;
        }

        public ItemsBuilder fireResistant(boolean fireResistant) {
            this.fireResistant = fireResistant;
            return this;
        }

        public ItemsBuilder hideGlint(boolean glint) {
            this.glint = glint;
            return this;
        }

        public ItemsBuilder tooltip(boolean tooltip) {
            this.hideTooltip = tooltip;
            return this;
        }

        public ItemsBuilder unbreakable(boolean unbreakable) {
            this.unbreakable = unbreakable;
            return this;
        }

        public ItemsBuilder maxStackSize(int maxStackSize) {
            this.maxStackSize = maxStackSize;
            return this;
        }

        public Items build() {
            return new Items(key, displayName, material, lores, model, interact, fireResistant, glint, hideTooltip, unbreakable, maxStackSize);
        }
    }

    public ItemStack getItemStack() {
        ItemStack item = new ItemStack(this.material);
        ItemMeta meta = item.getItemMeta();
        if (this.model != null) meta.setCustomModelData(this.model);
        meta.setDisplayName(this.displayName);
        meta.getPersistentDataContainer().set(typeNamespaceKey, PersistentDataType.STRING, this.key);
        if (fireResistant)
            meta.setFireResistant(true);
        if (glint)
            meta.setEnchantmentGlintOverride(true);
        if (hideTooltip)
            meta.setHideTooltip(true);
        if (unbreakable)
            meta.setUnbreakable(true);
        if (lores != null)
            meta.setLore(Arrays.stream(lores).toList());
        meta.setMaxStackSize(maxStackSize);
        item.setItemMeta(meta);
        return item;
    }


    public ItemStack getItemStack(Player player) {
        return getItemStack();
    }

    public static Items valueOf(ItemStack item) {
        if (item != null && item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(typeNamespaceKey, PersistentDataType.STRING))
            return valueOf(item.getItemMeta().getPersistentDataContainer().get(typeNamespaceKey, PersistentDataType.STRING));
        return null;
    }

    public static Items valueOf(String key) {
        for (Items item : list)
            if (item.key.equals(key)) return item;
        return null;
    }

    public static List<Items> values() {
        return new ArrayList<>(Items.list);
    }

    public static void initial() {
        ConsumableItems.initial();
        DurableItems.initial();
    }
}
