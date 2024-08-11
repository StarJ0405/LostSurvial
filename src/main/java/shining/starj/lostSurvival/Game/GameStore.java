package shining.starj.lostSurvival.Game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.Game.Maps.AbstractMap;
import shining.starj.lostSurvival.Systems.PlayerStore;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class GameStore {
    @Getter
    private final static GameStore instance = new GameStore();
    private boolean status;
    private final List<Player> players;
    private AbstractMap map;

    public GameStore() {
        this.status = false;
        this.players = new ArrayList<>();
    }

    public void reset() {
        World world = Bukkit.getWorld("world");
        for(Player player : players) {
            player.teleport(world.getSpawnLocation());
            PlayerStore.getStore(player).reset();
        }
        players.clear();
        this.map = null;
    }
}
