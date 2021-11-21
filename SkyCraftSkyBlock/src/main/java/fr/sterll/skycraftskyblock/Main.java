package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.commands.CommandMain;
import fr.sterll.skycraftskyblock.database.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        register();
        DatabaseManager.initAllDatabaseConnection();
    }

    public void register(){
        getCommand("is").setExecutor(new CommandMain());
        getServer().getPluginManager().registerEvents(new Events(), this);
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }

    public static Main getInstance(){
        return instance;
    }
}
