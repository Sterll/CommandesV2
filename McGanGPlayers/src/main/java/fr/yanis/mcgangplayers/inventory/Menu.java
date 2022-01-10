package fr.yanis.mcgangplayers.inventory;

import fr.yanis.mcgangplayers.McGanGPlayersMain;
import fr.yanis.mcgangplayers.timer.Booster;
import fr.yanis.mcgangplayers.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class Menu {

    private McGanGPlayersMain main;
    private File settingsFile, usersFile;
    private FileConfiguration settingsConfig, usersConfig;
    private Player player;

    public Menu(McGanGPlayersMain main, Player player){
        this.main = main;
        this.settingsFile = main.getSettingsFile();
        this.settingsConfig = main.getSettingsConfig();
        this.usersFile = new File(main.getDataFolder(), "users.yml");
        this.usersConfig = YamlConfiguration.loadConfiguration(this.usersFile);
        this.player = player;
    }

    public void sellNetherStar(){
         if(player.getInventory().contains(Material.NETHER_STAR)){
             int amountOfItem = 0;
             for(ItemStack item : player.getInventory().getContents()){
                 if(item != null){
                     if(item.getType() == Material.NETHER_STAR){
                         amountOfItem = amountOfItem + item.getAmount();
                     }
                 }
             }
             player.getInventory().remove(Material.NETHER_STAR);
             player.getInventory().addItem(new ItemBuilder(Material.NETHER_STAR, amountOfItem - 1).toItemStack());
             usersConfig.set(player.getUniqueId() + ".atoms", usersConfig.getInt(player.getUniqueId() + ".atoms") + 10000);
             main.getUtils().saveFile(usersFile, usersConfig);
             player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.sellNetherStar").replace("&", "§"));
         } else {
             player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.doesntHaveNetherStar").replace("&", "§"));
         }
    }

    public void sellHeartOfSea(){
        if(player.getInventory().contains(Material.HEART_OF_THE_SEA)){
            int amountOfItem = 0;
            for(ItemStack item : player.getInventory().getContents()){
                if(item != null){
                    if(item.getType() == Material.HEART_OF_THE_SEA){
                        amountOfItem = amountOfItem + item.getAmount();
                    }
                }
            }
            player.getInventory().remove(Material.HEART_OF_THE_SEA);
            player.getInventory().addItem(new ItemBuilder(Material.HEART_OF_THE_SEA, amountOfItem - 1).toItemStack());
            usersConfig.set(player.getUniqueId() + ".atoms", usersConfig.getInt(player.getUniqueId() + ".atoms") + 5000);
            main.getUtils().saveFile(usersFile, usersConfig);
            player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.sellHeartOfSea").replace("&", "§"));
        } else {
            player.sendMessage(main.getPrefix() + " " + settingsConfig.getString("messages.doesntHaveHeartOfSea").replace("&", "§"));
        }
    }

    public void buyPrestige(){
        Inventory inv = Bukkit.createInventory(null, 9, main.getPrefix() + " " + settingsConfig.getString("inventory.buyPrestige").replace("&", "§"));

        int prestigePlayer = usersConfig.getInt(player.getUniqueId() + ".prestiges");
        inv.setItem(4, new ItemBuilder(Material.PAPER).setName("§9Prestige " + (prestigePlayer + 1)).setLore("§7", "§7- §bPasser au prestige suivant", "§7- §bCoûte 15 000 atoms").toItemStack());

        inv.setItem(0, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(8, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());

        player.openInventory(inv);
    }

    public void buyLevel(){
        Inventory inv = Bukkit.createInventory(null, 9, main.getPrefix() + " " + settingsConfig.getString("inventory.buyLevel").replace("&", "§"));

        int levelPlayer = usersConfig.getInt(player.getUniqueId() + ".levels");
        inv.setItem(4, new ItemBuilder(Material.PAPER).setName("§9Niveau " + (levelPlayer + 1)).setLore("§7", "§7- §bPasser au niveau suivant", "§7- §bCoûte " + (levelPlayer + 1) * 250 + " atoms").toItemStack());

        inv.setItem(0, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(8, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());

        player.openInventory(inv);
    }

    public void glassColorChange(){
        Inventory inv = Bukkit.createInventory(null, 45, main.getPrefix() + " " + settingsConfig.getString("inventory.glassColorChange").replace("&", "§"));

        inv.setItem(10, new ItemBuilder(Material.ORANGE_STAINED_GLASS).setName("§6Orange").setLore(" ", "§7» §9Permet de changer la couleur des vitres en orange").toItemStack());
        inv.setItem(11, new ItemBuilder(Material.MAGENTA_STAINED_GLASS).setName("§5Magenta").setLore(" ", "§7» §9Permet de changer la couleur des vitres en magenta").toItemStack());
        inv.setItem(12, new ItemBuilder(Material.LIGHT_BLUE_STAINED_GLASS).setName("§bBleu clair").setLore(" ", "§7» §9Permet de changer la couleur des vitres en bleu clair").toItemStack());
        inv.setItem(13, new ItemBuilder(Material.YELLOW_STAINED_GLASS).setName("§eJaune").setLore(" ", "§7» §9Permet de changer la couleur des vitres en jaune").toItemStack());
        inv.setItem(14, new ItemBuilder(Material.LIME_STAINED_GLASS).setName("§aLime").setLore(" ", "§7» §9Permet de changer la couleur des vitres en lime").toItemStack());
        inv.setItem(15, new ItemBuilder(Material.PINK_STAINED_GLASS).setName("§dRose").setLore(" ", "§7» §9Permet de changer la couleur des vitres en rose").toItemStack());
        inv.setItem(16, new ItemBuilder(Material.GRAY_STAINED_GLASS).setName("§8Gris").setLore(" ", "§7» §9Permet de changer la couleur des vitres en gris").toItemStack());
        inv.setItem(19, new ItemBuilder(Material.BROWN_STAINED_GLASS).setName("§6Marron").setLore(" ", "§7» §9Permet de changer la couleur des vitres en orange").toItemStack());
        inv.setItem(25, new ItemBuilder(Material.RED_STAINED_GLASS).setName("§cRouge").setLore(" ", "§7» §9Permet de changer la couleur des vitres en rouge").toItemStack());
        inv.setItem(28, new ItemBuilder(Material.GREEN_STAINED_GLASS).setName("§2Vert").setLore(" ", "§7» §9Permet de changer la couleur des vitres en vert").toItemStack());
        inv.setItem(29, new ItemBuilder(Material.CYAN_STAINED_GLASS).setName("§3Cyan").setLore(" ", "§7» §9Permet de changer la couleur des vitres en cyan").toItemStack());
        inv.setItem(30, new ItemBuilder(Material.LIGHT_GRAY_STAINED_GLASS).setName("§7Gris clair").setLore(" ", "§7» §9Permet de changer la couleur des vitres en gris clair").toItemStack());
        inv.setItem(31, new ItemBuilder(Material.BLUE_STAINED_GLASS).setName("§1Bleu").setLore(" ", "§7» §9Permet de changer la couleur des vitres en bleu").toItemStack());
        inv.setItem(32, new ItemBuilder(Material.BLACK_STAINED_GLASS).setName("§0Noir").setLore(" ", "§7» §9Permet de changer la couleur des vitres en noir").toItemStack());
        inv.setItem(33, new ItemBuilder(Material.PURPLE_STAINED_GLASS).setName("§5Viollet").setLore(" ", "§7» §9Permet de changer la couleur des vitres en viollet").toItemStack());
        inv.setItem(34, new ItemBuilder(Material.WHITE_STAINED_GLASS).setName("§fBlanc").setLore(" ", "§7» §9Permet de changer la couleur des vitres en blanc").toItemStack());

        for (int i = 0; i < 10; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }

        for (int i = 35; i < 45; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        inv.setItem(17, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(18, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(26, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(27, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());

        player.openInventory(inv);
    }

    public void customItems(){
        Inventory inv = Bukkit.createInventory(null, 9, main.getPrefix() + " " + settingsConfig.getString("inventory.customItems").replace("&", "§"));

        inv.setItem(1, new ItemBuilder(Material.WOODEN_AXE).setName("§6Hache du fermier").setLore(" ", "§7- §bCasser les mélons et les citrouilles instantanément !", " ", "§7- §9Achetable pour 11 000 atoms", "§7- §cNécesite d'être Presitge 1 !").toItemStack());

        inv.setItem(0, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        inv.setItem(8, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());

        player.openInventory(inv);
    }

    public void atouts(){

    }

    public void boosters(){
        Inventory inv = Bukkit.createInventory(null, 27, main.getPrefix() + " " + settingsConfig.getString("inventory.boosters").replace("&", "§"));

        inv.setItem(4, new ItemBuilder(Material.ENDER_EYE).setName("§3Statistiques").setLore(" ", "§7» §9Niveau §b" + usersConfig.getInt(player.getUniqueId() + ".levels"), "§7» §9Prestige §b" + usersConfig.getInt(player.getUniqueId() + ".prestiges"), "§7» §9Atoms §b" + usersConfig.getInt(player.getUniqueId() + ".atoms"), "§7» §9Argent §b" + main.getEconomy().getBalance(player)).toItemStack());

        inv.setItem(11, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§3Booster 1").setLore(" ", "§7» §9Multiplicateur §b" + settingsConfig.getInt("boosters.first.multiplicator"), "§7» §9Prix §b" + settingsConfig.getInt("boosters.first.prize") + " dollars").toItemStack());
        inv.setItem(15, new ItemBuilder(Material.EXPERIENCE_BOTTLE).setName("§3Booster 2").setLore(" ", "§7» §9Multiplicateur §b" + settingsConfig.getInt("boosters.second.multiplicator"), "§7» §9Prix §b" + settingsConfig.getInt("boosters.second.prize") + " dollars").toItemStack());

        for (int i = 0; i < 4; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for (int i = 5; i < 10; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for (int i = 17; i < 22; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }
        for (int i = 23; i < 27; i++) {
            inv.setItem(i, new ItemBuilder(Material.getMaterial(usersConfig.getString(player.getUniqueId() + ".options.glasscolor") + "_STAINED_GLASS_PANE")).setName("§f").toItemStack());
        }

        player.openInventory(inv);
    }
}
