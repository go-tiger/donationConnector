package dev.gotiger.donationConnector.listener;

import dev.gotiger.donationConnector.DonationConnector;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerEventListener implements Listener {
    private final DonationConnector plugin;

    public PlayerEventListener(DonationConnector plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void  onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(ChatColor.GREEN + player.getName() + " Join");
    }

    @EventHandler
    public void  onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        plugin.getLogger().info(player.getName() + " Quit");
    }
}
