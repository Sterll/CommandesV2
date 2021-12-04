package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.commands.CommandIsland;
import fr.sterll.skycraftskyblock.gestion.database.DatabaseManager;
import fr.sterll.skycraftskyblock.management.PlayerManager;
import fr.sterll.skycraftskyblock.timer.BDDSave;
import fr.sterll.skycraftskyblock.utils.DBUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class Main extends JavaPlugin {

    private DBUtils dbUtils;

    @Override
    public void onEnable() {
        this.dbUtils = new DBUtils(this);
        register();
        DatabaseManager.initAllDatabaseConnection();
        BDDSave task = new BDDSave(this);
        task.runTaskTimer(this, 0, 0);
        getDbUtils().registerIsland();
    }

    public void register(){
        getCommand("is").setExecutor(new CommandIsland(this));
        getServer().getPluginManager().registerEvents(new PlayerEvents(this), this);
    }

    @Override
    public void onDisable() {
        DatabaseManager.closeAllDatabaseConnections();
    }

    public DBUtils getDbUtils() {
        return dbUtils;
    }
}
