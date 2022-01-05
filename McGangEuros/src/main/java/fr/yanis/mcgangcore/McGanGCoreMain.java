package fr.yanis.mcgangcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class McGanGCoreMain extends JavaPlugin {

    private Utils utils;
    private String prefixEuro;

    @Override
    public void onEnable() {
        this.utils = new Utils();
        getCommand("euro").setExecutor(new CommandEuro(this));
        getCommand("hbroaction").setExecutor(new CommandBroadcast(this));
        getServer().getPluginManager().registerEvents(new PlayersEvents(this), this);
        verifAllPrefix();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void verifAllPrefix(){
        File file = new File(this.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        if(file.exists()){
            this.prefixEuro = config.getString("prefixEuro").replace("&", "§");
        } else {
            config.set("prefixEuro", "&9Euro &7->");
            this.utils.saveFile(file, config);
            this.prefixEuro = config.getString("prefixEuro").replace("&", "§");
        }
    }

    public String getPrefixEuro() {
        return prefixEuro;
    }

    public Utils getUtils() {
        return utils;
    }
}
