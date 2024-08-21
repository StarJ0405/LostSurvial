package shining.starj.lostSurvival.Commands.Prework;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import shining.starj.lostSurvival.Commands.AbstractCommand;
import shining.starj.lostSurvival.Commands.AbstractCommandLine;
import shining.starj.lostSurvival.Commands.AbstractTab;
import shining.starj.lostSurvival.Commands.SenderType;
import shining.starj.lostSurvival.GUIs.AbstractGUI;
import shining.starj.lostSurvival.GUIs.Prework.InfoGUI;
import shining.starj.lostSurvival.Game.Character;
import shining.starj.lostSurvival.Game.PlayerStore;

import java.util.ArrayList;
import java.util.List;

public class CharacterCommand extends AbstractCommand {

    public CharacterCommand() {
        super("character", new AbstractCommandLine[]{new Line1(), new Line2()}, new Tab1(), new Tab2(), new Tab3());
    }

    // c [player] info [type]
    // c [player] set [type]
    public static class Line1 extends AbstractCommandLine {

        public Line1() {
            super(SenderType.ops(), 1);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) {
                AbstractGUI.CHARACTER_GUI.openInv(off.getPlayer());
                return true;
            }
            return false;
        }
    }

    public static class Line2 extends AbstractCommandLine {

        public Line2() {
            super(SenderType.ops(), 3);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) {
                Player player = off.getPlayer();
                Character character = Character.valueFromKorean(args[2]);
                if (character != null) switch (args[1]) {
                    case "info" -> {
                        AbstractGUI.INFO_GUI.setInfo(player, new InfoGUI.CharacterInfo(character)).openInv(player);
                        return true;
                    }
                    case "set" -> {
                        PlayerStore.getStore(player).setCharacter(character);
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public static class Tab1 extends AbstractTab {

        public Tab1() {
            super(0, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            final List<String> list = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers())
                if (value.isBlank() || player.getName().toLowerCase().startsWith(value)) list.add(player.getName());
            return list;
        }
    }

    public static class Tab2 extends AbstractTab {

        public Tab2() {
            super(1, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            final List<String> list = new ArrayList<>();
            if (value.isBlank() || "info".startsWith(value.toLowerCase())) list.add("info");
            if (value.isBlank() || "set".startsWith(value.toLowerCase())) list.add("set");
            return list;
        }
    }

    public static class Tab3 extends AbstractTab {

        public Tab3() {
            super(2, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            final List<String> list = new ArrayList<>();
            for (Character character : Character.values())
                if (value.isBlank() || character.getName().startsWith(value)) list.add(character.getName());
            return list;
        }
    }
}
