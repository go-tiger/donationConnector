package dev.gotiger.donationConnector.service;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

public class DonationService {

    private final DonationConnector plugin;
    private final ConfigManager configManager;

    public DonationService(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
    }

    public void executeCommands(Player player, Integer payAmount) {
        Map<Integer, List<String>> donationConfig = configManager.getDonationConfig();
        List<String> commands = donationConfig.get(payAmount);

        if (commands == null) {
            commands = donationConfig.get(0);
        }

        for (String command : commands) {
            Bukkit.getScheduler().runTask(plugin, () -> {
                try {
                    String processedCommand = command.replace("{player}", player.getName());
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), processedCommand);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        }

    }

}
