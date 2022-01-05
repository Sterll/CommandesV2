package fr.yanis.mcgangplayers;

import fr.yanis.mcgangplayers.blocks.Printer;
import fr.yanis.mcgangplayers.commands.CommandsMain;
import fr.yanis.mcgangplayers.events.PlayersEvents;
import fr.yanis.mcgangplayers.utils.Utils;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class McGanGPlayersMain extends JavaPlugin {

    private final Logger log = Logger.getLogger("Minecraft");
    private Utils utils;
    private String prefix;
    private File settingsFile;
    private FileConfiguration settingsConfig;
    private Economy econ = null;

    @Override
    public void onEnable() {
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            //getServer().getPluginManager().disablePlugin(this);
            //return;
        }
        this.utils = new Utils(this);
        this.settingsFile = new File(this.getDataFolder(), "settings.yml");
        this.settingsConfig = YamlConfiguration.loadConfiguration(settingsFile);
        initSettingsFile();
        getServer().getPluginManager().registerEvents(new PlayersEvents(this), this);
        getServer().getPluginManager().registerEvents(new Printer(this), this);
        getCommand("menu").setExecutor(new CommandsMain(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return this.econ != null;
    }

    public void initSettingsFile(){
        if(!settingsFile.exists()){
            //General
            settingsConfig.set("prefix", "&6McLeveL §7>>");
            this.prefix = "&6McLeveL §7>>";
            //Inventory
            settingsConfig.set("inventory.menu", "&eMenu principal !");
            settingsConfig.set("inventory.glassColorChange", "&eChanger de thème");
            settingsConfig.set("inventory.boosters", "&3Acheter un booster");
            settingsConfig.set("inventory.buyPrestige", "&3Acheter un un prestige");
            settingsConfig.set("inventory.customItems", "&3Acheter un item custom !");
            //Messages
            settingsConfig.set("messages.toMuchPrinter", "&cVous avez trop de printer");
            settingsConfig.set("messages.breakYourPrinter", "&cVous venez de casser un de vos printer");
            settingsConfig.set("messages.sellNetherStar", "&3Vous venez de vendre une nether star pour 10 000 atoms");
            settingsConfig.set("messages.doesntHaveNetherStar", "&3Vous ne possèdez pas de nether star");
            settingsConfig.set("messages.sellHeartOfSea", "&3Vous venez de vendre un coeur des mers pour 5 000 atoms");
            settingsConfig.set("messages.doesntHaveHeartOfSea", "&3Vous ne possèdez pas de coeur des mers");
            settingsConfig.set("messages.boosterFinish", "&9Un des vos booster est désormais fini !");
            settingsConfig.set("messages.notEnoughMoneyForBuyBooster", "&cVous ne possèdez pas assez d'argent pour acheter ce booster !");
            settingsConfig.set("messages.alreadyHaveBooster", "&cVous avez déjà un booster d'activé !");
            settingsConfig.set("messages.boostersBuy", "§3Vous venez d'acheter un booster !");
            settingsConfig.set("messages.booster2BuyWith1Activated", "§3Achat du booster bien effectué !");
            //Booster
            settingsConfig.set("boosters.first.multiplicator", 2);
            settingsConfig.set("boosters.first.durationInSeconds", 3600);
            settingsConfig.set("boosters.first.prize", 10000);
            settingsConfig.set("boosters.second.multiplicator", 4);
            settingsConfig.set("boosters.second.durationInSeconds", 3600);
            settingsConfig.set("boosters.second.prize", 17500);
            this.utils.saveFile(settingsFile, settingsConfig);
        } else {
            this.prefix = settingsConfig.getString("prefix").replace("&", "§");
        }
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public FileConfiguration getSettingsConfig() {
        return settingsConfig;
    }

    public String getPrefix() {
        return prefix;
    }

    public Economy getEconomy() {
        return econ;
    }

    public Utils getUtils() {
        return utils;
    }
}
