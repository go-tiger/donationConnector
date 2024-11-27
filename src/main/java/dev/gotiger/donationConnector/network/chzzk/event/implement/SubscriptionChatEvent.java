package dev.gotiger.donationConnector.network.chzzk.event.implement;

import dev.gotiger.donationConnector.network.chzzk.event.ChzzkEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubscriptionChatEvent implements ChzzkEvent {

    private final String channelId;

    private final String nickname;

    private final int month;

}