package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.management.IslandManager;
import fr.sterll.skycraftskyblock.management.PlayerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.UUID;

public class PlayerEvents implements Listener {

    private Main main;

    public PlayerEvents(Main main){
        this.main = main;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();

        // ON VERIFIE SI LE JOUEUR EXISTE DANS LA BDD
        if (!(main.getDbUtils().ifHaveAAccount(player))) {
            main.getDbUtils().createNewPlayer(player);
        }

        // ON VERIFIE SI LE JOUEUR EST BIEN ENREGISTRE DANS LE CACHE
        if(!PlayerManager.existPlayer(player)){
            new PlayerManager(player.getUniqueId(), player.getName(), main.getDbUtils().DBGetStringUserInfos(player.getUniqueId(), "island_name"), main.getDbUtils().DBGetBooleanUserInfos(player.getUniqueId(), "canVote"));
        }

        // ON VERIFIE SI L'ÎLE DU JOUEUR EST BIEN ENREGISTRE DANS LE CACHE
        if(main.getDbUtils().ifHaveAIsland(player)){
            String island_name = PlayerManager.getPlayer(player).getIsland_name();
            if(!IslandManager.existIsland(island_name)){
                new IslandManager(UUID.fromString(main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "owner_uuid")), main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "owner_name"), main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "island_name"), main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "biome"), main.getDbUtils().DBGetIslandBooleanInfoByIslandName(island_name, "opentovisite"), main.getDbUtils().DBGetIslandIntInfoByIslandName(island_name, "vote"), main.getDbUtils().DBGetIslandIntInfoByIslandName(island_name, "level"), main.getDbUtils().DBGetIslandIntInfoByIslandName(island_name, "x_spawn"), main.getDbUtils().DBGetIslandIntInfoByIslandName(island_name, "y_spawn"), main.getDbUtils().DBGetIslandIntInfoByIslandName(island_name, "z_spawn"));
            }
        }
    }

    @EventHandler
    public void onClick (InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        IslandManager islandManager = IslandManager.getIsland(playerManager.getIsland_name());
        InventoryView inv = e.getView();
        ItemStack it = e.getCurrentItem();

        if (it == null) {
            return;
        }

        if (inv.getTitle().equalsIgnoreCase("§6Control Panel")) {
            e.setCancelled(true);
            if (it.hasItemMeta() && it.getItemMeta().hasDisplayName()) {
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§6" + playerManager.getIsland_name())){
                    player.sendMessage("§6Veuillez entrer le nouveau nom de votre île dans le tchat !");
                    File file = new File(main.getDataFolder(), "tempfile/users_" + player.getUniqueId());
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    config.set("islandNameSet", true);
                    saveFile(file, config);
                    player.closeInventory();
                }
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§aOuvert à la visite")) {
                    islandManager.setOpentovisite(false);
                    player.sendMessage("§6Vous venez de §cfermer §6votre île à la visite");
                    player.closeInventory();
                    player.performCommand("is cp");
                }
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§cFermé à la visite")) {
                    islandManager.setOpentovisite(true);
                    player.sendMessage("§6Vous venez d'§aouvrir §6votre île à la visite");
                    player.closeInventory();
                    player.performCommand("is cp");
                }
            }
        }
        if(inv.getTitle().equalsIgnoreCase("§aÎles visitables")){
            e.setCancelled(true);
            if (it.hasItemMeta() && it.getItemMeta().hasDisplayName()) {
                if(it.getItemMeta().getLore().contains("§7- §3Se téléporter à cette île")){
                    player.closeInventory();
                    String island_name = it.getItemMeta().getDisplayName().replace("§6", "");
                    if(main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "owner_name") != null){
                        player.sendMessage("§9Vous venez de vous téléportez sur l'île : §6" + island_name + " §9appartenant à : §b" + main.getDbUtils().DBGetIslandStringInfoByIslandName(island_name, "owner_name"));
                    } else {
                        player.sendMessage("§cCette île n'existe plus !");
                    }
                }
            }
        }
    }

    private void saveFile(File file, FileConfiguration config) {
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        IslandManager islandManager = IslandManager.getIsland(playerManager.getIsland_name());
        String message = e.getMessage();
        File file = new File(main.getDataFolder(), "tempfile/users_" + player.getUniqueId());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(file.exists()){
            if(config.getBoolean("islandNameSet")){
                e.setCancelled(true);
                islandManager.setIsland_name(message);
                playerManager.setIsland_name(message);
                player.sendMessage("§9Vous venez de changer le nom de votre île, elle s'appelle désormais : §b" + message);
                file.delete();
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player player = e.getPlayer();

        main.getDbUtils().saveToBDDAPlayer(PlayerManager.getPlayer(player));
        if(IslandManager.existIsland(PlayerManager.getPlayer(player).getIsland_name())){
            main.getDbUtils().saveToBDDAIsland(IslandManager.getIsland(PlayerManager.getPlayer(player).getIsland_name()));
        }
    }

}
