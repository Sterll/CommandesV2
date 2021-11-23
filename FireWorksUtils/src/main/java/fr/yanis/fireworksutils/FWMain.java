package fr.yanis.fireworksutils;

import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class FWMain extends JavaPlugin {

    public static FWMain instance;

    @Override
    public void onEnable() {
        instance = this;
        getCommand("fireworks").setExecutor(new CommandMain());

        File file = new File(this.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(!file.exists()){
            config.set("color", Color.RED);
            config.set("type", "BALL");
            config.set("flicker", false);
            config.set("trail", false);
            saveFile(file, config);
        }

    }

    @Override
    public void onDisable() {

    }

    public static void saveFile(File file, FileConfiguration config){
        try{
            config.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static FWMain getInstance(){
        return instance;
    }
}
