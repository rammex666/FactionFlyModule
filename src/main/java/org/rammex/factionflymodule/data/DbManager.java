package org.rammex.factionflymodule.data;

import org.bukkit.Location;
import org.rammex.factionflymodule.FactionFlyModule;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;

public class DbManager {
    private static String dbname ;
    private static File dataFolder;

    public DbManager(String databaseName, File folder) {
        dbname = databaseName;
        dataFolder = folder;
    }

    public void initialize() {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM lands_loc");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);
        } catch (SQLException ex) {
            FactionFlyModule.instance.getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public static Connection getSQLConnection() {
        File folder = new File(dataFolder, dbname + ".db");
        if (!folder.exists()) {
            try {
                folder.createNewFile();
            } catch (IOException e) {
                FactionFlyModule.instance.getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection("jdbc:sqlite:" + folder);
        } catch (SQLException | ClassNotFoundException ex) {
            FactionFlyModule.instance.getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        }
        return null;
    }

    public void load() {
        try (Connection connection = getSQLConnection()) {
            Statement s = connection.createStatement();

            // Table pour les lands (chunks)
            String playerData = "CREATE TABLE IF NOT EXISTS player_data (" +
                    "`id` INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "`player_name` TEXT," +
                    "`time` INT," +
                    ");";

            s.executeUpdate(playerData);

            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }

    public static void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException ingored) {
        }
    }

    public static void addPlayerTime(String playerName, int time) {
        try (Connection connection = getSQLConnection()) {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO player_data (player_name, time) VALUES(?, ?);");
            ps.setString(1, playerName);
            ps.setInt(2, time);
            ps.executeUpdate();
            close(ps, null);
        } catch (SQLException ex) {
            FactionFlyModule.instance.getLogger().log(Level.SEVERE, "Unable to add player time", ex);
        }
    }

    public static void remove1sPlayerTime(String playerName) {
        String query = "UPDATE player_data SET time = time - 1 WHERE player_name = ?";
        try (Connection connection = getSQLConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ps.executeUpdate();
        } catch (SQLException ex) {
            FactionFlyModule.instance.getLogger().log(Level.SEVERE, "Unable to remove 1s player time", ex);
        }
    }

    public static int getPlayerTime(String playerName) {
        String query = "SELECT time FROM player_data WHERE player_name = ?";
        try (Connection connection = getSQLConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, playerName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("time");
            }
        } catch (SQLException ex) {
            FactionFlyModule.instance.getLogger().log(Level.SEVERE, "Unable to get player time", ex);
        }
        return 0;
    }
}
