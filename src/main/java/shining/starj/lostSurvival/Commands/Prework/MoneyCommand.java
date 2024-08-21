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
import shining.starj.lostSurvival.DBs.PlayerMoney;
import shining.starj.lostSurvival.Systems.MessageStore;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MoneyCommand extends AbstractCommand {
    public MoneyCommand() {
        super("money", new AbstractCommandLine[]{new Line1(), new Line2(), new Line3()}, new Tab1(), new Tab2());
    }

    record PM(String name, Long money) {
    }

    static class Line1 extends AbstractCommandLine {

        public Line1() {
            super(0);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            List<PM> list = new ArrayList<>();
            for (OfflinePlayer off : Bukkit.getOfflinePlayers())
                try {
                    long money = PlayerMoney.builder().player_name(off.getName()).build().getMoney();
                    list.add(new PM(off.getName(), money));
                } catch (SQLException ignored) {

                }
            list.sort(Comparator.comparingLong(pm -> pm.money));
            for (PM pm : list)
                MessageStore.getMessageStore().sendMessage(sender, String.format("%s : %d", pm.name, pm.money), false);
            return true;
        }
    }

    static class Line2 extends AbstractCommandLine {

        public Line2() {
            super(1);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) try {
                PlayerMoney playerMoney = PlayerMoney.builder().player_name(off.getName()).build();
                MessageStore.getMessageStore().sendMessage(sender, String.format("%s : %d", off.getName(), playerMoney.getMoney()), false);
                return true;
            } catch (SQLException ignored) {
            }
            return false;
        }
    }

    static class Line3 extends AbstractCommandLine {

        public Line3() {
            super(SenderType.ops(), 3);
        }

        @Override
        public boolean run(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
            OfflinePlayer off = Bukkit.getOfflinePlayer(args[0]);
            if (off.isOnline()) {
                Player player = off.getPlayer();
                long money = Long.parseLong(args[2]);
                String player_name = player.getName();
                PlayerMoney playerMoney = PlayerMoney.builder().player_name(player_name).money(money).build();
                try {
                    switch (args[1].toLowerCase()) {
                        case "give" -> {
                            playerMoney.createTable().addMoney();
                            MessageStore.getMessageStore().sendMessage(sender, String.format("%s : %d", player_name, playerMoney.getMoney()), false);
                            return true;
                        }
                        case "remove" -> {
                            playerMoney.createTable().removeMoney();
                            MessageStore.getMessageStore().sendMessage(sender, String.format("%s : %d", player_name, playerMoney.getMoney()), false);
                            return true;
                        }
                        case "set" -> {
                            playerMoney.createTable().setMoney();
                            MessageStore.getMessageStore().sendMessage(sender, String.format("%s : %d", player_name, playerMoney.getMoney()), false);
                            return true;
                        }
                    }
                } catch (SQLException ignored) {
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
            if (value.isBlank() || "set".startsWith(value.toLowerCase()))
                list.add("set");
            if (value.isBlank() || "give".startsWith(value.toLowerCase()))
                list.add("give");
            if (value.isBlank() || "remove".startsWith(value.toLowerCase()))
                list.add("remove");
            return list;
        }
    }
}
