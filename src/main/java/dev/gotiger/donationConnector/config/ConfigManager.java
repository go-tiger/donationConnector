package dev.gotiger.donationConnector.config;

import dev.gotiger.donationConnector.DonationConnector;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;;

@Getter
public class ConfigManager {
    private final  DonationConnector plugin;
    private final StreamerManager streamerManager;

    public ConfigManager(DonationConnector plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();

        streamerManager = new StreamerManager(plugin);
    }

    public Map<Integer, List<String>> getDonationConfig() {
        Map<Integer, List<String>> donationConfig = new HashMap<>();

        ConfigurationSection donationSection = plugin.getConfig().getConfigurationSection("Donation");
        if (donationSection == null) {
            plugin.getLogger().warning("Donation missing config.yml");
            return donationConfig;
        }

        for (String key : donationSection.getKeys(false)) {
            try {
                int amount = Integer.parseInt(key);
                List<String> commands = donationSection.getStringList(key);
                donationConfig.put(amount, commands);
            } catch (NumberFormatException e) {
                plugin.getLogger().warning("Invalid key in Donation section: " + key);
            }
        }
        return donationConfig;
    }
}
