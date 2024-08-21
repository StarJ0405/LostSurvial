package shining.starj.lostSurvival.Entities.Monster;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_20_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Entities.StunAble;
import shining.starj.lostSurvival.Entities.User.Exp;
import shining.starj.lostSurvival.Entities.User.Money;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;

public class Minion extends CustomEntities {

    public Minion() {
        super("미니언");
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
        bukkit.setRotation(loc.getYaw(), loc.getPitch());
        return bukkit;
    }

    @Override
    public Entity renew(Entity pre) {
        pre.remove();
        return pre;
    }

    public static class CustomZombie extends ZombieVillager implements StunAble {
        private boolean stun;
        private int stunTick;

        public CustomZombie(Level world) {
            super(EntityType.ZOMBIE_VILLAGER, world);
            setCustomNameVisible(true);
            getAttribute(Attributes.MAX_HEALTH).setBaseValue(200d);
            getAttribute(Attributes.FOLLOW_RANGE).setBaseValue(100d);
            getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(45d);
            setHealth(200f);
            setBaby(true);
            this.stunTick = 0;
        }

        @Override
        public void tick() {
            if (GameStore.getInstance().getStatus().equals(GameStatus.START)) {
                if (this.stun)
                    this.stunTick--;
                else
                    super.tick();
                if (stun && this.stunTick <= 0) {
                    this.stun = false;
                    this.getBukkitEntity().setVelocity(new Vector());
                }
            }
        }

        @Override
        protected void registerGoals() {
            this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 0.5, false));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Zombie.class, 10, true, false, getAttackAblePredicate()));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Player.class, 10, true, false, getPlayerPredicate()));
        }

        @Override
        public SoundEvent getAmbientSound() {
            return null;
        }

        @Override
        public void die(DamageSource damagesource) {
            super.die(damagesource);
            Location loc = positionToLocation(level(), position());
            {
                Entity entity = CustomEntities.EXP.spawn(loc);
                if (((CraftEntity) entity).getHandle() instanceof Exp.CustomItemDisplay display) display.setExp(10);
            }
            {
                Entity entity = CustomEntities.MONEY.spawn(loc);
                if (((CraftEntity) entity).getHandle() instanceof Money.CustomItemDisplay display) display.setMoney(1);
            }
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
        public void setStunTick(int tick) {
            this.stunTick = tick;
            if (tick > 0)
                this.stun = true;
        }
    }
}
