package fr.yanis.armorstandmovement.commands;

import fr.yanis.armorstandmovement.ArmorStandMain;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class CommandMain implements CommandExecutor {

    private ArmorStandMain main;

    public CommandMain(ArmorStandMain main){
        this.main = main;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            sender.sendMessage("§cSeul un joueur peut exécuter cette commande !");
            return false;
        }
        Player player = (Player) sender;

        ArmorStand armorStand = (ArmorStand) player.getLocation().getWorld().spawnEntity(player.getLocation(), EntityType.ARMOR_STAND);
        armorStand.setArms(true);
        armorStand.setCustomName("§6Michel");
        armorStand.setCustomNameVisible(true);
        armorStand.setVisible(true);

        new BukkitRunnable(){
            @Override
            public void run() {
                if(armorStand.isDead()){
                    player.sendMessage("Test Mort");
                    cancel();
                    return;
                }
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        armorStand.setLeftArmPose(new EulerAngle(10, 10, 10));
                        armorStand.setRightArmPose(new EulerAngle(10, 10, 10));
                        player.sendMessage("1");
                    }
                }, 20);
                Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
                    @Override
                    public void run() {
                        armorStand.setLeftArmPose(new EulerAngle(50, 10, 10));
                        armorStand.setRightArmPose(new EulerAngle(50, 10, 10));
                        player.sendMessage("2");
                    }
                }, 40);
            }
        }.runTaskTimer(main, 0, 60);
        return false;
    }
}
