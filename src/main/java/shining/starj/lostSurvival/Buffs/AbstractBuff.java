package shining.starj.lostSurvival.Buffs;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class AbstractBuff {
    private static final List<AbstractBuff> list = new ArrayList<>();
    //
    protected String name;
    protected BuffType[] buffTypes;
    protected double duration;
    protected List<Player> targets = new ArrayList<>();

    public void start() {
        list.add(this);
        for (Player target : targets)
            start(target);
    }

    public void start(Player player) {

    }

    public void end() {
        list.remove(this);
        for (Player target : targets)
            end(target);
    }

    public void tick(Player player) {
        if (targets.isEmpty())
            list.remove(this);
    }

    public void end(Player player) {

    }

    public void cancel() {
        for (Player target : targets)
            cancel(target);
    }

    public void cancel(Player player) {

    }

    public void tickAll() {
        this.duration = duration - 0.25d;
        for (Player target : targets)
            tick(target);
        if (duration == 0)
            end();
    }

    public AbstractBuff(String name, double duration, List<Player> targets, BuffType... buffTypes) {
        this.name = name;
        this.buffTypes = buffTypes;
        this.duration = duration;
        this.targets = targets;
    }

    public static boolean has(Player player, String name) {
        for (AbstractBuff buff : list)
            if (buff.name.equals(name)) {
                if (buff.targets.contains(player))
                    return true;
            }
        return false;
    }

    public static void remove(Player player, String name) {
        for (AbstractBuff buff : list)
            if (buff.name.equals(name)) {
                List<Player> targets = buff.getTargets();
                Optional<Player> _target = targets.stream().filter(f -> f.getUniqueId().equals(player.getUniqueId())).findFirst();
                _target.ifPresent(buff::cancel);
                buff.targets = targets.stream().filter(f -> !f.getUniqueId().equals(player.getUniqueId())).toList();
            }
    }

    public static void remove(Player player, BuffType type) {
        for (AbstractBuff buff : list)
            if (Arrays.stream(buff.buffTypes).toList().contains(type)) {
                List<Player> targets = buff.getTargets();
                Optional<Player> _target = targets.stream().filter(f -> f.getUniqueId().equals(player.getUniqueId())).findFirst();
                _target.ifPresent(buff::cancel);
                buff.targets = targets.stream().filter(f -> !f.getUniqueId().equals(player.getUniqueId())).toList();
            }
    }

    public static void removeAll(Player player) {
        for (AbstractBuff buff : list) {
            List<Player> targets = buff.getTargets();
            Optional<Player> _target = targets.stream().filter(f -> f.getUniqueId().equals(player.getUniqueId())).findFirst();
            _target.ifPresent(buff::cancel);
            buff.targets = targets.stream().filter(f -> !f.getUniqueId().equals(player.getUniqueId())).toList();
        }
    }

    public static List<AbstractBuff> getList() {
        return new ArrayList<>(list);
    }
}
