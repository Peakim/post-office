package org.peakimo.postoffice.request;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;
import java.util.Map;

public class PostCall implements Listener {

    private Map<String, Long> chatCooldowns = new HashMap<>();
    private Map<String, String> privateChatPartners = new HashMap<>();
    private static final long CHAT_DURATION = 2 * 60 * 1000; // 2 minutes duration

    public void setPrivateChat(Player player1, Player player2) {
        long endTime = System.currentTimeMillis() + CHAT_DURATION;
        chatCooldowns.put(player1.getName(), endTime);
        chatCooldowns.put(player2.getName(), endTime);
        privateChatPartners.put(player1.getName(), player2.getName());
        privateChatPartners.put(player2.getName(), player1.getName());
    }

    public void cancelPrivateChat(Player player) {
        String partnerName = privateChatPartners.get(player.getName());
        if (partnerName != null) {
            chatCooldowns.remove(player.getName());
            chatCooldowns.remove(partnerName);
            privateChatPartners.remove(player.getName());
            privateChatPartners.remove(partnerName);
        }
    }

    public boolean isInPrivateChat(Player player) {
        return privateChatPartners.containsKey(player.getName());
    }

    private Player getChatPartner(Player player) {
        String partnerName = privateChatPartners.get(player.getName());
        return partnerName != null ? Bukkit.getPlayer(partnerName) : null;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
        String prefix = color("&f[&cPost Call&f] ");

        if (isInPrivateChat(sender)) {
            Player receiver = getChatPartner(sender);
            if (receiver != null && receiver.isOnline()) {
                sender.sendMessage(prefix + "You: " + message);
                receiver.sendMessage(prefix + sender.getName() + ": " + message);
                event.setCancelled(true);
                return;
            }
        }
    }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}