package dev.gotiger.donationConnector.service;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;

public class ChzzkService {
    private final DonationConnector plugin;
    private final ConfigManager configManager;
    private final DonationService donationService;

    public ChzzkService(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.donationService = new DonationService(plugin);
    }
}
