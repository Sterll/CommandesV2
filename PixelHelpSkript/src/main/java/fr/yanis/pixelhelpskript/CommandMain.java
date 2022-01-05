package fr.yanis.pixelhelpskript;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandMain implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player player = (Player) sender;
        if(args.length == 1){
            if(Bukkit.getPlayer(args[0]) != null){
                Player target = Bukkit.getPlayer(args[0]);
                PCStorage pc = Pixelmon.storageManager.getPCForPlayer(target.getUniqueId());
                Pixelmon.storageManager.initializePCForPlayer(, pc);
            } else {
                player.sendMessage("§cVeuillez mettre un joueur valide");
            }
        }

        return false;
    }
}
