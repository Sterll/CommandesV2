package fr.yanis.mcgangcore;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.awt.*;

public class CommandBroadcast implements CommandExecutor {

    private McGanGCoreMain main;

    public CommandBroadcast(McGanGCoreMain main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        StringBuilder sb = new StringBuilder();
        for(String message : args){
            sb.append(message + " ");
        }
        for(Player players : Bukkit.getOnlinePlayers()){
            players.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(sb.toString().replace("&", "§")));
        }
        return true;
    }
}
