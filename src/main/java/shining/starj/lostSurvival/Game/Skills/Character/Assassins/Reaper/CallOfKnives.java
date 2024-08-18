package shining.starj.lostSurvival.Game.Skills.Character.Assassins.Reaper;

import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Effects.EffectStore;
import shining.starj.lostSurvival.Effects.ItemDisplayControl;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Upgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class CallOfKnives extends AbstractSkill {

    public CallOfKnives() {
        super("콜 오브 나이프", Material.YELLOW_DYE, Upgrade.COOL_DOWN, new Upgrade[]{Upgrade.DAMAGE, Upgrade.AREA, Upgrade.COOL_DOWN});
    }

    @Override
    public int getCoolDownTick(int level) {
        return 20 * 4;
    }


    @Override
    public boolean Use(Player player, int level) {
        PlayerStore playerStore = PlayerStore.getStore(player);
        double power = playerStore.getDamage();
        Location loc = player.getLocation().clone().add(0, 0.25d, 0);
        Vector dir = loc.getDirection().setY(0);
        if (dir.length() != 0) dir = dir.normalize().toBlockVector();
        RayTraceResult result = loc.getWorld().rayTraceBlocks(loc, dir, 5, FluidCollisionMode.ALWAYS);

        Location end;
        if (result == null) end = loc.clone().add(dir.clone().multiply(5)).getBlock().getLocation().add(0.5, 0.25, 0.5);
        else {
            Block target = result.getHitBlock();
            end = target.getLocation().subtract(dir).getBlock().getLocation().add(0.5, 0.25, 0.5);
            if (!end.getBlock().getType().isAir()) end.add(0, 1, 0);
        }
        end.setDirection(loc.getDirection());

        Display display = ItemDisplayControl.spawn(end).setItem(new ItemStack(Material.IRON_SWORD)).setLife(5).getDisplay();
        Transformation transformation = display.getTransformation();
        transformation.getTranslation().set(0f, 0.5625f, 0f);
        transformation.getLeftRotation().set(0f, 0f, 0.924f, 0.383f);
        display.setTransformation(transformation);
        EffectStore.getInstance().spawnDustParticle(end, Color.PURPLE, 1f, 64, 4, 0, 4);
        for (Entity et : end.getWorld().getNearbyEntities(end, 4, 2, 4, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
            LivingEntity le = (LivingEntity) et;
            damage(le, player, getSpawnDamage(level) * power);
        }
        int i = 1;
        for (; i <= getTick(level); i++)
            Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> Attack(player, getTickDamage(level) * power, display, level), 10L * i);
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            if (playerStore.isDead()) {
                display.remove();
                return;
            }
            for (Entity et : end.getWorld().getNearbyEntities(end, 4, 2, 4, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
                LivingEntity le = (LivingEntity) et;
                damage(le, player, getLastDamage(level) * power);
            }
            EffectStore.SimpleEffect.EXPLOSION.spawnParticle(end, 1);
            EffectStore.getInstance().spawnDustParticle(end, Color.PURPLE, 1f, 400, 2, 0.5, 2);
            display.remove();
            end.getWorld().playSound(end, Sound.ENTITY_GENERIC_EXPLODE, 1f, 1f);
        }, 10L * (i + 1));
        end.getWorld().playSound(end, Sound.BLOCK_TRIAL_SPAWNER_SPAWN_ITEM, 1f, 1f);
        return true;
    }

    private final HashMap<UUID, BukkitTask> tasks = new HashMap<>();

    public void applySlow(LivingEntity target, int level) {
        UUID uuid = target.getUniqueId();
        AttributeInstance instance = target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        AttributeModifier modifier = instance.getModifier(uuid);
        if (modifier == null)
            instance.addModifier(new AttributeModifier(uuid, 1 + "", -getReduce(level), AttributeModifier.Operation.ADD_SCALAR));
        else try {
            int stack = Integer.parseInt(modifier.getName()) + 1;
            if (stack < getMaxStack(level)) {
                instance.removeModifier(uuid);
                instance.addModifier(new AttributeModifier(uuid, stack + "", -getReduce(level) * stack, AttributeModifier.Operation.ADD_SCALAR));
            }
        } catch (NumberFormatException ignored) {

        }
        if (tasks.containsKey(uuid)) tasks.get(uuid).cancel();
        BukkitTask task = Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> target.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).removeModifier(uuid), (long) (20L * getDuration(level)));

        tasks.put(uuid, task);
    }


    private void Attack(Player player, double damage, Display display, int level) {
        if (PlayerStore.getStore(player).isDead()) {
            display.remove();
            return;
        }
        Location loc = display.getLocation();
        for (Entity et : loc.getWorld().getNearbyEntities(loc, 4, 2, 4, t -> !(t instanceof Player) && t instanceof LivingEntity)) {
            LivingEntity le = (LivingEntity) et;
            applySlow(le, level);
            damage(le, player, damage);
        }
        EffectStore.getInstance().spawnDustParticle(loc, Color.PURPLE, 1f, 100, 2, 0.5, 2);
        for (int x = -10; x <= 10; x++)
            for (int z = -10; z <= 10; z++) {
                if (x == 0 && z == 0)
                    continue;
                Vector vector = new Vector(x, 0, z).normalize().multiply(4);
                Location now = loc.clone().add(vector.getX(), vector.getY(), vector.getZ());
                EffectStore.getInstance().spawnDustParticle(now, Color.PURPLE, 1f, 1, 0);
            }
        loc.getWorld().playSound(loc, Sound.BLOCK_SOUL_SAND_FALL, 1f, 1f);
    }

    private double getSpawnDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private double getReduce(int level) {
        return 0.1;
    }

    private double getTickDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private int getTick(int level) {
        return 5;
    }

    private int getMaxStack(int level) {
        return 5;
    }

    private double getLastDamage(int level) {
        if (level < 0) return 0;
        return 30 + 10 * level;
    }

    private double getDuration(int level) {
        return 2;
    }

    @Override
    public List<String> getLore(int level) {
        List<String> list = super.getLore(level);
        list.addFirst(ChatColor.WHITE + String.format("이동속도 감소는 최대 %d회 중첩된다.", getMaxStack(level)));
        list.addFirst(ChatColor.WHITE + String.format("마지막에 폭발하여 %.0f의 속성 피해를 준다.", getLastDamage(level)));
        list.addFirst(ChatColor.WHITE + String.format("이후 주변을 피해를 주며 총 %.0f의 피해를 준다.", getTickDamage(level) * getTick(level)));
        list.addFirst(ChatColor.WHITE + String.format("주변에 %.0f의 피해를 주고 %.1f초간 이동속도를 %.0f 감소시킨다.", getSpawnDamage(level), getDuration(level), getReduce(level) * 100d));
        list.addFirst(ChatColor.WHITE + "정면에 사신의 검을 소환한다.");
        return list;
    }

    @Override
    public List<String> getUpgradeLore(int level) {
        List<String> list = new ArrayList<>();
        list.add(ChatColor.WHITE + String.format("피해량 %.0f -> %.0f", getLastDamage(level - 1), getLastDamage(level)));
        return list;
    }
}
