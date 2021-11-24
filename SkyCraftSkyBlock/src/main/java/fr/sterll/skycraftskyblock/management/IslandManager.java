package fr.sterll.skycraftskyblock.management;

import fr.sterll.skycraftskyblock.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class IslandManager {

    private UUID owner_uuid;
    private String owner_name, island_name, biome;
    private int opentovisite; // 0 = false, 1 = true
    private int vote, level;
    private float x_spawn, y_spawn, z_spawn;
    private Main main;

    public static HashMap<String, IslandManager> Islands = new HashMap<>();

    public IslandManager(Main main, UUID owner_uuid, String owner_name, String island_name, String biome, int opentovisite, int vote, int level, float x_spawn, float y_spawn, float z_spawn){
        this.owner_uuid = owner_uuid;
        this.owner_name = owner_name;
        this.island_name = island_name;
        this.biome = biome;
        this.opentovisite = opentovisite;
        this.vote = vote;
        this.level = level;
        this.x_spawn = x_spawn;
        this.y_spawn = y_spawn;
        this.z_spawn = z_spawn;
        this.main = main;

        Islands.put(island_name, this);
    }

    public static IslandManager getIsland (String island_name) { return Islands.get(island_name); }

    public static boolean existIsland(String island_name){
        return Islands.containsKey(island_name);
    }

    public UUID getOwner_uuid(){ return owner_uuid; }
    public String getOwner_name(){ return owner_name; }
    public String getIsland_name(){ return island_name; }
    public String getBiome(){ return biome; }
    public int getOpenToVisite(){ return opentovisite; }
    public int getVote(){ return vote; }
    public int getLevel(){ return level; }
    public float getX_spawn(){ return x_spawn; }
    public float getY_spawn(){ return y_spawn; }
    public float getZ_spawn(){ return z_spawn; }

    public void setOwner_uuid(UUID uuid){
        this.owner_uuid = uuid;
    }
    public void setOwner_name(String ownerName){
        this.owner_name = ownerName;
    }
    public void setIsland_name(String islandName){
        Islands.remove(this.island_name);
        this.island_name = islandName;
        Islands.put(islandName, this);
    }
    public void setBiome(String ownerName){
        this.biome = biome;
    }
    public void setOpentovisite(int opentovisite){
        this.opentovisite = opentovisite;
        main.getDbUtils().DBSetIslandInfo(getIsland_name(), "opentovisite", String.valueOf(opentovisite));
    }
    public void setVote(int vote){
        this.vote = vote;
    }
    public void setLevel(int level){
        this.level = level;
    }
    public void setX_spawn(float x_spawn){
        this.x_spawn = x_spawn;
    }
    public void setY_spawn(float y_spawn){
        this.y_spawn = y_spawn;
    }
    public void setZ_spawn(float z_spawn){
        this.z_spawn = z_spawn;
    }
}
