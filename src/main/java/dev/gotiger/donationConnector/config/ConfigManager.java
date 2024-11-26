package dev.gotiger.donationConnector.config;

import dev.gotiger.donationConnector.DonationConnector;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private final DonationConnector plugin;
    private File streamerFile;
    private FileConfiguration streamerConfig;

    public ConfigManager(DonationConnector plugin) {
        this.plugin = plugin;
        loadConfigs();
    }

    private void loadConfigs() {
        plugin.saveDefaultConfig();

        streamerFile  = new File(plugin.getDataFolder(),"streamer.yml");
        if (!streamerFile.exists()) {
            plugin.saveResource("streamer.yml", false);
        }
        streamerConfig = YamlConfiguration.loadConfiguration(streamerFile);
    }
}
