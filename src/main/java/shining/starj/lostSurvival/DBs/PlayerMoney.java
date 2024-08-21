package shining.starj.lostSurvival.DBs;

import lombok.Builder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
public class PlayerMoney extends AbstractTableInstance {
    private final String player_name;
    private long money;

    public PlayerMoney setMoney() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE player_name='" + player_name + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET money = " + Math.max(0, this.money) + " WHERE  player_name='" + player_name + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, money) values ('" + player_name + "'," + Math.max(0, this.money) + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public PlayerMoney addMoney() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE player_name='" + player_name + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long money = rs.getLong("money");
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET money = " + (money + this.money) + " WHERE  player_name='" + player_name + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, money) values ('" + player_name + "'," + Math.max(0, this.money) + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public PlayerMoney removeMoney() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE player_name='" + player_name + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long money = rs.getLong("money");
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET money = " + Math.max(money - this.money, 0) + " WHERE  player_name='" + player_name + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, money) values ('" + player_name + "'," + 0 + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public Long getMoney() throws SQLException {
        if (conn == null) return null;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE player_name='" + player_name + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                long money = rs.getLong("money");
                rs.close();
                return money;
            }
            rs.close();
        }
        return 0L;
    }

    @Override
    public PlayerMoney createTable() throws SQLException {
        return (PlayerMoney) super.createTable();
    }
}
