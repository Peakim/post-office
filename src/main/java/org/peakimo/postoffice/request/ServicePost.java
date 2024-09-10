package org.peakimo.postoffice.request;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.peakimo.postoffice.Post_office;

import java.util.HashMap;
import java.util.Map;

public class ServicePost implements CommandExecutor {

    private Post_office instance = Post_office.getInstance();
    private Map<String, Long> playerRequestTimes = new HashMap<>();
    private PostCall postCall;

    public ServicePost(PostCall postCall) {
        this.postCall = postCall;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String post_requestsent = color(instance.getConfig().getString("post-request"));
        String post_receive = color(instance.getConfig().getString("post-receive"));

        Player bazikon = (Player) sender;
        String name = bazikon.getName();

        String postrequestsent_with_placeholder = post_requestsent.replace("%PLAYER%", bazikon.getPlayer().getName());
        String postreceive_with_placeholder = post_receive.replace("%PLAYER%", bazikon.getPlayer().getName());


        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {

            bazikon.sendMessage(ChatColor.YELLOW + "--------> Post Section <--------");
            bazikon.sendMessage(ChatColor.WHITE + "/post-request help - for get all command help");
            bazikon.sendMessage(ChatColor.WHITE + "/post-request accept (Player id) - Accept request player");
            bazikon.sendMessage(ChatColor.WHITE + "/post-request cancel - Cancel post call");
            bazikon.sendMessage(ChatColor.YELLOW + "---------------------------------");

            return true;
        }

        if (args.length > 0 && (!args[0].equalsIgnoreCase("accept") && !args[0].equalsIgnoreCase("cancel") && !args[0].equalsIgnoreCase("help"))) {
            sender.sendMessage(color("&e/post-request help"));
            return true;
        }


        if (sender instanceof Player) {
            String playerName = bazikon.getName();


            if (args.length > 0 && args[0].equalsIgnoreCase("accept")) {
                if (bazikon.hasPermission("Postoffice.get")) {
                    if (args.length > 1) {
                        String targetPlayerName = args[1];
                        Player targetPlayer = Bukkit.getPlayer(targetPlayerName);

                        if (targetPlayer != null) {
                            if (playerRequestTimes.containsKey(targetPlayerName)) {
                                playerRequestTimes.put(targetPlayerName, 0L);
                                bazikon.sendMessage(color("&f&l[&c&lPost Office&f&l]&r &aYour request by " + playerName + " accepted."));
                                bazikon.performCommand("playsound minecraft:accept_request player " + playerName +  " ~ ~ ~ 1");
                                targetPlayer.sendMessage(color( "&f&l[&c&lPost Office&f&l]&r &aYou accepted request."));
                                postCall.setPrivateChat(bazikon, targetPlayer);
                            } else {
                                bazikon.sendMessage(color("&f&l[&c&lPost Office&f&l]&r &c" + targetPlayerName + " Has not sent a Post request!"));
                            }
                        } else {
                            bazikon.sendMessage(color( "&f&l[&c&lPost Office&f&l]&r &cNo player found with this name!"));
                        }
                    } else {
                        bazikon.sendMessage(color("&f&l[&c&lPost Office&f&l]&r &cPlease enter the player's name."));
                    }
                }
                return true;
            }

            if (args.length > 0 && args[0].equalsIgnoreCase("cancel")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    postCall.cancelPrivateChat(player);
                    player.sendMessage(color("&f&l[&c&lPost Office&f&l]&r &fPost-Service canceled!"));
                }
                return true;
            }
            long lastRequestTime = playerRequestTimes.getOrDefault(playerName, 0L);

            if (System.currentTimeMillis() - lastRequestTime >= 60000) {
                bazikon.sendMessage(postrequestsent_with_placeholder);
                for (Player onlinePlayers : Bukkit.getOnlinePlayers()) {
                    if (onlinePlayers.hasPermission("Postoffice.get")) {
                        onlinePlayers.playSound(onlinePlayers.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
                        onlinePlayers.sendMessage(postreceive_with_placeholder);
                    }
                }
                playerRequestTimes.put(playerName, System.currentTimeMillis());
            } else {
                bazikon.sendMessage(color( "&f&l[&c&lPost Office&f&l]&r &cYou cannot submit a new request yet."));
            }
        } else {
            bazikon.sendMessage(color( "&f&l[&c&lPost Office&f&l]&r &cYou cannot submit a new request yet."));
        }
        return true;
    }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}