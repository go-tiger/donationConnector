package dev.gotiger.donationConnector.service;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.config.StreamerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DonationService {

    private final DonationConnector plugin;
    private final ConfigManager configManager;
    private final StreamerManager streamerManager;

    public DonationService(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.streamerManager = plugin.getConfigManager().getStreamerManager();
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

    public String enableDonation(UUID playerUUID) {
        ConfigurationSection playerData = streamerManager.getStreamerData(playerUUID);
        boolean donationLink = playerData.getBoolean("donationLink", false);

        if (!donationLink) {
            ConfigurationSection platformSection = playerData.getConfigurationSection("platforms");

            if (platformSection == null || platformSection.getKeys(false).isEmpty()) {
                return ChatColor.RED + "후원 연동 먼저 등록해 주세요.";
            }

            playerData.set("donationLink", true);
            streamerManager.saveStreamerConfig();
            return ChatColor.GREEN + "후원 연동 기능을 켰습니다.";
        }
        return ChatColor.GREEN + "후원 연동 기능이 이미 켜져있습니다.";
    }

    public String disableDonation(UUID playerUUID) {
        ConfigurationSection playerData = streamerManager.getStreamerData(playerUUID);
        boolean donationLink = playerData.getBoolean("donationLink", false);

        if (!donationLink) {
            return ChatColor.GREEN + "후원 연동 기능이 이미 꺼져있습니다.";
        }

        playerData.set("donationLink", false);
        streamerManager.saveStreamerConfig();

        return ChatColor.GREEN + "후원 연동 기능을 껐습니다.";
    }
}
