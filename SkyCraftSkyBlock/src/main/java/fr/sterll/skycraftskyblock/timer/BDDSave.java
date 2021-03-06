package fr.sterll.skycraftskyblock.timer;

import fr.sterll.skycraftskyblock.Main;
import org.bukkit.scheduler.BukkitRunnable;

public class BDDSave extends BukkitRunnable {

    private int timer = 72000;
    private Main main;

    public BDDSave(Main main){
        this.main = main;
    }

    @Override
    public void run() {

        if(timer == 0){
            main.getDbUtils().saveToBDD();
            timer = 72000;
        }

        timer--;
    }
}
