package fr.sterll.skycraftskyblock.commands;

import fr.sterll.skycraftskyblock.ItemBuilder;
import fr.sterll.skycraftskyblock.Main;
import fr.sterll.skycraftskyblock.management.IslandManager;
import fr.sterll.skycraftskyblock.management.PlayerManager;
import fr.sterll.skycraftskyblock.utils.DBUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.sterll.skycraftskyblock.utils.DBUtils.*;

public class CommandMain implements CommandExecutor {

    private Main main;

    public CommandMain(Main main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("§cSeul un joueur peut utiliser cette commande !");
            return false;
        }

        Player player = (Player) sender;
        PlayerManager playerManager = PlayerManager.getPlayer(player);
        IslandManager islandManager = IslandManager.getIsland(playerManager.getIsland_name());

        switch (args.length){
            case 0:
                island(player, playerManager, islandManager);
                break;
            case 1:
                switch (args[0]){
                    case "help":
                        help(player, playerManager, islandManager);
                        break;
                    case "warp":
                        player.sendMessage("§cVeuillez entrer un nom de joueur");
                        break;
                    case "warps":
                        try {
                            warps(player, playerManager, islandManager);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "topvote":
                        topVote(player, playerManager, islandManager);
                        break;
                    case "controlpanel":
                    case "cp":
                        controlPanel(player, playerManager, islandManager);
                        break;
                }
                break;
        }

        return false;
    }

    public void help(Player player, PlayerManager playerManager, IslandManager islandManager){
        player.sendMessage("§9================================");
        player.sendMessage("§6/is §7--> §bPermet de vous téléportez à votre île ou de vous en créez une");
        player.sendMessage("§6/is controlpanel §7--> §bPermet d'ouvrir le menu de configuration de votre île");
        player.sendMessage("§9================================");
    }

    public void warps(Player player, PlayerManager playerManager, IslandManager islandManager) throws SQLException {
        Inventory inv = Bukkit.createInventory(null, 54, "§aÎles visitables");

        List<String> islands_name = new ArrayList<String>();

        for(OfflinePlayer player2 : Bukkit.getOfflinePlayers()){
            PlayerManager playerManager2 = PlayerManager.getPlayer(player2.getPlayer());
            IslandManager islandManager2 = IslandManager.getIsland(playerManager2.getIsland_name());

            if(player2.getName() == IslandManager.getIsland(playerManager2.getIsland_name()).getOwner_name()){
                islands_name.add(playerManager2.getIsland_name());
            }
        }

        for(String island_name : islands_name){
            IslandManager islandManager3 = IslandManager.getIsland(island_name);
            inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player.getName()).setName("§6" + island_name).setLore("§f", "§7- §9Owner : §b" + islandManager3.getOwner_name(), "§7- §9Votes : §b" + islandManager3.getVote(),"§7- §9Level : §b" + islandManager3.getLevel(), "§f", "§7- §3Se téléporter à cette île").toItemStack());
        }

        inv.setItem(45, new ItemBuilder(Material.HOPPER).setName("§6Trier").toItemStack());
        inv.setItem(46, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("§b").toItemStack());
        inv.setItem(48, new ItemBuilder(Material.HOPPER).setName("§6Filtrer").toItemStack());
        inv.setItem(49, new ItemBuilder(Material.BARRIER).setName("§cQuitter").toItemStack());
        inv.setItem(51, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE).setName("§b").toItemStack());
        inv.setItem(52, new ItemBuilder(Material.SUNFLOWER).setName("§eTéléportation à une île aléatoirement").toItemStack());
        inv.setItem(53, new ItemBuilder(Material.CYAN_STAINED_GLASS_PANE, 1).setName("§b").toItemStack());

        player.openInventory(inv);
    }

    public void topVote(Player player, PlayerManager playerManager, IslandManager islandManager){
        player.sendMessage("");
    }

    public void island(Player player, PlayerManager playerManager, IslandManager islandManager){
        if(!(main.getDbUtils().ifHaveAIsland(player))){
            main.getDbUtils().createNewIsland(player);
            new IslandManager(player.getUniqueId(), player.getName(), "Île de " + player.getName(), "Planes", false, 0, 0, 0, 0, 0);
        } else {
            player.teleport(new Location(player.getWorld(), islandManager.getX_spawn(), islandManager.getY_spawn(), islandManager.getZ_spawn()));
            player.sendMessage("§6Vous venez de vous téléporter à votre île !");
        }
    }

    public void controlPanel(Player player, PlayerManager playerManager, IslandManager islandManager){
        Inventory inv = Bukkit.createInventory(null, 27, "§6Control Panel");

        inv.setItem(12, new ItemBuilder(Material.PAPER, 1).setName("§6" + playerManager.getIsland_name()).setLore("§f", "§7- §bChangez le nom de l'île").toItemStack());
        if(islandManager.getOpenToVisite()){
            inv.setItem(14, new ItemBuilder(Material.GREEN_CONCRETE, 1).setName("§aOuvert à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        } else {
            inv.setItem(14, new ItemBuilder(Material.RED_CONCRETE, 1).setName("§cFermé à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        }

        player.openInventory(inv);
    }
}
