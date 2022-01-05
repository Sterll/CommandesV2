package fr.yanis.mcgangplayers.timer;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Booster extends BukkitRunnable {

    private McGanGPlayersMain main;

    private int timer;
    private Player player;
    private String boosterId;
    private File usersFile;
    private FileConfiguration usersConfig, settingsConfig;

    public Booster(int timer, Player player, String boosterId, McGanGPlayersMain main){
        this.timer = timer;
        this.player = player;
        this.boosterId = boosterId;
        this.main = main;
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.settingsConfig = main.getSettingsConfig();

    }

    @Override
    public void run() {
        if(!usersConfig.getBoolean(player.getUniqueId() + ".boosters." + boosterId + ".activate")) cancel();
        if(timer == 0){
            usersConfig.set("boosters." + boosterId + ".activate", false);
            main.getUtils().saveFile(usersFile, usersConfig);
            player.sendMessage(settingsConfig.getString("messages.boosterFinish").replace("&", "§"));
            cancel();
        }
        timer--;
    }
}
