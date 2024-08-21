package shining.starj.lostSurvival.Entities.User;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;

public class Puppet extends CustomEntities {

    public Puppet() {
        super("분신");
    }

    @Override
    public Entity spawn(@NotNull Location loc) {
        World world = loc.getWorld();
        ServerLevel level = ((CraftWorld) world).getHandle();
        CustomZombie entity = new CustomZombie(level);
        entity.setPos(loc.getX(), loc.getY(), loc.getZ());
        level.addFreshEntity(entity);

        Entity bukkit = entity.getBukkitEntity();
        PersistentDataContainer container = bukkit.getPersistentDataContainer();
        container.set(new NamespacedKey(Core.getCore(), "key"), PersistentDataType.STRING, this.name);
        CustomEntities.setAttackAble(bukkit);
        CustomEntities.setNpc(bukkit);
        bukkit.setRotation(loc.getYaw(), loc.getPitch());
        return bukkit;
    }

    @Override
    public Entity renew(Entity pre) {
        pre.remove();
        return pre;
    }

    public static class CustomZombie extends Zombie {
        private Player player;

        public CustomZombie(Level world) {
            super(EntityType.ZOMBIE, world);
            setCustomNameVisible(true);
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(200d);
            setHealth(200f);
        }

        public void setOwner(Player player, double maxHealth) {
            this.player = player;
            setCustomName(Component.literal(ChatColor.GREEN + player.getName() + ChatColor.WHITE + "님의 분신" + String.format("( %.0f / %.0f )", getHealth(), getMaxHealth())));
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
            setHealth((float) maxHealth);
        }

        @Override
        public SoundEvent getAmbientSound() {
            return null;
        }

        @Override
        public void tick() {
            super.tick();
            setCustomName(Component.literal(ChatColor.GREEN + player.getName() + ChatColor.WHITE + "님의 분신" + String.format(" %s(%.0f / %.0f)", ChatColor.RED, getHealth(), getMaxHealth())));
        }

        @Override
        protected void registerGoals() {

        }

        @Override
        protected void addBehaviourGoals() {

        }

        @Override
        public void aiStep() {
            Vec3 vec3d1 = new Vec3(this.xxa, this.yya, this.zza);
            travel(vec3d1);
        }

        @Override
        protected boolean convertsInWater() {
            return false;
        }

        @Override
        protected boolean isSunSensitive() {
            return false;
        }

        @Override
        public boolean hurt(DamageSource damagesource, float f) {
            if (damagesource.getEntity() != null && (damagesource.getEntity().getBukkitEntity() instanceof Player player && !player.getGameMode().equals(GameMode.CREATIVE)))
                return super.hurt(damagesource, 0);
            return super.hurt(damagesource, f);
        }
    }
}
