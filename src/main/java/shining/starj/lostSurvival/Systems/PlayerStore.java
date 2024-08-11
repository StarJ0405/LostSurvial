package shining.starj.lostSurvival.Systems;

import lombok.Builder;
import lombok.Getter;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.Game.Characters.AbstractCharacter;
import shining.starj.lostSurvival.Game.Upgrades.AbstractUpgrade;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class PlayerStore {
    private static final HashMap<UUID, PlayerStore> maps = new HashMap<>();
    //
    private final Player player;
    private AbstractCharacter character;
    private double health;
    private final AbstractUpgrade[] upgrades;

    @Builder
    public PlayerStore(Player player) {
        this.player = player;
        this.health = 1d;
        this.upgrades = new AbstractUpgrade[6];
    }

    public void reset() {
        this.character = null;
        this.health = 1d;
        Arrays.fill(upgrades, null);
    }

    public static PlayerStore getStore(Player player) {
        UUID uuid = player.getUniqueId();
        if (!maps.containsKey(uuid))
            maps.put(uuid, new PlayerStore(player));
        return maps.get(uuid);
    }
}
