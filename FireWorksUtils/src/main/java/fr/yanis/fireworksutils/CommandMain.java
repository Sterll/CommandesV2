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
import java.lang.reflect.Field;

import static fr.yanis.fireworksutils.FWMain.saveFile;

public class CommandMain implements CommandExecutor {

    public FWMain main = FWMain.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player player)
        {
            File file = new File(main.getDataFolder(), "config.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            switch (args.length){
                case 0:
                    ItemStack it = new ItemStack(Material.FIREWORK_ROCKET);
                    FireworkMeta im = (FireworkMeta) it.getItemMeta();

                    if(im == null) return false;

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
                            for (Field declaredField : Color.class.getDeclaredFields())
                            {
                                if(!declaredField.getName().equals(args[2])) continue;

                                try
                                {
                                    config.set("color", declaredField.get(null));
                                    break;
                                } catch (IllegalAccessException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            saveFile(file, config);
                        }
                        if(args[1].equalsIgnoreCase("type")){
                            for (FireworkEffect.Type value : FireworkEffect.Type.values())
                            {
                                if(!value.name().equalsIgnoreCase(args[2])) continue;
                                config.set("type", value.name());
                            }

                            saveFile(file, config);
                        }
                        if(args[1].equalsIgnoreCase("flicker")){
                            if(args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("true"))
                                config.set("flicker", Boolean.parseBoolean(args[2]));
                            saveFile(file, config);
                        }
                        if(args[1].equalsIgnoreCase("trail")){
                            if(args[2].equalsIgnoreCase("false") || args[2].equalsIgnoreCase("true"))
                                config.set("trail", Boolean.parseBoolean(args[2]));
                            saveFile(file, config);
                        }
                    }
                    break;
            }
        }
        else sender.sendMessage("§cSeul un joueur peut executer cette commande !");

        return true;
    }
}
