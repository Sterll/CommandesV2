package fr.yanis.mcgangplayers.utils;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class Utils {

    private McGanGPlayersMain main;
    private final File usersFile, settingsFile;
    private final FileConfiguration usersConfig, settingsConfig;

    public Utils(McGanGPlayersMain main){
        this.main = main;
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
    }

    public void saveFile(File file, FileConfiguration config){
        try{
            config.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void addAtoms(int atomsAdded, Player player){
        int atoms = atomsAdded;
        if(usersConfig.getBoolean(player.getUniqueId() + ".boosters.first.activate")){
            atoms = atoms * settingsConfig.getInt("boosters.first.multiplicator");
        }
        if(usersConfig.getBoolean(player.getUniqueId() + ".boosters.second.activate")){
            atoms = atoms * settingsConfig.getInt("boosters.second.multiplicator");
        }
        usersConfig.set(player.getUniqueId() + ".atoms", usersConfig.getInt(player.getUniqueId() + ".atoms") + atoms);
        main.getUtils().saveFile(usersFile, usersConfig);
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§e+ " + atoms + " atoms"));
    }

}
