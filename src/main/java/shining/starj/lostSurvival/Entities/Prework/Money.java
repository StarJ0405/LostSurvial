package shining.starj.lostSurvival.Entities.Prework;

import com.mojang.math.Transformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Display;
import net.minecraft.world.entity.EntityType;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.DBs.PlayerMoney;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;

import java.sql.SQLException;

public class Money extends CustomEntities {

    public Money() {
        super("돈");
    }

    @Override
    public Entity spawn(@NotNull Location loc) {
        World world = loc.getWorld();
        ServerLevel level = ((CraftWorld) world).getHandle();
        CustomItemDisplay entity = new CustomItemDisplay(level);
        entity.setPos(loc.getX(), loc.getY(), loc.getZ());
        level.addFreshEntity(entity);

        Entity bukkit = entity.getBukkitEntity();
        bukkit.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "key"), PersistentDataType.STRING, this.name);
        return bukkit;
    }

    @Override
    public Entity renew(Entity pre) {
        pre.remove();
        return null;
    }

    public static class CustomItemDisplay extends Display.ItemDisplay {
        private long money;

        public CustomItemDisplay(ServerLevel world) {
            super(EntityType.ITEM_DISPLAY, world);
            setMoney(1);
            setTransformation(new Transformation(new Vector3f(0, 0.12f, 0), new Quaternionf(0, 0, 0, 1), new Vector3f(0.5f, 0.5f, 0.5f), new Quaternionf(0, 0, 0, 1)));
            setGlowingTag(true);
            setGlowColorOverride(Color.ORANGE.asRGB());
        }

        public void setMoney(int money) {
            this.money = money;
            ItemStack item = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta meta = item.getItemMeta();
            meta.setCustomModelData(1000000 + money);
            item.setItemMeta(meta);
            setItemStack(CraftItemStack.asNMSCopy(item));
        }

        @Override
        public void tick() {
            final int xz = 2;
            Location now = positionToLocation(level(), position());
            for (Entity entity : level().getWorld().getNearbyEntities(now, xz * 3, 2, xz * 3, t -> {
                if (t instanceof Player player) {
                    PlayerStore playerStore = PlayerStore.getStore(player);
                    if (playerStore.isDead()) return false;
                    return player.getLocation().distance(now) <= xz * playerStore.getPickup();
                }
                return false;
            })) {
                Player player = (Player) entity;
                try {
                    PlayerMoney.builder().player_name(player.getName()).money(this.money).build().createTable().addMoney().getMoney();
                    player.getWorld().playSound(player, Sound.ENTITY_ITEM_PICKUP, 1f, 1f);
                } catch (SQLException ignored) {
                }
                this.getBukkitEntity().remove();
                return;
            }
        }
    }
}
