package fr.yanis.mcgangcore;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;

public class PlayersEvents implements Listener {

    private McGanGCoreMain main;

    public PlayersEvents(McGanGCoreMain main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        File file = new File(main.getDataFolder(), "users.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection configSection = config.getConfigurationSection(player.getUniqueId().toString());

        if(configSection == null){
            config.set(player.getUniqueId() + ".name", player.getName());
            config.set(player.getUniqueId() + ".euro", 0);
            main.getUtils().saveFile(file, config);
        }
    }

}
