package fr.yanis.pixelhelpskript;

import org.bukkit.plugin.java.JavaPlugin;

public final class PHSMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("").setExecutor(new CommandMain());

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
