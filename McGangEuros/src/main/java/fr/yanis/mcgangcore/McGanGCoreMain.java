package fr.yanis.mcgangcore;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class McGanGCoreMain extends JavaPlugin {

    private Utils utils;
    private String prefixEuro, prefixBroadCast;

    @Override
    public void onEnable() {
        this.utils = new Utils();
        getCommand("euro").setExecutor(new CommandEuro(this));
        getCommand("broadcast").setExecutor(new CommandBroadcast(this));
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
            this.prefixBroadCast = config.getString("prefixBroadCast").replace("&", "§");
        } else {
            config.set("prefixEuro", "&9Euro &7->");
            config.set("prefixBroadCast", "&9BroadCast &7-> &b");
            this.utils.saveFile(file, config);
        }
    }

    public String getPrefixBroadCast() {
        return prefixBroadCast;
    }

    public String getPrefixEuro() {
        return prefixEuro;
    }

    public Utils getUtils() {
        return utils;
    }
}
