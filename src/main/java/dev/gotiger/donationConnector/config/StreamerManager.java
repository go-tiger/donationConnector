package dev.gotiger.donationConnector.config;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.gotiger.donationConnector.DonationConnector;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class StreamerManager {
    private FileConfiguration streamerConfig;
    private final File streamerFile;

    public StreamerManager(DonationConnector plugin) {
        this.streamerFile = new File(plugin.getDataFolder(), "streamer.yml");
        if (!streamerFile.exists()) {
            plugin.saveResource("streamer.yml", false);
        }
        loadStreamerConfig();
    }

    public void loadStreamerConfig() {
        streamerConfig = YamlConfiguration.loadConfiguration(streamerFile);
    }

    public void reloadStreamerConfig() {
        loadStreamerConfig();
    }

    public FileConfiguration getStreamerConfig() {
        return streamerConfig;
    }

    public void saveStreamerConfig() {
        try {
            streamerConfig.save(streamerFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigurationSection isStreamerRegistered(UUID playerUUID) {
        return streamerConfig.getConfigurationSection(playerUUID.toString());
    }
}
