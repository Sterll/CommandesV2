package fr.yanis.mcgangplayers.events;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import fr.yanis.mcgangplayers.timer.Booster;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class InventoryEvents {

    private final McGanGPlayersMain main;
    private final File usersFile, settingsFile;
    private final FileConfiguration usersConfig, settingsConfig;

    public InventoryEvents(McGanGPlayersMain main){
        this.main = main;
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        InventoryView invView = e.getView();
        ItemStack it = e.getCurrentItem();

        if(it == null) return;

        if(invView.getTitle().equalsIgnoreCase(main.getPrefix() + " " + settingsConfig.getString("inventory.buyPrestige").replace("&", "§"))){
            if(it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§3Booster 1")) buyBoosters(player, "first");
            if(it.hasItemMeta() && it.getItemMeta().hasDisplayName() && it.getItemMeta().getDisplayName().equalsIgnoreCase("§3Booster 2")) buyBoosters(player, "second");
        }
    }

    public void buyBoosters(Player player, String boosterId){

        if(boosterId.equalsIgnoreCase("first")){
            if(usersConfig.getBoolean(player.getUniqueId() + ".boosters.first.activate")){
                player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.alreadyHaveBooster").replace("&", "§"));
                return;
            }
        }

        if(boosterId.equalsIgnoreCase("second")){
            if(usersConfig.getBoolean(player.getUniqueId() + ".boosters.second.activate")){
                player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.alreadyHaveBooster").replace("&", "§"));
                return;
            }
        }

        if(main.getEconomy().getBalance(player) >= settingsConfig.getInt("boosters." + boosterId + ".prize")){
            if(boosterId.equalsIgnoreCase("second")){
                if(usersConfig.getBoolean(player.getUniqueId() + ".boosters.first.activate")){
                    usersConfig.set(player.getUniqueId() + ".boosters.first.activate", false);
                    main.getUtils().saveFile(usersFile, usersConfig);
                    player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.booster2BuyWith1Activated").replace("&", "§"));
                }
            }

            main.getEconomy().bankWithdraw(player.getName(), settingsConfig.getInt("boosters." + boosterId + ".prize"));
            usersConfig.set(player.getUniqueId() + ".boosters." + boosterId + ".activate", true);
            main.getUtils().saveFile(usersFile, usersConfig);
            Booster task = new Booster(settingsConfig.getInt("boosters." + boosterId + ".durationInSeconds"), player, boosterId, main);
            task.runTaskTimer(main, 0, 20);
            player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.boostersBuy").replace("&", "§"));
        } else {
            player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.notEnoughMoneyForBuyBooster").replace("&", "§"));
        }
    }

}
