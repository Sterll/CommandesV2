package fr.sterll.skycraftskyblock.management;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class IslandManager {

    private UUID owner_uuid;
    private String owner_name, island_name;
    public static HashMap<String, IslandManager> Islands = new HashMap<>();

    public IslandManager(UUID owner_uuid, String owner_name, String island_name){
        this.owner_uuid = owner_uuid;
        this.owner_name = owner_name;
        this.island_name = island_name;

        Islands.put(island_name, this);
    }

    public static IslandManager getIsland (String island_name) { return Islands.get(island_name); }

    public UUID getOwner_uuid(){ return owner_uuid; }
    public String getOwner_name(){ return owner_name; }
    public String getIsland_name(){ return island_name; }

    public void setOwner_uuid(UUID uuid){
        this.owner_uuid = uuid;
    }
    public void setOwner_name(String ownerName){
        this.owner_name = ownerName;
    }
    public void setIsland_name(String islandName){
        this.island_name = islandName;
    }
}
