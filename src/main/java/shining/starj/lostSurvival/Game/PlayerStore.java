package shining.starj.lostSurvival.Game;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.entity.Entity;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.v1_20_R4.entity.CraftEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import shining.starj.lostSurvival.Atrributes.Attributes;
import shining.starj.lostSurvival.Buffs.AbstractBuff;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Entities.User.DeadBody;
import shining.starj.lostSurvival.Game.Skills.AbstractCharacterSkill;
import shining.starj.lostSurvival.Game.Skills.AbstractSkill;
import shining.starj.lostSurvival.Game.Skills.SkillInfo;
import shining.starj.lostSurvival.Items.Items;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerStore {
    private static final HashMap<UUID, PlayerStore> maps = new HashMap<>();
    //
    private final Player player;
    private Character character;
    private double health;
    private AbstractSkill identity;
    private AbstractSkill ultimate;
    private final SkillInfo[] skillInfos;
    private final UpgradeInfo[] upgrades;
    private int engraving;
    private final HashMap<String, Double> reduces;
    @Setter
    private boolean invulnerable;
    private boolean isDying;

    private PlayerStore(Player player) {
        this.player = player;
        this.health = 20d;
        this.skillInfos = new SkillInfo[5];
        this.upgrades = new UpgradeInfo[6];
        this.engraving = 0;
        this.reduces = new HashMap<>();
        this.isDying = false;
        player.setLevel(0);
        player.setExp(0f);
    }

    public void sendActionBar() {
        MessageStore.getMessageStore().sendActionbar(player, String.format("%s체력 : %.0f / %.0f                          ", ChatColor.RED, getHealth(), getMaxHealth()));
    }

    public void setCharacter(Character character) {
        player.getInventory().setHeldItemSlot(0);
        if (character == null) {
            reset();
            return;
        }
        GameStore gameStore = GameStore.getInstance();
        this.character = character;
        if (gameStore.getStatus().equals(GameStatus.END)) {
            heal(getMaxHealth());
            player.setLevel(0);
            player.setExp(0f);
        } else {
            player.setLevel(gameStore.getLevel());
            player.setExp(gameStore.getExp() * 1f / gameStore.getNeedExp());
        }
        Attributes.builder().attribute(Attribute.GENERIC_MOVEMENT_SPEED).amount(getMoveSpeed()).build().apply(player);
        Attributes.builder().attribute(Attribute.GENERIC_ATTACK_DAMAGE).amount(0).build().apply(player);
        this.skillInfos[0] = SkillInfo.builder().skill(character.getBasic()).build();
        Inventory inv = player.getInventory();
        inv.clear();
        if (gameStore.getStatus().equals(GameStatus.END)) this.engraving = 0;
        if (character.getBasic() != null) inv.setItem(0, skillInfos[0].getItemStack());
        for (int i = 1; i < skillInfos.length; i++)
            inv.setItem(i, Items.empty.getItemStack());
        for (int i = 0; i < upgrades.length; i++)
            inv.setItem(i + 9 * 3, Items.empty.getItemStack());
        if (character.getPassive() != null) inv.setItem(6, character.getPassive().getItemStack(0));
        updateItem();
        player.setFoodLevel(19);
    }

    public void updateItem() {
        Inventory inv = player.getInventory();
        int level = GameStore.getInstance().getLevel();
        switch (engraving) {
            case 1 -> {
                AbstractCharacterSkill.Engraving engraving = character.getEngraving1();
                if (engraving != null) {
                    if (engraving.getIdentity() != null) {
                        this.identity = engraving.getIdentity();
                        inv.setItem(7, this.identity.getItemStack(level, false));
                    } else
                        inv.setItem(7, Items.empty.getItemStack());
                    if (engraving.getUltimate() != null) {
                        this.ultimate = engraving.getUltimate();
                        inv.setItem(8, this.ultimate.getItemStack(level, false));
                    } else
                        inv.setItem(8, Items.empty.getItemStack());
                    inv.setItem(8 + 9 * 3, engraving.getChoice());
                }

            }
            case 2 -> {
                AbstractCharacterSkill.Engraving engraving = character.getEngraving2();
                if (engraving != null) {
                    if (engraving.getPassive() != null)
                        inv.setItem(6, engraving.getPassive().getItemStack(level));
                    else
                        inv.setItem(6, Items.empty.getItemStack());
                    if (engraving.getIdentity() != null) {
                        this.identity = engraving.getIdentity();
                        inv.setItem(7, this.identity.getItemStack(level, false));
                    } else
                        inv.setItem(7, Items.empty.getItemStack());
                    if (engraving.getUltimate() != null) {
                        this.ultimate = engraving.getUltimate();
                        inv.setItem(8, ultimate.getItemStack(level, false));
                    } else
                        inv.setItem(8, Items.empty.getItemStack());
                    inv.setItem(8 + 9 * 3, engraving.getChoice());
                }
            }
            default -> {
                if (character.getPassive() != null)
                    inv.setItem(6, character.getPassive().getItemStack(level));
                else
                    inv.setItem(6, Items.empty.getItemStack());
                if (level >= 3)
                    if (character.getIdentity() != null) {
                        this.identity = character.getIdentity();
                        inv.setItem(7, this.identity.getItemStack(level, false));
                    } else
                        inv.setItem(7, Items.empty.getItemStack());
                else
                    inv.setItem(7, Items.empty.getItemStack());
                inv.setItem(8, Items.empty.getItemStack());
                inv.setItem(8 + 9 * 3, Items.empty.getItemStack());
            }
        }
    }

    public void kill() {
        die();
    }

    public void setReduce(String name, double reduce) {
        reduces.put(name, reduce);
    }

    public void removeReduce(String name) {
        reduces.remove(name);
    }

    private double getReduces() {
        double armor = 1d - getArmor();
        double skill = 1d;
        for (double reduce : reduces.values())
            skill = Math.min(skill, 1 - reduce);
        return armor * skill;
    }

    public void damage(double damage) {
        if (damage <= 0 || invulnerable || isDying) return;
        player.sendHurtAnimation(0f);
        player.playSound(player, Sound.ENTITY_PLAYER_HURT, 1f, 1f);
        player.setNoDamageTicks(12);
        this.health -= damage * getReduces();
        if (health <= 0) die();
        else {
            double maxHealth = getMaxHealth();
            player.setHealth(Math.min(health < maxHealth ? 19 : 20, Math.max(1, health / maxHealth * 20d)));
            sendActionBar();
        }
    }

    public void heal(double heal) {
        if (heal <= 0 || isDying) return;
        this.health = Math.min(getMaxHealth(), this.health + heal);
        double maxHealth = getMaxHealth();
        player.setHealth(Math.min(health < maxHealth ? 19 : 20, Math.max(1, health / maxHealth * 20d)));
        sendActionBar();
    }

    public void setUpgrade(Upgrade upgrade) {
        int i = 0;
        for (; i < upgrades.length; i++) {
            UpgradeInfo info = upgrades[i];
            if (info == null) break;
            else if (info.getUpgrade().equals(upgrade)) {
                int level = info.getLevel() + 1;
                info.setLevel(level);
                info.getUpgrade().apply(player, level);
                player.getInventory().setItem(i + 9 * 3, info.getItemStack());
                return;
            }
        }
        if (i != upgrades.length - 1 && upgrades[i] != null) return;
        upgrades[i] = UpgradeInfo.builder().upgrade(upgrade).build();
        upgrade.apply(player, 1);
        for (i = 0; i < upgrades.length; i++)
            if (upgrades[i] != null) player.getInventory().setItem(i + 9 * 3, upgrades[i].getItemStack());
    }

    public void setSkill(AbstractSkill skill) {
        int i = 0;
        for (; i < skillInfos.length; i++) {
            SkillInfo info = skillInfos[i];
            if (info == null) break;
            else if (info.getSkill().equals(skill)) {
                if (info.getLevel() == info.getSkill().getMaxLevel())
                    info.setUpgraded(true);
                else
                    info.setLevel(info.getLevel() + 1);
                player.getInventory().setItem(i, info.getItemStack());
                return;
            }
        }
        if (i >= skillInfos.length) return;
        if (i != skillInfos.length - 1 && skillInfos[i] != null) return;
        skillInfos[i] = SkillInfo.builder().skill(skill).build();
        for (i = 0; i < skillInfos.length; i++)
            if (skillInfos[i] != null) player.getInventory().setItem(i, skillInfos[i].getItemStack());
    }

    public void setEngraving(int engraving) {
        this.engraving = engraving;
        updateItem();
    }

    /*
     * 능력치
     */
    public double getDamage() {
        Upgrade upgrade = Upgrade.DAMAGE;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1d + info.getValue() + defaultUpgrade;
        return 1d + defaultUpgrade;
    }

    public double getCriticalChance() {
        Upgrade upgrade = Upgrade.CRITICAL_CHANCE;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 0d + info.getValue() + defaultUpgrade;
        return 0d + defaultUpgrade;
    }

    public double getCoolDown() {
        Upgrade upgrade = Upgrade.COOL_DOWN;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1d - info.getValue() - defaultUpgrade;
        return Math.max(0d, 1d - defaultUpgrade);
    }

    public int getProjectTile() {
        Upgrade upgrade = Upgrade.PROJECTILE;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return (int) (info.getValue() + defaultUpgrade);
        return (int) (defaultUpgrade);
    }

    public float getMoveSpeed() {
        if (character == null) return 0.1f;
        Upgrade upgrade = Upgrade.MOVE_SPEED;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade))
                return character.getMoveSpeed() * (float) (1f + info.getValue() + defaultUpgrade);
        return character.getMoveSpeed() * (float) (1f + defaultUpgrade);
    }

    public double getMaxHealth() {
        if (character == null) return 20d;
        Upgrade upgrade = Upgrade.MAX_HEALTH;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade))
                return (character.getMaxHealth() + info.getValue()) * (1d + defaultUpgrade);
        return character.getMaxHealth() * (1d + defaultUpgrade);
    }

    public double getMaxHealthUp(double health) {
        Upgrade upgrade = Upgrade.MAX_HEALTH;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade))
                return (health + info.getValue()) * (1d + defaultUpgrade);
        return health * (1d + defaultUpgrade);
    }

    public double getArmor() {
        Upgrade upgrade = Upgrade.ARMOR;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return info.getValue() + defaultUpgrade;
        return defaultUpgrade;
    }

    public double getRegen() {
        Upgrade upgrade = Upgrade.REGEN;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1 + info.getValue() + defaultUpgrade;
        return 1 + defaultUpgrade;
    }

    public double getArea() {
        Upgrade upgrade = Upgrade.AREA;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1d + info.getValue() + defaultUpgrade;
        return 1d + defaultUpgrade;
    }

    public double getDuration() {
        Upgrade upgrade = Upgrade.DURATION;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1d + info.getValue() + defaultUpgrade;
        return 1d + defaultUpgrade;
    }

    public double getPickup() {
        Upgrade upgrade = Upgrade.PICKUP;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return 1d + info.getValue() + defaultUpgrade;
        return 1d + defaultUpgrade;
    }

    public double getExpUp() {
        Upgrade upgrade = Upgrade.EXP;
        double defaultUpgrade = upgrade.getDefaultValue(player);

        for (UpgradeInfo info : upgrades)
            if (info != null && info.getUpgrade().equals(upgrade)) return info.getValue() + defaultUpgrade;
        return defaultUpgrade;
    }

    /*
     * 종료
     */
    public void respawn() {
        this.isDying = false;
        player.setHealth(20);
        this.health = getMaxHealth();
        player.setGameMode(GameMode.ADVENTURE);
    }

    public int getRespawnTick() {
        return 20 * 10;
    }

    public void die() {
        player.setHealth(0);
        AbstractBuff.removeAll(player);
        this.isDying = true;
        player.setGameMode(GameMode.SPECTATOR);
        Entity entity = ((CraftEntity) CustomEntities.DEAD_BODY.spawn(player.getLocation())).getHandle();
        if (entity instanceof DeadBody.CustomArmorStand body) body.setOwner(player, getRespawnTick());
    }

    public boolean isDead() {
        return isDying;
    }

    public void reset() {
        player.getInventory().clear();
        this.character = null;
        this.health = 20d;
        Arrays.fill(upgrades, null);
        new Attributes(Attribute.GENERIC_MOVEMENT_SPEED, 0.1f).apply(player);
        Attributes.builder().attribute(Attribute.GENERIC_ATTACK_DAMAGE).amount(1d).build().apply(player);
        player.setExp(0);
        player.setLevel(0);
        player.setInvisible(false);
        invulnerable = false;
        this.engraving = 0;
        this.isDying = false;
        Location loc = Bukkit.getWorld("world").getSpawnLocation();
        player.teleport(loc);
        player.setGameMode(GameMode.SURVIVAL);
    }

    public static PlayerStore getStore(Player player) {
        UUID uuid = player.getUniqueId();
        if (!maps.containsKey(uuid)) maps.put(uuid, new PlayerStore(player));
        return maps.get(uuid);
    }
}
