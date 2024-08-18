package shining.starj.lostSurvival.Listeners.PreWork;

import lombok.Builder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import shining.starj.lostSurvival.Core;
import shining.starj.lostSurvival.Entities.CustomEntities;
import shining.starj.lostSurvival.Events.Prework.TimerEvent;
import shining.starj.lostSurvival.Game.Character;
import shining.starj.lostSurvival.Game.GameStatus;
import shining.starj.lostSurvival.Game.GameStore;
import shining.starj.lostSurvival.Game.PlayerStore;
import shining.starj.lostSurvival.Game.Skills.SkillInfo;
import shining.starj.lostSurvival.Listeners.AbstractEventListener;
import shining.starj.lostSurvival.Systems.ScoreboardStore;

@Builder
public class GameListener extends AbstractEventListener {
    @EventHandler
    public void Events(TimerEvent e) {
        GameStore gameStore = GameStore.getInstance();
        if (gameStore.getStatus().equals(GameStatus.START))
            for (Player player : gameStore.getPlayers())
                if (player.getGameMode().equals(GameMode.ADVENTURE)) {
                    PlayerStore playerStore = PlayerStore.getStore(player);
                    if (!playerStore.isDead())
                        for (SkillInfo skillInfo : playerStore.getSkillInfos())
                            if (skillInfo != null)
                                skillInfo.Use(player);
                }
    }

    @EventHandler
    public void Events(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if (e.getAction().equals(Action.PHYSICAL) && e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.FARMLAND) || !player.getGameMode().equals(GameMode.CREATIVE) && !e.getAction().equals(Action.PHYSICAL))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(PlayerDropItemEvent e) {
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
            PlayerStore playerStore = PlayerStore.getStore(player);
            if (playerStore.getCharacter() != null && playerStore.getUltimate() != null)
                playerStore.getUltimate().Use(player);
        }
    }

    @EventHandler
    public void Events(PlayerSwapHandItemsEvent e) {
        Player player = e.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE)) {
            e.setCancelled(true);
            PlayerStore playerStore = PlayerStore.getStore(player);
            if (playerStore.getCharacter() != null && playerStore.getIdentity() != null) {
                playerStore.getIdentity().Use(player);
            }
        }
    }

    @EventHandler
    public void Events(EntityDeathEvent e) {
        e.getDrops().clear();
    }

    @EventHandler
    public void Events(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(InventoryDragEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(InventoryInteractEvent e) {
        Player player = (Player) e.getWhoClicked();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(InventoryOpenEvent e) {
        Player player = (Player) e.getPlayer();
        if (!player.getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerStore playerStore = PlayerStore.getStore(player);
        playerStore.setCharacter(playerStore.getCharacter());
        ScoreboardStore.getInstance().joinPlayer(player);
        GameStore gameStore = GameStore.getInstance();
        if (gameStore.getStatus().equals(GameStatus.START)) {
            if (!gameStore.getPlayers().contains(player)) {
                player.setGameMode(GameMode.SPECTATOR);
                if (gameStore.getMap() != null)
                    player.teleport(gameStore.getMap().getWorld().getSpawnLocation());
            }
        } else if (gameStore.getStatus().equals(GameStatus.END))
            player.teleport(Bukkit.getWorld("world").getSpawnLocation());
    }

    @EventHandler
    public void Events(FoodLevelChangeEvent e) {
        e.setFoodLevel(19);
    }

    @EventHandler
    public void Events(EntitySpawnEvent e) {
        if (e.getEntity() instanceof ExperienceOrb)
            e.setCancelled(true);
    }

    @EventHandler
    public void Events(EntityRegainHealthEvent e) {
        if (e.getEntity() instanceof Player player) {
            PlayerStore playerStore = PlayerStore.getStore(player);
            playerStore.heal(e.getAmount());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Events(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player player) {
            PlayerStore playerStore = PlayerStore.getStore(player);
            if (e.getCause().equals(EntityDamageEvent.DamageCause.VOID) || e.getCause().equals(EntityDamageEvent.DamageCause.KILL))
                playerStore.kill();
            else if (!e.getCause().equals(EntityDamageEvent.DamageCause.FALL))
                playerStore.damage(e.getDamage());
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void Events(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        Bukkit.getScheduler().runTaskLater(Core.getCore(), () -> {
            for (Material material : Material.values())
                if (material.isItem() && player.hasCooldown(material)) {
                    player.setCooldown(material, player.getCooldown(material));
                }
        }, 0);

    }

    @EventHandler
    public void Events(ChunkLoadEvent e) {
        NamespacedKey key = new NamespacedKey(Core.getCore(), "key");
        for (Entity et : e.getChunk().getEntities()) {
            PersistentDataContainer container = et.getPersistentDataContainer();
            if (container.has(key, PersistentDataType.STRING)) {
                CustomEntities customEntities = CustomEntities.valueOf(container.get(key, PersistentDataType.STRING));
                customEntities.renew(et);
            }
        }
    }

    @EventHandler
    public void Events(PlayerToggleSneakEvent e) {
        Player player = e.getPlayer();
        PlayerStore playerStore = PlayerStore.getStore(player);
        if (playerStore.getCharacter() == null) {
            playerStore.setCharacter(Character.REAPER);
//            playerStore.setSkill(playerStore.getCharacter().getSkills()[Math.max(0, playerStore.getCharacter().getSkills().length - 1)]);
//            List<AbstractSkill> list = new ArrayList<>(List.of(playerStore.getCharacter().getSkills()));
//            Collections.shuffle(list);
//            for (AbstractSkill skill : list)
//                playerStore.setSkill(skill);
            player.setGameMode(GameMode.ADVENTURE);
            GameStore gameStore = GameStore.getInstance();
            gameStore.setStatus(GameStatus.START);
            gameStore.addPlayer(player);
            for (SkillInfo info : playerStore.getSkillInfos())
                if (info != null && info.getSkill() != null)
                    player.setCooldown(info.getSkill().getType(), 0);

        }
    }
}
