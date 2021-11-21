package fr.yanis.fireworksutils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

import java.io.File;

import static fr.yanis.fireworksutils.FWMain.saveFile;

public class CommandMain implements CommandExecutor {

    public FWMain main = FWMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cSeul un joueur peut executer cette commande !");
        }

        Player player = (Player) sender;
        File file = new File(main.getDataFolder(), "config.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        switch (args.length){
            case 0:
                ItemStack it = new ItemStack(Material.FIREWORK_ROCKET);
                FireworkMeta im = (FireworkMeta) it.getItemMeta();

                im.addEffect(FireworkEffect.builder().withColor(config.getColor("color")).with(FireworkEffect.Type.valueOf(config.getString("type"))).flicker(config.getBoolean("flicker")).trail(config.getBoolean("trail")).build());
                it.setItemMeta(im);

                player.getInventory().addItem(it);
                break;
            case 1:
                player.sendMessage("§cFireWorks --> §6/fireworks set [color/type/flicker/trail] [<string>]");
                break;
            case 2:
                if(args[0].equalsIgnoreCase("set")){
                    if(args[1].equalsIgnoreCase("color")){
                        player.sendMessage("§cFireWorks --> §6/fireworks set color [<color>]");
                    }
                    if(args[1].equalsIgnoreCase("type")){
                        player.sendMessage("§cFireWorks --> §6/fireworks set color [BALL/BALL_LARGE/BURST/CREEPER/STAR]");
                    }
                    if(args[1].equalsIgnoreCase("flicker")){
                        player.sendMessage("§cFireWorks --> §6/fireworks set color [true/false]");
                    }
                    if(args[1].equalsIgnoreCase("trail")){
                        player.sendMessage("§cFireWorks --> §6/fireworks set color [true/false]");
                    }
                }
                break;
            case 3:
                if(args[0].equalsIgnoreCase("set")){
                    if(args[1].equalsIgnoreCase("color")){
                        if(args[2].equalsIgnoreCase("red")){
                            config.set("color", Color.RED);
                        }
                        saveFile(file, config);
                    }
                    if(args[1].equalsIgnoreCase("type")){
                        if(args[2].equalsIgnoreCase("ball")){
                            config.set("type", "BALL");
                        }
                        if(args[2].equalsIgnoreCase("ball_large")){
                            config.set("type", "BALL_LARGE");
                        }
                        if(args[2].equalsIgnoreCase("burst")){
                            config.set("type", "BURST");
                        }
                        if(args[2].equalsIgnoreCase("creeper")){
                            config.set("type", "CREEPER");
                        }
                        if(args[2].equalsIgnoreCase("star")){
                            config.set("type", "STAR");
                        }
                        saveFile(file, config);
                    }
                    if(args[1].equalsIgnoreCase("flicker")){
                        if(args[2].equalsIgnoreCase("true")){
                            config.set("flicker", true);
                        }
                        if(args[2].equalsIgnoreCase("false")){
                            config.set("flicker", false);
                        }
                        saveFile(file, config);
                    }
                    if(args[1].equalsIgnoreCase("trail")){
                        if(args[2].equalsIgnoreCase("true")){
                            config.set("trail", true);
                        }
                        if(args[2].equalsIgnoreCase("false")){
                            config.set("trail", false);
                        }
                    }
                    saveFile(file, config);
                }
                break;
        }

        return false;
    }
}
