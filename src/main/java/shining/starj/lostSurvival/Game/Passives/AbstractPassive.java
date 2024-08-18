package shining.starj.lostSurvival.Game.Passives;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Getter
public abstract class AbstractPassive {
    private final String name;
    private final PassiveType type;

    public AbstractPassive(String name, PassiveType type) {
        this.name = name;
        this.type = type;
    }

    public ItemStack getItemStack(int level) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(this.name);
        meta.setLore(getLore(level));
        item.setItemMeta(meta);
        return item;
    }

    public abstract List<String> getLore(int level);
}
