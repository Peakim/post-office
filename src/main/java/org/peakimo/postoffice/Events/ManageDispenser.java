package org.peakimo.postoffice.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dispenser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public class ManageDispenser implements CommandExecutor {

    @Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args){

        if (args.length == 0) {


        }

        Player bazikon = (Player) sender;
        Block block = bazikon.getTargetBlock((Set)null, 5);
        Dispenser dispenser = (Dispenser) block.getState();
        ItemStack Item = bazikon.getInventory().getItemInMainHand();


        if (bazikon.hasPermission("Postoffice.sendmail")) {

            if (block.getState() == null){
                sender.sendMessage(color("&cYou should look at Postmail!"));
                return true;
            }


            if (dispenser.getInventory().firstEmpty() == -1) { // اگه پر بود دیسپنسر
                bazikon.sendMessage(color("&f[&cPost Mail&f] &fDispenser is full!"));
                return true;
            }
            if (block.getType() == Material.DISPENSER) {

                dispenser.getInventory().addItem(Item);
                bazikon.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                bazikon.sendMessage(color("&f[&cPost Mail&f] &fSend item to mail"));
            }
        }
        return true;
        }

    private String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);

    }

    }