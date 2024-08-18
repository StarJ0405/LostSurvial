package shining.starj.lostSurvival.Entities.Prework;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;

import java.util.ArrayList;
import java.util.List;

public class Shadow extends CustomEntities {

    public Shadow() {
        super("그림자");
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
        CustomEntities.setNpc(bukkit);
        bukkit.teleport(loc);
        return bukkit;
    }

    @Override
    public Entity renew(Entity pre) {
        pre.remove();
        return pre;
    }


    public static class CustomZombie extends Zombie {
        private Player owner;
        private int tick;
        private double damage;
        private int projectile;

        public CustomZombie(Level world) {
            super(EntityType.ZOMBIE, world);
            setCustomNameVisible(true);
        }

        public void initial(Player owner, double damage, int projectile) {
            this.owner = owner;
            this.damage = damage;
            this.projectile = projectile;
        }

        @Override
        public void tick() {
            if (this.owner == null) {
                this.getBukkitEntity().remove();
                return;
            }
            if (PlayerStore.getStore(owner).isDead()) {
                this.getBukkitEntity().remove();
                return;
            }

            super.tick();
            if (GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                this.tick++;
                if (this.tick == 30) {
                    this.tick = 0;
                    Location start = positionToLocation(level(), position(), 0, 0.5d);
                    start.getWorld().playSound(start, Sound.ENTITY_ARROW_SHOOT, 2f, 1f);
                    List<Entity> list = new ArrayList<>();
                    if (projectile % 2 == 1) {
                        Vector dir = owner.getLocation().clone().add(0, 0.5d, 0).subtract(start).toVector().normalize().multiply(0.5d);
                        new Projectile(start, dir, owner, damage, list).runTaskTimer(Core.getCore(), 0, 1);
                        for (int i = 1; i <= projectile / 2; i++) {
                            new Projectile(start, dir.clone().rotateAroundY(Math.PI / 18 * i), owner, damage, list).runTaskTimer(Core.getCore(), 0, 1);
                            new Projectile(start, dir.clone().rotateAroundY(-Math.PI / 18 * i), owner, damage, list).runTaskTimer(Core.getCore(), 0, 1);
                        }
                    } else if (projectile % 2 == 0) {
                        Vector dir = owner.getLocation().clone().add(0, 0.5d, 0).subtract(start).toVector().normalize().multiply(0.5d);
                        for (int i = 1; i <= projectile / 2; i++) {
                            new Projectile(start, dir.clone().rotateAroundY(Math.PI / 36 * (i * 2 - 1)), owner, damage, list).runTaskTimer(Core.getCore(), 0, 1);
                            new Projectile(start, dir.clone().rotateAroundY(-Math.PI / 36 * (i * 2 - 1)), owner, damage, list).runTaskTimer(Core.getCore(), 0, 1);
                        }
                    }
                } else if (this.tick == 29) {
                    Location now = positionToLocation(level(), position());
                    now.setDirection(owner.getLocation().clone().subtract(now).toVector());
                    this.getBukkitEntity().teleport(now);
                }
            }
        }

        public static class Projectile extends BukkitRunnable {
            private final Location start;
            private int tick;
            private final int maxTick;
            private final Player owner;
            private final double damage;
            private final List<Entity> list;
            private final Vector dir;

            public Projectile(Location start, Vector dir, Player player, double damage, List<Entity> list) {
                this.start = start.clone();
                this.tick = 0;
                this.maxTick = 20;
                this.owner = player;
                this.damage = damage;
                this.list = list;
                this.dir = dir;
            }

            @Override
            public void run() {
                this.tick++;
                EffectStore.getInstance().spawnDustParticle(start, Color.BLACK, 1f, 1, 0);
                for (Entity et : start.getWorld().getNearbyEntities(start, 0.5, 1, 0.5, t -> !(t instanceof Player) && t instanceof LivingEntity && !list.contains(t))) {
                    LivingEntity le = (LivingEntity) et;
                    damage(le, owner, damage);
                    list.add(le);
                }
                // 종료 및 다음 준비
                if (this.tick >= this.maxTick) this.cancel();
                else start.add(dir);
            }
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
            if (damagesource.getEntity() != null && (damagesource.getEntity().getBukkitEntity() instanceof LivingEntity || (damagesource.getEntity().getBukkitEntity() instanceof Player player && !player.getGameMode().equals(GameMode.CREATIVE))))
                return super.hurt(damagesource, 0);
            return super.hurt(damagesource, f);
        }
    }
}
