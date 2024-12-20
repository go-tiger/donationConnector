package dev.gotiger.donationConnector;

import dev.gotiger.donationConnector.command.DonationCommand;
import dev.gotiger.donationConnector.config.ConfigManager;
import dev.gotiger.donationConnector.listener.PlayerEventListener;
import dev.gotiger.donationConnector.service.ChzzkService;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class DonationConnector extends JavaPlugin {
    @Getter
    private ConfigManager configManager;
    private ChzzkService chzzkService;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        chzzkService = new ChzzkService(this);

        // 명령어 등록
        getCommand("dc").setExecutor(new DonationCommand(this));

        // 이벤트 등록
        getServer().getPluginManager().registerEvents(new PlayerEventListener(this), this);

        getLogger().info("DonationConnector 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        chzzkService.disconnectAllChzzkStreamer();
        getLogger().info("DonationConnector 플러그인이 비활성화되었습니다.");
    }

    public ChzzkService getChzzkService() {
        return this.chzzkService;
    }
}
