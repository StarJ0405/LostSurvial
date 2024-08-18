package shining.starj.lostSurvival.Game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.Game.Skills.SkillInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
public class GameStore {
    @Getter
    private final static GameStore instance = new GameStore();
    private GameStatus status;
    private final Set<Player> players;
    private Maps map;

    public GameStore() {
        this.status = GameStatus.END;
        this.players = new HashSet<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public void start(Maps map, List<Player> players) {
        this.map = map;
        this.players.addAll(players);
        for (Player player : players) {
            player.teleport(map.getWorld().getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
            PlayerStore playerStore = PlayerStore.getStore(player);
            for (SkillInfo info : playerStore.getSkillInfos())
                if (info != null && info.getSkill() != null)
                    player.setCooldown(info.getSkill().getType(), 0);
            playerStore.setExp(0);
            playerStore.setLevel(0);
        }
        for (Player player : Bukkit.getOnlinePlayers())
            if (!players.contains(player)) {
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(map.getWorld().getSpawnLocation());
            }
    }

    public void reset() {
        World world = Bukkit.getWorld("world");
        for (Player player : players) {
            player.teleport(world.getSpawnLocation());
            PlayerStore.getStore(player).reset();
        }
        players.clear();
        this.map = null;
    }
}
