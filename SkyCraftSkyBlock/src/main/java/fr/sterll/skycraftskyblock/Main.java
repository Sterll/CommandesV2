package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.commands.CommandMain;
import fr.sterll.skycraftskyblock.gestion.database.DatabaseManager;
import fr.sterll.skycraftskyblock.utils.DBUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    private DBUtils dbUtils;

    @Override
    public void onEnable() {
        this.dbUtils = new DBUtils(this);
        register();
        DatabaseManager.initAllDatabaseConnection();
        Bukkit.getScheduler().runTaskTimer(this, new BukkitRunnable() {
            @Override
            public void run() {
                getDbUtils().saveToBDD();
            }
        }, 0, 72000);
    }

    public void register(){
        getCommand("is").setExecutor(new CommandMain(this));
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }

    public static Main getInstance(){
        return instance;
    }
}
