package shining.starj.lostSurvival.Entities.Prework;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.PlayerStore;

public class DeadBody extends CustomEntities {

    public DeadBody() {
        super("시체");
    }

    @Override
    public Entity spawn(@NotNull Location loc) {
        World world = loc.getWorld();
        ServerLevel level = ((CraftWorld) world).getHandle();
        CustomArmorStand entity = new CustomArmorStand(level);
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

    public static class CustomArmorStand extends ArmorStand {
        private Player owner;
        private int respawnTick;
        private int tick;

        public CustomArmorStand(ServerLevel world) {
            super(EntityType.ARMOR_STAND, world);
            setCustomNameVisible(true);
            setCustomName(Component.literal("시체"));
        }

        public void setOwner(Player owner, int respawnTick) {
            this.owner = owner;
            this.respawnTick = respawnTick;
            setCustomName(Component.literal(String.format(ChatColor.GREEN + owner.getName() + ChatColor.WHITE + "님의 시체 (%ds)", respawnTick)));
            ItemStack helmet = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) helmet.getItemMeta();
            meta.setOwningPlayer(owner);
            helmet.setItemMeta(meta);
            setItemSlot(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(helmet));
        }

        @Override
        public InteractionResult interactAt(net.minecraft.world.entity.player.Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
            return InteractionResult.FAIL;
        }

        @Override
        public void tick() {
            if (this.owner == null) {
                this.getBukkitEntity().remove();
                return;
            }
            if (!PlayerStore.getStore(owner).isDead()) {
                this.getBukkitEntity().remove();
                return;
            }
            super.tick();
            this.tick++;

            if (this.tick == respawnTick) {
                owner.teleport(this.getBukkitEntity().getLocation());
                PlayerStore.getStore(owner).respawn();
                this.getBukkitEntity().remove();
            }
            if (this.tick > respawnTick / 2) {
                if (!level().getWorld().getNearbyEntities(positionToLocation(level(), position()), 1, 1, 1, t -> t instanceof Player player && !PlayerStore.getStore(player).isDead()).isEmpty()) {
                    owner.teleport(this.getBukkitEntity().getLocation());
                    PlayerStore.getStore(owner).respawn();
                    this.getBukkitEntity().remove();
                }
                setCustomName(Component.literal(String.format(ChatColor.GREEN + owner.getName() + ChatColor.WHITE + "님의 시체 %s(부활가능 %.0fs)", ChatColor.GREEN, (respawnTick - tick) / 20d)));
            } else
                setCustomName(Component.literal(String.format(ChatColor.GREEN + owner.getName() + ChatColor.WHITE + "님의 시체 (%.0fs)", (respawnTick - tick) / 20d)));
        }

        @Override
        public boolean hurt(DamageSource damagesource, float f) {
            if (damagesource.getEntity().getBukkitEntity() instanceof Player player && !player.getGameMode().equals(GameMode.CREATIVE))
                return super.hurt(damagesource, 0);
            return super.hurt(damagesource, f);
        }
    }
}
