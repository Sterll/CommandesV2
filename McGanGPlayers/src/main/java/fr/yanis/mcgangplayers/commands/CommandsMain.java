package fr.yanis.mcgangplayers.commands;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import fr.yanis.mcgangplayers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;

public class CommandsMain implements CommandExecutor {

    private McGanGPlayersMain main;
    private File settingsFile;
    private FileConfiguration settingsConfig;

    public CommandsMain(McGanGPlayersMain main){
        this.main = main;
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage();
        }
        Player player = (Player) sender;
        File file = new File(main.getDataFolder(), "users.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        Inventory inv = Bukkit.createInventory(null, 54, main.getPrefix() + " " + settingsConfig.getString("inventory.menu").replace("&", "§"));
        //main.getEcon().getBalance(player)
        inv.setItem(4, new ItemBuilder(Material.ENDER_EYE).setName("§3Statistiques").setLore(" ", "§7» §9Niveau §b" + config.getInt(player.getUniqueId() + ".levels"), "§7» §9Prestige §b" + config.getInt(player.getUniqueId() + ".prestiges"), "§7» §9Atoms §b" + config.getInt(player.getUniqueId() + ".atoms"), "§7» §9Argent §b" + main.getEconomy().getBalance(player)).toItemStack());
        inv.setItem(11, new ItemBuilder(Material.NETHER_STAR).setName("§3Vendre Nether Star").setLore(" ", "§7» §9Vendre une nether star pour 10 000 atoms").toItemStack());
        inv.setItem(15, new ItemBuilder(Material.HEART_OF_THE_SEA).setName("§3Vendre Coeur De La Mer").setLore(" ", "§7» §9Vendre un coeur de la mer pour 5 000 atoms").toItemStack());
        inv.setItem(21, new ItemBuilder(Material.END_CRYSTAL).setName("§3Acheter un prestige").setLore(" ", "§7» §9Utiliser ses atoms pour passer en prestige", " ", "§7» §cNécessite d'être niveau 55 !").toItemStack());
        inv.setItem(23, new ItemBuilder(Material.AMETHYST_SHARD).setName("§3Acheter un niveau").setLore(" ", "§7» §9Utiliser ses atoms pour passer en niveau").toItemStack());
        inv.setItem(31, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS")).setName("§3Changer de couleur").setLore(" ", "§7» §9Modifier la couleur des vitres dans les menus !").toItemStack());
        inv.setItem(38, new ItemBuilder(Material.GLOW_INK_SAC).setName("§3Items Custom").setLore(" ", "§7» §9Acheter des items customs").toItemStack());
        inv.setItem(40, new ItemBuilder(Material.FIRE_CHARGE).setName("§3Atouts").setLore(" ", "§7» §9Utiliser ses atouts").toItemStack());
        inv.setItem(42, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§3Boosters").setLore(" ", "§7» §9Obtenir des boosters").toItemStack());

        for(int i = 0;i < 4; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for(int i = 5;i < 11; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for(int i = 16;i < 19 ; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for(int i = 26;i < 31 ; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for(int i = 32;i < 38 ; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for(int i = 43;i < 54 ; i++){
            inv.setItem(i, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }

        inv.setItem(12, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(14, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(39, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(41, new ItemBuilder(Material.getMaterial(config.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());


        player.openInventory(inv);
        return false;
    }
}
