package fr.yanis.orexianworld.commands;

import fr.yanis.orexianworld.OrexianWorldMain;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandsWorlds implements CommandExecutor {

    private OrexianWorldMain main;

    public CommandsWorlds(OrexianWorldMain main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(args.length == 1){
            if(args[0].equalsIgnoreCase("create")){
                sender.sendMessage("§cVeuillez préciser un nom");
                return false;
            }
        }
        if(args.length == 2){
            sender.sendMessage("§9Vous venez de créer un monde ce nomant : §b" + args[1]);
            World world = Bukkit.createWorld(WorldCreator.name(args[1]));
        }
        return false;
    }
}
