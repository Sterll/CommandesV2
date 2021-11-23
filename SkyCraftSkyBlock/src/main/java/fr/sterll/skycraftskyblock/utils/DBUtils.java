package fr.sterll.skycraftskyblock.utils;

import fr.sterll.skycraftskyblock.Main;
import fr.sterll.skycraftskyblock.gestion.database.DatabaseManager;
import fr.sterll.skycraftskyblock.management.IslandManager;
import fr.sterll.skycraftskyblock.management.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.UUID;

public class DBUtils {

    private Main main;

    public DBUtils(Main main){
        this.main = main;
    }

    //===================================
    // Fichier - Sauvegarde
    //===================================

    public void saveFile(File file, FileConfiguration config) {
        try {
            config.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    //===================================
    // Base De Données - Sauvegarde
    //===================================

    public void saveToBDD(){
        Bukkit.broadcastMessage("§cEnregistrement des données dans la base de donnée ! §4Un coup de lag peut se faire ressentir !");
        for(PlayerManager playerManager : PlayerManager.Players.values()){
            saveToBDDAPlayer(playerManager);
        }

        for(IslandManager islandManager : IslandManager.Islands.values()){
            saveToBDDAIsland(islandManager);
        }
    }

    public void saveToBDDAPlayer(PlayerManager playerManager){
        DBSetUserInfo(Objects.requireNonNull(Bukkit.getPlayer(playerManager.getPlayername())), "canVote", String.valueOf(playerManager.getCanVote()));
        DBSetUserInfo(Objects.requireNonNull(Bukkit.getPlayer(playerManager.getPlayername())), "island_name", String.valueOf(playerManager.getIsland_name()));
    }

    public void saveToBDDAIsland(IslandManager islandManager){
        DBSetIslandInfo(islandManager.getIsland_name(), "owner_uuid", islandManager.getOwner_uuid().toString());
        DBSetIslandInfo(islandManager.getIsland_name(), "owner_name", islandManager.getOwner_name());
        DBSetIslandInfo(islandManager.getIsland_name(), "island_name", islandManager.getIsland_name());
        DBSetIslandInfo(islandManager.getIsland_name(), "biome", islandManager.getBiome());
        DBSetIslandInfo(islandManager.getIsland_name(), "opentovisite", String.valueOf(islandManager.getOpenToVisite()));
        DBSetIslandInfo(islandManager.getIsland_name(), "vote", String.valueOf(islandManager.getVote()));
        DBSetIslandInfo(islandManager.getIsland_name(), "level", String.valueOf(islandManager.getLevel()));
        DBSetIslandInfo(islandManager.getIsland_name(), "x_spawn", String.valueOf(islandManager.getX_spawn()));
        DBSetIslandInfo(islandManager.getIsland_name(), "y_spawn", String.valueOf(islandManager.getY_spawn()));
        DBSetIslandInfo(islandManager.getIsland_name(), "z_spawn", String.valueOf(islandManager.getZ_spawn()));
    }

    //===================================
    // Base De Données - Creator
    //===================================

    public void createNewPlayer(Player player) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_informations (uuid, name, canVote, island_name) VALUES (?,?,?,?)");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setInt(3, 1);
            preparedStatement.setString(4, "Aucune");

            preparedStatement.executeUpdate();

            player.sendMessage("§9Vos informations ont bien été créer !");

            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createNewIsland(Player player) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO islands_informations (owner_uuid, owner_name, island_name, biome, opentovisite, vote, level, x_spawn, y_spawn, z_spawn) VALUES (?,?,?,?,?,?,?,?,?,?)");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setString(3, "Île de " + player.getName());
            preparedStatement.setString(4, "Planes");
            preparedStatement.setBoolean(5, false);
            preparedStatement.setInt(6, 0);
            preparedStatement.setInt(7, 0);
            preparedStatement.setInt(8, 0);
            preparedStatement.setInt(9, 0);
            preparedStatement.setInt(10, 0);

            preparedStatement.executeUpdate();

            player.sendMessage("§9Vos informations ont bien été créer !");

            connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        player.sendMessage("§6Vous venez de créer votre île ! §9/is §8--> §bPour vous y téléporter");
    }

    public boolean ifHaveAIsland(Player player) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE owner_uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {

                player.sendMessage("§9Vos informations ont bien été trouvé !");

            } else {
                connection.close();
                return false;
            }

            connection.close();
            return true;
        } catch (SQLException event) {
            event.printStackTrace();
            return false;
        }
    }

    public boolean ifHaveAAccount(Player player) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_informations WHERE uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {

                player.sendMessage("§9Vos informations ont bien été trouvé !");

            } else {
                return false;
            }

            connection.close();
            return true;
        } catch (SQLException event) {
            event.printStackTrace();
            return false;
        }
    }

    //===================================
    // Base De Données - Modifier
    //===================================

    public void DBSetIslandInfo(String island_name, String setting, String settingsvalue) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE islands_informations SET " + setting + "='" + settingsvalue + "' WHERE island_name = ?");

            preparedStatement.setString(1, island_name);
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
        }
    }

    public void DBSetUserInfo(Player player, String setting, String settingsvalue) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users_informations SET " + setting + "='" + settingsvalue + "' WHERE uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
        }
    }

    // Get a value - With UUID

    public String DBGetIslandStringInfoByUUID(UUID uuid, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE owner_uuid = ?");

            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                String getted = resultSet.getString(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un String via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return null;
        }
        return null;
    }

    public int DBGetIslandIntInfoByUUID(UUID uuid, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE owner_uuid = ?");

            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int getted = resultSet.getInt(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un int via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return 0;
        }
        return 0;
    }

    public boolean DBGetIslandBooleanInfoByUUID(UUID uuid, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE owner_uuid = ?");

            preparedStatement.setString(1, String.valueOf(uuid));
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                boolean getted = resultSet.getBoolean(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un boolean via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return false;
        }
        return false;
    }

    // Get a value - With Island Name

    public String DBGetIslandStringInfoByIslandName(String island_name, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE island_name = ?");

            preparedStatement.setString(1, island_name);
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                String getted = resultSet.getString(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un String via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return null;
        }
        return null;
    }

    public int DBGetIslandIntInfoByIslandName(String island_name, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE island_name = ?");

            preparedStatement.setString(1, island_name);
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                int getted = resultSet.getInt(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un int via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return 0;
        }
        return 0;
    }

    public boolean DBGetIslandBooleanInfoByIslandName(String island_name, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE island_name = ?");

            preparedStatement.setString(1, island_name);
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                boolean getted = resultSet.getBoolean(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un boolean via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return false;
        }
        return false;
    }

    public String DBGetStringUserInfos(UUID uuid, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_informations WHERE uuid = ?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                String getted = resultSet.getString(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un String via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return null;
        }
        return null;
    }

    public boolean DBGetBooleanUserInfos(UUID uuid, String geting) {
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_informations WHERE uuid = ?");

            preparedStatement.setString(1, uuid.toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if (resultSet.next()) {
                boolean getted = resultSet.getBoolean(geting);
                connection.close();
                return getted;
            } else {
                System.out.println("Erreur lors du getting d'un boolean via le nom d'île");
            }

            connection.close();
        } catch (SQLException event) {
            event.printStackTrace();
            return false;
        }
        return false;
    }
}
