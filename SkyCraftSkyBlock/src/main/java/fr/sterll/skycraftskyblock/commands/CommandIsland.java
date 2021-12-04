package fr.sterll.skycraftskyblock.commands;

import fr.sterll.skycraftskyblock.utils.ItemBuilder;
import fr.sterll.skycraftskyblock.Main;
import fr.sterll.skycraftskyblock.management.IslandManager;
import fr.sterll.skycraftskyblock.management.PlayerManager;
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
import java.util.Objects;

public class CommandIsland implements CommandExecutor {

    private Main main;

    public CommandIsland(Main main){
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

        switch (args.length){
            case 0:
                island(player, playerManager);
                break;
            case 1:
                switch (args[0]){
                    case "help":
                        help(player, playerManager);
                        break;
                    case "warp":
                        player.sendMessage("§cVeuillez entrer un nom de joueur");
                        break;
                    case "warps":
                        try {
                            warps(player, playerManager);
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case "topvote":
                        topVote(player, playerManager);
                        break;
                    case "controlpanel":
                    case "cp":
                        controlPanel(player, playerManager);
                        break;
                }
                break;
        }

        return false;
    }

    public void help(Player player, PlayerManager playerManager){
        player.sendMessage("§9================================");
        player.sendMessage("§6/is §7--> §bPermet de vous téléportez à votre île ou de vous en créez une");
        player.sendMessage("§6/is controlpanel §7--> §bPermet d'ouvrir le menu de configuration de votre île");
        player.sendMessage("§6/is warps §7--> §bPermet d'ouvrir le menu de pour se téléporter aux îles ouvertes");
        player.sendMessage("§9================================");
    }

    public void warps(Player player, PlayerManager playerManager) throws SQLException {
        Inventory inv = Bukkit.createInventory(null, 54, "§aÎles visitables");
        List<String> islands_name  = main.getDbUtils().getAllIslandOpen();

        for(String island_name : islands_name){
            IslandManager islandManager3 = IslandManager.getIsland(island_name);
            inv.addItem(new ItemBuilder(Material.PLAYER_HEAD).setSkullOwner(islandManager3.getOwner_name()).setName("§6" + island_name).setLore("§f", "§7- §9Propriétaire : §b" + islandManager3.getOwner_name(), "§7- §9Votes : §b" + islandManager3.getVote(),"§7- §9Niveau : §b" + islandManager3.getLevel(), "§f", "§7- §3Se téléporter à cette île").toItemStack());
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

    public void topVote(Player player, PlayerManager playerManager){
        player.sendMessage("");
    }

    public void island(Player player, PlayerManager playerManager){
        if(!(main.getDbUtils().ifHaveAIsland(player))){
            main.getDbUtils().createNewIsland(player);
            main.getDbUtils().DBSetUserInfo(player, "island_name", "Île de " + player.getName());
            playerManager.setIsland_name("Île de " + player.getName());
            new IslandManager(main, player.getUniqueId(), player.getName(), "Île de " + player.getName(), "Planes", 0, 0, 0, 0, 0, 0);
        } else {
            IslandManager islandManager = IslandManager.getIsland(playerManager.getIsland_name());
            player.teleport(new Location(player.getWorld(), islandManager.getX_spawn(), islandManager.getY_spawn(), islandManager.getZ_spawn()));
            player.sendMessage("§6Vous venez de vous téléporter à votre île !");
        }
    }

    public void controlPanel(Player player, PlayerManager playerManager){
        Inventory inv = Bukkit.createInventory(null, 27, "§6Control Panel");
        if(IslandManager.getIsland(playerManager.getIsland_name()) == null){
            player.sendMessage("§cVous ne possédez pas d'île");
            return;
        }
        IslandManager islandManager = IslandManager.getIsland(playerManager.getIsland_name());

        inv.setItem(12, new ItemBuilder(Material.PAPER, 1).setName("§6" + playerManager.getIsland_name()).setLore("§f", "§7- §bChangez le nom de l'île").toItemStack());
        if(islandManager.getOpenToVisite() == 1){
            inv.setItem(14, new ItemBuilder(Material.GREEN_CONCRETE, 1).setName("§aOuvert à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        } else {
            inv.setItem(14, new ItemBuilder(Material.RED_CONCRETE, 1).setName("§cFermé à la visite").setLore("§f", "§7- §bPermet de définir le droit de visite").toItemStack());
        }
        player.openInventory(inv);
    }
}