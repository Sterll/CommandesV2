package fr.yanis.mcgangcore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class CommandEuro implements CommandExecutor {

    private McGanGCoreMain main;

    public CommandEuro(McGanGCoreMain main){
        this.main = main;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        switch(args.length){
            case 0:
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    File file = new File(main.getDataFolder(), "users.yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    player.sendMessage("§9Vous possédez §b" + config.getInt(player.getUniqueId() + "euro") + " euros");
                } else {
                    sender.sendMessage("§cSeul un joueur peut exécuter cette commande !");
                }
                break;
            case 1:
                if(Bukkit.getPlayer(args[0]) != null){
                    Player target = Bukkit.getPlayer(args[0]);
                    File file = new File(main.getDataFolder(), "users.yml");
                    FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                    sender.sendMessage(main.getPrefixEuro() + " §9Ce joueur à §b" + config.getInt(target.getUniqueId() + "euro") + " euro");
                } else {
                    sender.sendMessage("§cVeuillez préciser un joueur !");
                }
                break;
            case 2:
                if(args[0].equalsIgnoreCase("reset")) {
                    if (Bukkit.getPlayer(args[1]) != null) {
                        Player target = Bukkit.getPlayer(args[1]);
                        File fileTarget = new File(main.getDataFolder(), "users.yml");
                        FileConfiguration configTarget = YamlConfiguration.loadConfiguration(fileTarget);
                        configTarget.set(target.getUniqueId() + "euro", 0);
                        main.getUtils().saveFile(fileTarget, configTarget);
                        target.sendMessage(main.getPrefixEuro() + " §bVotre solde vient d'être remit à zéro");
                    } else {
                        sender.sendMessage(" §cLe joueur §4" + args[1] + " §cn'existe pas !");
                    }
                } else {
                    sender.sendMessage(main.getPrefixEuro() + " §cVeuillez saisir un bon montant");
                }
                break;
            case 3:
                if(sender instanceof Player){{
                    Player player = (Player) sender;
                    if(args[0].equalsIgnoreCase("pay")){
                        if(Bukkit.getPlayer(args[1]) != null){
                            try{
                                int euro = Integer.parseInt(args[2]);
                                Player target = Bukkit.getPlayer(args[1]);
                                File filePlayer = new File(main.getDataFolder(), "users.yml");
                                File fileTarget = new File(main.getDataFolder(), "users.yml");
                                FileConfiguration configPlayer = YamlConfiguration.loadConfiguration(filePlayer);
                                FileConfiguration configTarget = YamlConfiguration.loadConfiguration(fileTarget);
                                if(configPlayer.getInt(player.getUniqueId() + "euro") < euro){
                                    player.sendMessage(main.getPrefixEuro() + " §cVous n'avez pas assez d'argent pour payer §b" + euro + " € §cà §3" + target.getName());
                                } else {
                                    configPlayer.set(player.getUniqueId() + "euro", configPlayer.getInt(player.getUniqueId() + "euro") - euro);
                                    main.getUtils().saveFile(filePlayer, configPlayer);
                                    configTarget.set(player.getUniqueId() + "euro", configTarget.getInt(player.getUniqueId() + "euro") + euro);
                                    main.getUtils().saveFile(fileTarget, configTarget);
                                    player.sendMessage(main.getPrefixEuro() + " §9Vous venez de payer §b" + euro + " € §9à §3" + target.getName());
                                    target.sendMessage(main.getPrefixEuro() + " §9Vous venez de recevoir §b" + euro + " € §9de la part de §3" + player.getName());
                                }
                            } catch (NumberFormatException e){
                                e.printStackTrace();
                                player.sendMessage(main.getPrefixEuro() + " §cLe montant n'est pas correct !");
                            }
                        } else {
                            player.sendMessage(main.getPrefixEuro() + " §cLe joueur §4" + args[1] + " §cn'existe pas !");
                        }
                    }
                }}
                if(Bukkit.getPlayer(args[1]) != null){
                    try{
                        int euro = Integer.parseInt(args[2]);
                        Player target = Bukkit.getPlayer(args[1]);
                        File file = new File(main.getDataFolder(), "users.yml");
                        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
                        if(args[0].equalsIgnoreCase("set")){
                            config.set(target.getUniqueId() + "euro", euro);
                            main.getUtils().saveFile(file, config);
                            target.sendMessage(main.getPrefixEuro() + " §bVotre solde vient d'être set à §b" + euro + " euro");
                        }
                        if(args[0].equalsIgnoreCase("give")){
                            config.set(target.getUniqueId() + "euro", euro + config.getInt("euro"));
                            main.getUtils().saveFile(file, config);
                            target.sendMessage(main.getPrefixEuro() + " §bVous venez de recevoir §b" + euro + " euro");
                        }
                        if(args[0].equalsIgnoreCase("remove")){
                            config.set(target.getUniqueId() + "euro", config.getInt("euro") - euro);
                            main.getUtils().saveFile(file, config);
                            target.sendMessage(main.getPrefixEuro() + " §bVous venez de perdre §b" + euro + " euro");
                        }
                    } catch (NumberFormatException e){
                        e.printStackTrace();
                        sender.sendMessage("§cLe montant n'est pas correct !");
                    }
                } else {
                    sender.sendMessage("§cLe joueur §4" + args[1] + " §cn'existe pas !");
                }
                break;
        }

        return false;
    }
}
