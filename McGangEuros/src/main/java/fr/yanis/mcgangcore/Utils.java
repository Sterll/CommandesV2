package fr.yanis.mcgangcore;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;

public class Utils {

    public void saveFile(File file, FileConfiguration config){
        try{
            config.save(file);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
