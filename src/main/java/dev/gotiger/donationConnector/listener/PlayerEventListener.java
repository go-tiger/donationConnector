package dev.gotiger.donationConnector.listener;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.service.ChzzkService;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

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
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        String playerName = player.getName();

        ConfigurationSection playerStreamerData = configManager.getStreamerManager().getStreamerData(playerUUID);

        if (playerStreamerData != null) {
            boolean donationLink = playerStreamerData.getBoolean("donationLink", false);
            if (!donationLink) {
                player.sendMessage("후원연동 상태: " + ChatColor.RED + "●");
                return;
            }
            boolean allowDuplicatePlatforms = configManager.isAllowDuplicatePlatforms(plugin);
            String priorityPlatform = playerStreamerData.getString("priority");

            ConfigurationSection platformsSection = playerStreamerData.getConfigurationSection("platforms");

            assert platformsSection != null;
            String broadcastUUIDChzzk = platformsSection.getString("chzzk");
            String broadcastUUIDSoop = platformsSection.getString("soop");

            if (allowDuplicatePlatforms) {
                //todo 모든 플랫폼 연결
                chzzkService.connectChzzkStreamer(playerUUID, broadcastUUIDChzzk);
                player.sendMessage("[" + priorityPlatform + "]" + "후원연동 상태: " + ChatColor.GREEN + "●");
                // Todo 숲 연동 개발 예정
                // soopService.connectSoopStreamer(playerUUID, broadcastUUIDSoop);
                // player.sendMessage("[" + priorityPlatform + "]" + "후원연동 상태: " + ChatColor.GREEN + "●");
            } else {
                //todo 우선순위 플랫폼만 연결
                if (priorityPlatform != null) {
                    switch (priorityPlatform) {
                        case "chzzk":
                            chzzkService.connectChzzkStreamer(playerUUID, broadcastUUIDChzzk);
                            player.sendMessage("[" + priorityPlatform + "]" + "후원연동 상태: " + ChatColor.GREEN + "●");
                            break;
                        case "soop":
                            // Todo 숲 연동 개발 예정
                            // soopService.connectSoopStreamer(playerUUID, broadcastUUIDSoop);
                            // player.sendMessage("[" + priorityPlatform + "]" + "후원연동 상태: " + ChatColor.GREEN + "●");
                            break;
                    }
                }
            }

        } else {
            ConfigurationSection newStreamerData = configManager
                    .getStreamerManager()
                    .getStreamerConfig()
                    .createSection(playerUUID.toString());
            newStreamerData.set("nickname", playerName);
            newStreamerData.set("donationLink", false);

            configManager.getStreamerManager().saveStreamerConfig();

            plugin.getLogger().info("temp player: " + playerName + "("+playerUUID+")");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        chzzkService.disconnectChzzkStreamer(playerUUID);
        plugin.getLogger().info("Disconnected Chzzk API : " + playerUUID);
    }
}
