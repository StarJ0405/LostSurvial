package shining.starj.lostSurvival.Commands.Prework;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Commands.AbstractCommand;
import shining.starj.lostSurvival.Commands.AbstractCommandLine;
import shining.starj.lostSurvival.Commands.AbstractTab;
import shining.starj.lostSurvival.Commands.SenderType;
import shining.starj.lostSurvival.Entities.CustomEntities;

import java.util.ArrayList;
import java.util.List;

public class SpawnCommand extends AbstractCommand {

    public SpawnCommand() {
        super("spawn", true, new AbstractCommandLine[]{new Line1(), new Line2()}, new Tab1(), new Tab2());
    }

    private static class Line1 extends AbstractCommandLine {

        public Line1() {
            super(SenderType.OP_PLayer, 1);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            if (sender instanceof Player player) {
                CustomEntities customEntities = CustomEntities.valueOf(args[0]);
                if (customEntities != null) {
                    customEntities.spawn(player.getLocation());
                    return true;
                }
            }
            return false;
        }
    }

    private static class Line2 extends AbstractCommandLine {

        public Line2() {
            super(SenderType.ops(), 4);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            CustomEntities customEntities = CustomEntities.valueOf(args[0]);
            if (customEntities != null) try {
                World world = Bukkit.getWorld(args[1]);
                double x = Double.parseDouble(args[2]);
                double y = Double.parseDouble(args[3]);
                double z = Double.parseDouble(args[4]);
                customEntities.spawn(new Location(world, x, y, z));
                return true;
            } catch (NumberFormatException ignored) {

            }
            return false;
        }
    }

    private static class Tab1 extends AbstractTab {
        public Tab1() {
            super(0, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            List<String> list = new ArrayList<>();
            for (CustomEntities customEntities : CustomEntities.values())
                if (value.isBlank() || customEntities.getName().toLowerCase().startsWith(value.toLowerCase()))
                    list.add(customEntities.getName());
            return list;
        }
    }

    private static class Tab2 extends AbstractTab {
        public Tab2() {
            super(1, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            List<String> list = new ArrayList<>();
            for (World world : Bukkit.getWorlds())
                if (value.isBlank() || world.getName().toLowerCase().startsWith(value.toLowerCase()))
                    list.add(world.getName());
            return list;
        }
    }
}
