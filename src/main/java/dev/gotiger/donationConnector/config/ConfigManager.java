package dev.gotiger.donationConnector.config;

import dev.gotiger.donationConnector.DonationConnector;
import lombok.Getter;

@Getter
public class ConfigManager {
    private final  DonationConnector plugin;
    private final StreamerManager streamerManager;

    public ConfigManager(DonationConnector plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();

        streamerManager = new StreamerManager(plugin);
    }
}
