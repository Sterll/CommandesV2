package fr.yanis.mcgangcore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandBroadcast implements CommandExecutor {

    private McGanGCoreMain main;

    public CommandBroadcast(McGanGCoreMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String message : args){
            sb.append(message);
        }
        Bukkit.broadcastMessage(main.getPrefixBroadCast() + sb);
        return true;
    }
}
