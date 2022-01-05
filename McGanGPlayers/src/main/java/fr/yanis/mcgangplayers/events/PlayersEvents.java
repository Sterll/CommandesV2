package fr.yanis.mcgangplayers.events;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import fr.yanis.mcgangplayers.inventory.Menu;
import fr.yanis.mcgangplayers.timer.Booster;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.material.MaterialData;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayersEvents implements Listener {

    private final McGanGPlayersMain main;
    private final File usersFile, settingsFile;
    private final FileConfiguration usersConfig, settingsConfig;

    public PlayersEvents(McGanGPlayersMain main){
        this.main = main;
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        ConfigurationSection configSection = usersConfig.getConfigurationSection(player.getUniqueId().toString());
        if(configSection == null){
            usersConfig.set(player.getUniqueId() + ".name", player.getName());
            usersConfig.set(player.getUniqueId() + ".atoms", 0);
            usersConfig.set(player.getUniqueId() + ".levels", 0);
            usersConfig.set(player.getUniqueId() + ".prestiges", 0);
            usersConfig.set(player.getUniqueId() + ".boosters.first.activate", false);
            usersConfig.set(player.getUniqueId() + ".boosters.second.activate", false);

            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(0);
            usersConfig.set(player.getUniqueId() + ".printerList", list);

            usersConfig.set(player.getUniqueId() + ".options.glasscolor", "ORANGE");
            main.getUtils().saveFile(usersFile, usersConfig);
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        InventoryView invView = e.getView();
        ItemStack it = e.getCurrentItem();
        Menu menu = new Menu(main, player);

        if(it == null) return;

        if(invView.getTitle().contains(main.getPrefix())) e.setCancelled(true);

        if(invView.getTitle().equalsIgnoreCase(main.getPrefix() + " " + settingsConfig.getString("inventory.menu").replace("&", "§"))){
            e.setCancelled(true);
            if(it.hasItemMeta() && it.getItemMeta().hasDisplayName()){
                switch (it.getItemMeta().getDisplayName()){
                    case "§3Vendre Nether Star":
                        menu.sellNetherStar();
                        break;
                    case "§3Vendre Coeur De La Mer":
                        menu.sellHeartOfSea();
                        break;
                    case "§3Acheter un prestige":
                        menu.buyPrestige();
                        break;
                    case "§3Acheter un niveau":
                        menu.buyLevel();
                        break;
                    case "§3Changer de couleur":
                        menu.glassColorChange();
                        break;
                    case "§3Items Custom":
                        menu.customItems();
                        break;
                    case "§3Atouts":
                        menu.atouts();
                        break;
                    case "§3Boosters":
                        menu.boosters();
                        break;
                }
            }
        }
        if(invView.getTitle().equalsIgnoreCase(main.getPrefix() + " " + settingsConfig.getString("inventory.glassColorChange").replace("&", "§"))){
            e.setCancelled(true);
            if(!it.getType().toString().contains("_STAINED_GLASS_PANE")) {
                usersConfig.set(player.getUniqueId() + ".options.glasscolor", it.getType().toString().replace("_STAINED_GLASS", ""));
                main.getUtils().saveFile(usersFile, usersConfig);
                player.closeInventory();
            }
        }
    }

    @EventHandler
    public void onEarnXP(PlayerExpChangeEvent e){

    }

    @EventHandler
    public void onAchivementSuccessful(PlayerAdvancementDoneEvent e){
        Random random = new Random();
        int atoms = random.nextInt(150);
        if(atoms < 50){
            atoms = 50;
        }
        main.getUtils().addAtoms(atoms, e.getPlayer());
    }
}
