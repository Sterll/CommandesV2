package fr.sterll.skycraftskyblock;

import fr.sterll.skycraftskyblock.management.PlayerManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Events implements Listener {

    private Main main;

    public Events(Main main){
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
    }

    @EventHandler
    public void onClick (InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        InventoryView inv = e.getView();
        ItemStack it = e.getCurrentItem();

        if (it == null) {
            return;
        }

        if (inv.getTitle().equalsIgnoreCase("§6Control Panel")) {
            e.setCancelled(true);
            if (it.hasItemMeta() && it.getItemMeta().hasDisplayName()) {
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§6" + main.getDbUtils().DBGetIslandStringInfoByUUID(player.getUniqueId(), "island_name"))) {
                    player.sendMessage("§6Veuillez entrer le nouveau nom de votre île dans le tchat !");
                    File file = new File(main.getDataFolder(), "tempfile/users_" + player.getUniqueId());
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    config.set("islandNameSet", true);
                    saveFile(file, config);
                    player.closeInventory();
                }
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§aOuvert à la visite")) {
                    main.getDbUtils().DBSetIslandInfo(player, "opentovisite", "0");
                    player.sendMessage("§6Vous venez de §cfermer §6votre île à la visite");
                    player.closeInventory();
                    player.performCommand("is cp");
                }
                if (it.getItemMeta().getDisplayName().equalsIgnoreCase("§cFermé à la visite")) {
                    main.getDbUtils().DBSetIslandInfo(player, "opentovisite", "1");
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
        String message = e.getMessage();
        File file = new File(main.getDataFolder(), "tempfile/users_" + player.getUniqueId());
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        if(file.exists()){
            if(config.getBoolean("islandNameSet")){
                e.setCancelled(true);
                main.getDbUtils().DBSetIslandInfo(player, "island_name", message);
                player.sendMessage("§9Vous venez de changer le nom de votre île, elle s'appelle désormais : §b" + message);
                file.delete();
            }
        }
    }

}
