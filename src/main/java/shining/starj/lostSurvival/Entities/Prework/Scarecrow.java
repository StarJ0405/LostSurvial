package shining.starj.lostSurvival.Entities.Prework;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;

public class Scarecrow extends CustomEntities {

    public Scarecrow() {
        super("허수아비");
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
        net.minecraft.world.entity.Entity entity = ((CraftEntity) pre).getHandle();
        if (!(entity instanceof CustomArmorStand)) {
            Location loc = pre.getLocation();
            pre.remove();
            return spawn(loc);
        } else return pre;
    }

    static class CustomArmorStand extends ArmorStand {
        private int activeTick;
        private int tick;
        private boolean battle;
        private float damage;

        public CustomArmorStand(ServerLevel world) {
            super(EntityType.ARMOR_STAND, world);
            setCustomNameVisible(true);
            setCustomName(Component.literal("허수아비"));
        }

        @Override
        public void tick() {
            super.tick();
            if (battle) {
                this.tick++;
                this.activeTick++;
                if (this.tick == 120) {
                    this.damage = 0;
                    this.activeTick = 0;
                    this.tick = 0;
                    this.battle = false;
                    setCustomName(Component.literal("허수아비"));
                }
            }
        }

        @Override
        public InteractionResult interactAt(net.minecraft.world.entity.player.Player entityhuman, Vec3 vec3d, InteractionHand enumhand) {
            return InteractionResult.FAIL;
        }

        @Override
        public boolean hurt(DamageSource damagesource, float f) {
            if (damagesource.getEntity().getBukkitEntity() instanceof Player player && !player.getGameMode().equals(GameMode.CREATIVE)) {
                this.tick = 0;
                this.battle = true;
                damage += f;
                setCustomName(Component.literal(String.format("피해량 : %.0f (전투시간 : %.1fs)", damage, activeTick / 20d)));
                return super.hurt(damagesource, 0);
            }

            return super.hurt(damagesource, f);
        }
    }
}
