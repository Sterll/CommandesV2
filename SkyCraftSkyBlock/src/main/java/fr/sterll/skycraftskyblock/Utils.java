package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.database.DatabaseManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Utils {

    //===================================
    // Fichier - Sauvegarde
    //===================================

    public static void saveFile(File file, FileConfiguration config){
        try{
            config.save(file);
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    //===================================
    // Base De Données - Creator
    //===================================

    public static void createNewPlayer(Player player){
        try{
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users_informations (uuid, name, canVote) VALUES (?,?,?)");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setString(2, player.getName());
            preparedStatement.setString(3, "1");

            preparedStatement.executeUpdate();

            player.sendMessage("§9Vos informations ont bien été créer !");

            connection.close();
        } catch (SQLException exception){
            exception.printStackTrace();
        }
    }

    public static void createNewIsland(Player player){
        try{
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
        } catch (SQLException exception){
            exception.printStackTrace();
        }
        player.sendMessage("§6Vous venez de créer votre île ! §9/is §8--> §bPour vous y téléporter");
    }

    public static boolean ifHaveAIsland(Player player){
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM islands_informations WHERE owner_uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next()){

                player.sendMessage("§9Vos informations ont bien été trouvé !");

            } else {
                connection.close();
                return false;
            }

            connection.close();
            return true;
        } catch (SQLException event){
            event.printStackTrace();
            return false;
        }
    }

    public static boolean ifHaveAAccount(Player player){
        try {
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users_informations WHERE uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeQuery();

            final ResultSet resultSet = preparedStatement.getResultSet();

            if(resultSet.next()){

                player.sendMessage("§9Vos informations ont bien été trouvé !");

            } else {
                return false;
            }

            connection.close();
            return true;
        } catch (SQLException event){
            event.printStackTrace();
            return false;
        }
    }

    //===================================
    // Base De Données - Modifier
    //===================================

    public static void DBSetIslandInfo(Player player, String setting, String settingsvalue) {
        try{
            final Connection connection = DatabaseManager.SkyCraftBDD.getDatabaseAccess().getConnection();
            final PreparedStatement preparedStatement = connection.prepareStatement("UPDATE islands_informations SET " + setting + "='" + settingsvalue + "' WHERE owner_uuid = ?");

            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.executeUpdate();

            connection.close();
        } catch (SQLException event){
            event.printStackTrace();
        }
    }

    // Get a value - With UUID

    public static String DBGetIslandStringInfoByUUID(UUID uuid, String geting) {
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

    public static int DBGetIslandIntInfoByUUID(UUID uuid, String geting) {
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

    public static boolean DBGetIslandBooleanInfoByUUID(UUID uuid, String geting) {
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

    public static String DBGetIslandStringInfoByIslandName(String island_name, String geting) {
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

    public static int DBGetIslandIntInfoByIslandName(String island_name, String geting) {
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

    public static boolean DBGetIslandBooleanInfoByIslandName(String island_name, String geting) {
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
}
