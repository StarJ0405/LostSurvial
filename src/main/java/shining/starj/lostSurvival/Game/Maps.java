package shining.starj.lostSurvival.Game;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.WorldType;
import shining.starj.lostSurvival.Systems.WorldStore;

@Getter
public enum Maps {
    cow("마수 골짜기"), mario("서커스")
    //
    ;

    //
    private final String name;
    private final World world;

    Maps(String name) {
        this.name = name;
        this.world = WorldStore.name(name()).type(WorldType.FLAT).doTileDrops(false).doEntityDrops(false).emptyChunkGenerator().doDaylightCycle(false).doWeatherCycle(false).spawnRadius(0).doMobSpawning(false).doPatrolSpawning(false).doTraderSpawning(false).doWardenSpawning(false).doImmediateRespawn(true).naturalRegeneration(false).keepInventory(true).build();
    }

}
