package fr.yanis.mcgangplayers.events;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ItemsEvents implements Listener {

    @EventHandler
    public void onInteract(PlayerHarvestBlockEvent e){
        Player player = e.getPlayer();
        ItemStack it = e.getPlayer().getItemInHand();
        Block block = e.getHarvestedBlock();

        if(it == null) return;

        if(it.hasItemMeta() && it.getItemMeta().hasDisplayName()){
            String itName = it.getItemMeta().getDisplayName();
            List<String> itLore = it.getItemMeta().getLore();
            if(itName.equalsIgnoreCase("§6Hâche de la ferme")){
                if(itLore.contains("§bCet item vous permet de §cOneShot §bles citrouilles et les pastèques")){
                    if(block.getType() == Material.PUMPKIN || block.getType() == Material.MELON){
                        block.breakNaturally();
                    }
                }
            }
        }
    }
}
