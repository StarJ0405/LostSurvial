package shining.starj.lostSurvival.Entities;

import lombok.Getter;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.Prework.*;
import shining.starj.lostSurvival.Game.PlayerStore;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class CustomEntities {
    private static final List<CustomEntities> list = new ArrayList<>();
    protected final String name;

    public CustomEntities(String name) {
        this.name = name;
        list.add(this);
    }

    public static final Scarecrow SCARECROW = new Scarecrow();
    public static final Puppet PUPPET = new Puppet();
    public static final Shadow SHADOW = new Shadow();
    public static final DeadBody DEAD_BODY = new DeadBody();
    public static final Money MONEY = new Money();
    public static final Exp EXP = new Exp();

    // 소환 명령어 엔티티를 반환
    public abstract Entity spawn(@NotNull Location loc);

    public abstract Entity renew(Entity pre);

    public static CustomEntities valueOf(Entity entity) {
        if (entity.hasMetadata("key"))
            for (MetadataValue value : entity.getMetadata("key"))
                if (value.getOwningPlugin().equals(Core.getCore()))
                    return valueOf(value.asString());
        return null;
    }

    public static CustomEntities valueOf(String name) {
        for (CustomEntities entities : list)
            if (entities.name.equals(name))
                return entities;
        return null;
    }

    public static List<CustomEntities> values() {
        return list;
    }

    public static <T extends Entity> T setLiveTick(T entity, int second) {
        entity.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "live"), PersistentDataType.INTEGER, second * 4);
        return entity;
    }

    public static <T extends Entity> T setLife(T entity, double seconds) { // 현재 시간 비례
        entity.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "life"), PersistentDataType.LONG, System.currentTimeMillis() + (long) (seconds * 1000));
        return entity;
    }

    public static <T extends Entity> T setNpc(T entity) {
        entity.getPersistentDataContainer().set(new NamespacedKey(Core.getCore(), "npc"), PersistentDataType.BOOLEAN, true);
        return entity;
    }

    public static boolean isNpc(Entity entity) {
        return entity.getPersistentDataContainer().has(new NamespacedKey(Core.getCore(), "npc"));
    }

    public static void damage(LivingEntity vic, Player att, double damage) {
        if (!isNpc(vic)) {
            int ndt = vic.getNoDamageTicks();
            vic.setNoDamageTicks(10);
            vic.damage(damage, att);
            vic.setNoDamageTicks(ndt);
            PlayerStore.getStore(att).sendActionBar();
        }
    }

    public static Location positionToLocation(Level world, Vec3 vec) {
        return positionToLocation(world, vec, 0);
    }

    public static Location positionToLocation(Level world, Vec3 vec, double d) {
        return positionToLocation(world, vec, d, d, d);
    }

    public static Location positionToLocation(Level world, Vec3 vec, double dxz, double dy) {
        return positionToLocation(world, vec, dxz, dy, dxz);
    }

    public static Location positionToLocation(Level world, Vec3 vec, double dx, double dy, double dz) {
        return new Location(world.getWorld(), vec.x + dx, vec.y + dy, vec.z + dz);
    }

    public static void setStun(Entity pre, double seconds) {
        net.minecraft.world.entity.Entity entity = ((CraftEntity) pre).getHandle();
        if (entity instanceof StunAble stunAble)
            stunAble.setStun((int) (seconds * 20));
    }
}
