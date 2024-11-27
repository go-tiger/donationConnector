package dev.gotiger.donationConnector.network.chzzk.listener;

import dev.gotiger.donationConnector.network.chzzk.event.implement.DonationChatEvent;
import dev.gotiger.donationConnector.network.chzzk.event.implement.MessageChatEvent;
import dev.gotiger.donationConnector.network.chzzk.event.implement.SubscriptionChatEvent;

public interface ChzzkListener {

    default void onMessageChat(final MessageChatEvent e) { }
    default void onDonationChat(final DonationChatEvent e) { }
    default void onSubscriptionChat(final SubscriptionChatEvent e) { }

}