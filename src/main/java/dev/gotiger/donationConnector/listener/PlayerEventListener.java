package dev.gotiger.donationConnector.listener;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.service.ChzzkService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {
    private final DonationConnector plugin;
    private final ConfigManager configManager;
    private ChzzkService chzzkService;

    public PlayerEventListener(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.chzzkService = new ChzzkService(plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
    }
}
