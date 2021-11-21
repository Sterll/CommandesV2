package fr.sterll.skycraftskyblock.commands;

import fr.sterll.skycraftskyblock.ItemBuilder;
import fr.sterll.skycraftskyblock.Utils;
import fr.sterll.skycraftskyblock.database.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static fr.sterll.skycraftskyblock.Utils.*;
import static fr.sterll.skycraftskyblock.Utils.DBGetIslandStringInfoByIslandName;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){
            sender.sendMessage("§cSeul un joueur peut utiliser cette commande !");
            return false;
        }

        Player player = (Player) sender;

        switch (args.length){
            case 0:
                is(player);
                break;
            case 1:
                switch (args[0]){
                    case "help":
                        help(player);
                        break;
                    case "warp":
                        player.sendMessage("§cVeuillez entrer un nom de joueur");
                        break;
                    case "warps":
                        try {
                            warps(player);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "topvote":
                        topVote(player);
                        break;
                    case "controlpanel":
                    case "cp":
                        controlPanel(player);
                        break;
                }
                break;
        }

        return false;
    }

    public void help(Player player){

    }

    public void warps(Player player) throws SQLException {
        Inventory inv = Bukkit.createInventory(null, 54, "§aÎles visitables");

        List<String> islands_name = new ArrayList<String>();

        for(OfflinePlayer player2 : Bukkit.getOfflinePlayers()){
            if(DBGetIslandStringInfoByUUID(player2.getUniqueId(), "island_name") != null){
                islands_name.add(DBGetIslandStringInfoByUUID(player2.getUniqueId(), "island_name"));
            }
        }

        for(String island_name : islands_name){
            inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(player.getName()).setName("§6" + island_name).setLore("§f", "§7- §9Owner : §b" + DBGetIslandStringInfoByIslandName(island_name, "owner_name"), "§7- §9Votes : §b" + DBGetIslandStringInfoByIslandName(island_name, "vote"),"§7- §9Level : §b" + DBGetIslandStringInfoByIslandName(island_name, "level"), "§f", "§7- §3Se téléporter à cette île").toItemStack());
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

    public void topVote(Player player){
        player.sendMessage("");
    }

    public void is(Player player){
        if(!(ifHaveAIsland(player))){
            Utils.createNewIsland(player);
        } else {
            player.teleport(new Location(player.getWorld(), DBGetIslandIntInfoByUUID(player.getUniqueId(), "x_spawn"), DBGetIslandIntInfoByUUID(player.getUniqueId(), "y_spawn"), DBGetIslandIntInfoByUUID(player.getUniqueId(), "z_spawn")));
            player.sendMessage("§6Vous venez de vous téléporter à votre île !");
        }
    }

    public void controlPanel(Player player){
        Inventory inv = Bukkit.createInventory(null, 27, "§6Control Panel");

        inv.setItem(12, new ItemBuilder(Material.PAPER, 1).setName("§6" + DBGetIslandStringInfoByUUID(player.getUniqueId(), "island_name")).setLore("§f", "§7- §bChangez le nom de l'île").toItemStack());
        if(DBGetIslandBooleanInfoByUUID(player.getUniqueId(), "opentovisite")){
            inv.setItem(14, new ItemBuilder(Material.GREEN_CONCRETE, 1).setName("§aOuvert à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        } else {
            inv.setItem(14, new ItemBuilder(Material.RED_CONCRETE, 1).setName("§cFermé à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        }

        player.openInventory(inv);
    }
}
