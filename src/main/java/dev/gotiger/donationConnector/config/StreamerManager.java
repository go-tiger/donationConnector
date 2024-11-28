package dev.gotiger.donationConnector.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import dev.gotiger.donationConnector.DonationConnector;
import java.io.File;

public class StreamerManager {
    private final FileConfiguration streamerConfig;
    private final File streamerFile;

    public StreamerManager(DonationConnector plugin) {
        this.streamerFile = new File(plugin.getDataFolder(), "streamer.yml");
        if (!streamerFile.exists()) {
            plugin.saveResource("streamer.yml", false);
        }
        this.streamerConfig = YamlConfiguration.loadConfiguration(streamerFile);
    }

    public FileConfiguration getStreamerConfig() {
        return streamerConfig;
    }
}
