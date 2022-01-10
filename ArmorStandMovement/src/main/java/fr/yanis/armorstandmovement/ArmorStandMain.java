package fr.yanis.armorstandmovement;

import fr.yanis.armorstandmovement.commands.CommandMain;
import org.bukkit.plugin.java.JavaPlugin;

public final class ArmorStandMain extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("armorstand").setExecutor(new CommandMain(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
