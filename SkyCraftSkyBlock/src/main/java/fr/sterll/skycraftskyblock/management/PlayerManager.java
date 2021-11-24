package fr.sterll.skycraftskyblock.management;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private UUID uuid;
    private String playername, island_name;
    private int canVote; // 0 = false 1 = true
    public static HashMap<Player, PlayerManager> Players = new HashMap<>();

    public PlayerManager(UUID uuid, String playername, String island_name, int canVote){
        this.uuid = uuid;
        this.playername = playername;
        this.island_name = island_name;
        this.canVote = canVote;

        Players.put(Bukkit.getPlayer(uuid), this);
    }

    public static boolean existPlayer(Player player){
        return Players.containsKey(player);
    }

    public static PlayerManager getPlayer (Player p) { return Players.get(p); }

    public UUID getUuid(){ return uuid; }
    public String getPlayername(){ return playername; }
    public String getIsland_name(){ return island_name; }
    public int getCanVote(){ return canVote; }

    public void setIsland_name(String islandName){
        this.island_name = islandName;
    }
    public void setCanVote(int canVote){
        this.canVote = canVote;
    }
}