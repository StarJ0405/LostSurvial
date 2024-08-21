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
import shining.starj.lostSurvival.Game.Upgrade;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UpgradeCommand extends AbstractCommand {
    public UpgradeCommand() {
        super("upgrade", new AbstractCommandLine[]{new Line1(), new Line2(), new Line3()}, new Tab1(), new Tab2(), new Tab3());
    }

    static class Line1 extends AbstractCommandLine {

        public Line1() {
            super(1);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) {
                AbstractGUI.UPGRADE_GUI.openInv(off.getPlayer());
                return true;
            }
            return false;
        }
    }

    static class Line2 extends AbstractCommandLine {

        public Line2() {
            super(SenderType.ops(), 2);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline() && args[1].equalsIgnoreCase("reset")) {
                for (Upgrade upgrade : Upgrade.values())
                    try {
                        shining.starj.lostSurvival.DBs.Upgrade.builder().player_name(off.getName()).upgrade(upgrade.name()).level(0).build().setLevel();
                    } catch (SQLException ignored) {
                    }
                MessageStore.getMessageStore().sendMessage(sender, off.getName() + "님의 업그레이드를 초기화헀습니다.");
                return true;
            }
            return false;
        }
    }

    static class Line3 extends AbstractCommandLine {

        public Line3() {
            super(SenderType.ops(), 4);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) {
                Player player = off.getPlayer();
                Upgrade upgrade = Upgrade.valueFromKorean(args[2]);
                int level = Integer.parseInt(args[3]);
                if (upgrade != null) {
                    shining.starj.lostSurvival.DBs.Upgrade db = shining.starj.lostSurvival.DBs.Upgrade.builder().player_name(player.getName()).upgrade(upgrade.name()).level(level).build();
                    try {
                        switch (args[1].toLowerCase()) {
                            case "set" -> {
                                db.setLevel();
                                MessageStore.getMessageStore().sendMessage(sender, String.format("%s의 %s : %d", player.getName(), upgrade.getName(), db.getLevel()), false);
                                return true;
                            }
                            case "add" -> {
                                db.addLevel();
                                MessageStore.getMessageStore().sendMessage(sender, String.format("%s의 %s : %d", player.getName(), upgrade.getName(), db.getLevel()), false);
                                return true;
                            }
                            case "remove" -> {
                                db.removeLevel();
                                MessageStore.getMessageStore().sendMessage(sender, String.format("%s의 %s : %d", player.getName(), upgrade.getName(), db.getLevel()), false);
                                return true;
                            }
                        }
                    } catch (SQLException ignored) {
                    }
                }
            }
            return false;
        }
    }

    static class Tab1 extends AbstractTab {

        public Tab1() {
            super(0);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            List<String> list = new ArrayList<>();
            for (OfflinePlayer off : Bukkit.getOfflinePlayers())
                if (value.isBlank() || off.getName().toLowerCase().startsWith(value.toLowerCase()))
                    list.add(off.getName());
            return list;
        }
    }

    static class Tab2 extends AbstractTab {

        public Tab2() {
            super(1, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            List<String> list = new ArrayList<>();
            if (value.isBlank() || "reset".startsWith(value.toLowerCase()))
                list.add("reset");
            if (value.isBlank() || "set".startsWith(value.toLowerCase()))
                list.add("set");
            if (value.isBlank() || "add".startsWith(value.toLowerCase()))
                list.add("add");
            if (value.isBlank() || "remove".startsWith(value.toLowerCase()))
                list.add("remove");
            return list;
        }
    }

    static class Tab3 extends AbstractTab {

        public Tab3() {
            super(2, true);
        }

        @Override
        public List<String> getString(CommandSender sender, String value, String[] args) {
            List<String> list = new ArrayList<>();
            switch (args[1].toLowerCase()) {
                case "set", "add", "remove" -> {
                    for (Upgrade upgrade : Upgrade.values())
                        if (value.isBlank() || upgrade.getName().startsWith(value))
                            list.add(upgrade.getName());
                }
            }
            return list;
        }
    }
}
