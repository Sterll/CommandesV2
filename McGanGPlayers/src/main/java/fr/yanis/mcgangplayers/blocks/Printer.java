package fr.yanis.mcgangplayers.blocks;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Printer implements Listener {

    private McGanGPlayersMain main;
    private final File usersFile, settingsFile, printerFile;
    private final FileConfiguration usersConfig, settingsConfig, printerConfig;

    public Printer(McGanGPlayersMain main){
        this.main = main;
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.printerFile = new File(main.getDataFolder(), "printer.yml");
        this.printerConfig = YamlConfiguration.loadConfiguration(this.printerFile);
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        Block block = e.getBlockPlaced();
        ItemStack it = e.getItemInHand();
        Player player = e.getPlayer();

        if(it.hasItemMeta() && it.getItemMeta().hasLore() && it.getItemMeta().getLore().contains("§7- §3Permet de posser un printer")){
            if(usersConfig.getIntegerList(player.getUniqueId() + "printerList").size() < 5){
                int printerID = 0;

                if(!printerFile.exists()){
                    printerID = 1;
                    printerConfig.set("maxID", 1);
                    main.getUtils().saveFile(printerFile, printerConfig);
                } else {
                    printerID = printerConfig.getInt("maxID") + 1;
                }

                printerConfig.set(printerID + ".x", block.getLocation().getX());
                printerConfig.set(printerID + ".y", block.getLocation().getY());
                printerConfig.set(printerID + ".z", block.getLocation().getZ());
                printerConfig.set(printerID + ".ownerUUID", player.getUniqueId());
                main.getUtils().saveFile(printerFile, printerConfig);

                usersConfig.set(player.getUniqueId() + ".printerList", usersConfig.getIntegerList(player.getUniqueId() + ".printerList").add(printerID));
                main.getUtils().saveFile(usersFile, usersConfig);

            } else {
                player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.toMuchPrinter").replace("&", "§"));
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Block block = e.getBlock();
        Player player = e.getPlayer();
        ConfigurationSection configSection = printerConfig.getConfigurationSection("");
        if(block.getType() == Material.STONECUTTER){
            for(String id : configSection.getKeys(false)){
                player.sendMessage(id);
            }
        }
    }

}
