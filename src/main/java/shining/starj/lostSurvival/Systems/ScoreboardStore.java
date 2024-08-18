package shining.starj.lostSurvival.Systems;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class ScoreboardStore {
    private static final ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    private static final Scoreboard scoreboard = scoreboardManager.getMainScoreboard();
    @Getter
    private static final ScoreboardStore instance = new ScoreboardStore();
    private final Team team;

    private ScoreboardStore() {
        team = scoreboard.getTeam("users") == null ? scoreboard.registerNewTeam("users") : scoreboard.getTeam("users");
        team.setDisplayName("유저");
        team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
        team.setCanSeeFriendlyInvisibles(true);
    }

    public Objective getObjective(String name) {
        return scoreboard.getObjective(name);
    }

    public ScoreboardStore setObjective(String name, String displayName, DisplaySlot slot) {
        return setObjective(name, displayName, slot, RenderType.INTEGER);
    }

    public ScoreboardStore setObjective(String name, String displayName, DisplaySlot slot, RenderType type) {
        Objective objective = scoreboard.getObjective(name);
        objective.setDisplayName(displayName);
        if (slot != null)
            objective.setDisplaySlot(slot);
        objective.setRenderType(type);
        return this;
    }

    public ScoreboardStore removeObjective(String name) {
        scoreboard.getObjective(name).unregister();
        return this;
    }

    public ScoreboardStore joinPlayer(Player player) {
        if (!team.hasPlayer(player))
            team.addPlayer(player);
        return this;
    }
}
