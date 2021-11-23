package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.commands.CommandMain;
import fr.sterll.skycraftskyblock.database.DatabaseManager;
import fr.sterll.skycraftskyblock.utils.DBUtils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private DBUtils dbUtils;

    @Override
    public void onEnable() {
        this.dbUtils = new DBUtils(this);
        register();
        DatabaseManager.initAllDatabaseConnection();
    }

    public void register(){
        getCommand("is").setExecutor(new CommandMain(this));
        getServer().getPluginManager().registerEvents(new Events(this), this);
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }

    public static Main getInstance(){
        return instance;
    }
}
