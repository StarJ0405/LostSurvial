package shining.starj.lostSurvival.DBs;

import lombok.Builder;
import lombok.Getter;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Builder
@Getter
public class Upgrade extends AbstractTableInstance {
    private final long auto_id;
    private String player_name;
    private String upgrade;
    private int level;

    public Upgrade setLevel() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET level = " + Math.max(0, Math.min(shining.starj.lostSurvival.Game.Upgrade.valueOf(upgrade).getDefaultMaxLevel(), this.level)) + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, upgrade, level) values ('" + player_name + "','" + upgrade + "'," + Math.max(0, Math.min(shining.starj.lostSurvival.Game.Upgrade.valueOf(upgrade).getDefaultMaxLevel(), this.level)) + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public Upgrade addLevel() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int level = rs.getInt("level");
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET level = " + Math.min(shining.starj.lostSurvival.Game.Upgrade.valueOf(upgrade).getDefaultMaxLevel(), this.level + level) + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, upgrade, level) values ('" + player_name + "','" + upgrade + "'," + Math.max(0, Math.min(shining.starj.lostSurvival.Game.Upgrade.valueOf(upgrade).getDefaultMaxLevel(), this.level)) + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public Upgrade removeLevel() throws SQLException {
        if (conn == null) return this;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int level = rs.getInt("level");
                rs.close();
                try (PreparedStatement statement1 = conn.prepareStatement("UPDATE " + this.getClass().getSimpleName() + " SET level = " + Math.max(0, this.level - level) + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
                    statement1.executeUpdate();
                }
                return this;
            }
            rs.close();
        }
        try (PreparedStatement statement = conn.prepareStatement("INSERT INTO " + this.getClass().getSimpleName() + " (player_name, upgrade, level) values ('" + player_name + "','" + upgrade + "'," + 0 + ")")) {
            statement.executeUpdate();
        }
        return this;
    }

    public Integer getLevel() throws SQLException {
        if (conn == null) return null;
        try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + this.getClass().getSimpleName() + " WHERE  player_name='" + player_name + "' and upgrade='" + upgrade + "'")) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int level = rs.getInt("level");
                rs.close();
                return level;
            }
            rs.close();
        }
        return 0;
    }

    @Override
    public Upgrade createTable() throws SQLException {
        return (Upgrade) super.createTable();
    }
}
