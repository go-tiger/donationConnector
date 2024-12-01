package dev.gotiger.donationConnector.service;

import dev.gotiger.donationConnector.DonationConnector;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.network.chzzk.ChzzkAPI;
import dev.gotiger.donationConnector.network.chzzk.event.implement.*;
import dev.gotiger.donationConnector.network.chzzk.exception.ChzzkException;
import dev.gotiger.donationConnector.network.chzzk.listener.ChzzkListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChzzkService {
    private final DonationConnector plugin;
    private final ConfigManager configManager;
    private final DonationService donationService;
    public static final Map<UUID, ChzzkAPI> playerApis = new HashMap<>();

    public ChzzkService(DonationConnector plugin) {
        this.plugin = plugin;
        this.configManager = plugin.getConfigManager();
        this.donationService = new DonationService(plugin);
    }

    public void connectChzzkStreamer(UUID playerUUID, String broadcastUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
        plugin.getLogger().info(player.getName() + " | " + playerUUID + " | " + broadcastUUID);
        if (broadcastUUID != null) {
            try {
                ChzzkAPI api = new ChzzkAPI.ChzzkBuilder().withData(broadcastUUID).build()
                        .addListeners(new ChzzkListener() {
                            @Override
                            public void onDonationChat(DonationChatEvent e) {
                                Integer payAmount = e.getPayAmount();
                                donationService.executeCommands(player, payAmount);
                                Bukkit.getLogger().info(player.getName() + "가 " + payAmount + "원을 받았습니다.");
                            }
                        });
                api.connect();
                playerApis.put(playerUUID, api);
            } catch (ChzzkException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectChzzkStreamer(UUID playerUUID) {
        ChzzkAPI api = playerApis.get(playerUUID);
        if (api != null) {
            try {
                api.disconnect();
                playerApis.remove(playerUUID);
            } catch (ChzzkException e) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectAllChzzkStreamer() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            ChzzkAPI api = playerApis.get(playerUUID);
            if (api != null) {
                try {
                    api.disconnect();
                    playerApis.remove(playerUUID);
                } catch (ChzzkException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
