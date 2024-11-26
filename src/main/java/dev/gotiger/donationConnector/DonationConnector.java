package dev.gotiger.donationConnector;

import org.bukkit.plugin.java.JavaPlugin;

public final class DonationConnector extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("DonationConnector 플러그인이 활성화되었습니다.");
    }

    @Override
    public void onDisable() {
        getLogger().info("DonationConnector 플러그인이 비활성화되었습니다.");
    }
}
