package fr.yanis.orexianworld;

import fr.yanis.orexianworld.commands.CommandsWorlds;
import org.bukkit.plugin.java.JavaPlugin;

public final class OrexianWorldMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("oxerianworld").setExecutor(new CommandsWorlds(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
