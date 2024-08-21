package shining.starj.lostSurvival.Game;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.Game.Skills.SkillInfo;
import shining.starj.lostSurvival.Systems.MessageStore;

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
    private int level;
    private int exp;
    private int remain;

    public GameStore() {
        this.status = GameStatus.END;
        this.players = new HashSet<>();
        this.level = 0;
        this.exp = 0;
        this.remain = 0;
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
        setExp(0);
        setLevel(0);
        for (Player player : players) {
            player.teleport(map.getWorld().getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
            PlayerStore playerStore = PlayerStore.getStore(player);
            for (SkillInfo info : playerStore.getSkillInfos())
                if (info != null && info.getSkill() != null) player.setCooldown(info.getSkill().getType(), 0);
        }
        for (Player player : Bukkit.getOnlinePlayers())
            if (!players.contains(player)) {
                player.setGameMode(GameMode.SPECTATOR);
                player.teleport(map.getWorld().getSpawnLocation());
            }
    }

    public double getExpUp() {
        double value = 1d;
        for (Player player : players)
            value += PlayerStore.getStore(player).getExpUp();
        return value;
    }

    public void setLevel(int level) {
        if (!status.equals(GameStatus.START)) return;
        this.level = level;
        for (Player player : players)
            player.setLevel(level);
    }

    public int getNeedExp() {
        return (level + 1) * (level + 1) * 60;
    }

    public void setExp(int exp) {
        if (!status.equals(GameStatus.START)) return;
        if (exp >= getNeedExp()) {
            exp -= getNeedExp();
            setLevel(this.level + 1);
            setStatus(GameStatus.STOP);
            for (Player player : players) {
                PlayerStore.getStore(player).updateItem();
                player.getWorld().playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                if (level == 10) AbstractGUI.SELECT_ENGRAVING_GUI.openInv(player);
                else AbstractGUI.LEVEL_UP_GUI.openInv(player);
            }
            int seconds = 30;


            if (level == 10) if (AbstractGUI.SELECT_ENGRAVING_GUI.isClosed()) {
                for (Player player : players) {
                    PlayerStore.getStore(player).updateItem();
                    player.getWorld().playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 1f, 1f);
                    AbstractGUI.LEVEL_UP_GUI.openInv(player);
                }
                if (AbstractGUI.LEVEL_UP_GUI.isClosed()) {
                    if (exp >= getNeedExp()) setExp(exp);
                    setStatus(GameStatus.START);
                } else {
                    MessageStore.getMessageStore().sendBroadcastBossBar(true, seconds, "레벨업 선택 시간", BarColor.BLUE, BarStyle.SOLID);
                    this.remain = 4 * seconds;
                }
            } else {
                MessageStore.getMessageStore().sendBroadcastBossBar(true, seconds, "직업 각인 선택 시간", BarColor.PURPLE, BarStyle.SOLID);
                this.remain = 4 * seconds;
            }
            else if (AbstractGUI.LEVEL_UP_GUI.isClosed()) {
                if (exp >= getNeedExp()) setExp(exp);
                setStatus(GameStatus.START);
            } else {
                MessageStore.getMessageStore().sendBroadcastBossBar(true, seconds, "레벨업 선택 시간", BarColor.BLUE, BarStyle.SOLID);
                this.remain = 4 * seconds;
            }
        }
        this.exp = exp;
        for (Player player : players)
            player.setExp(Math.min(1f, exp * 1f / getNeedExp()));
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
