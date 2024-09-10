package org.peakimo.postoffice;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.peakimo.postoffice.Events.ManageDispenser;
import org.peakimo.postoffice.request.PostCall;
import org.peakimo.postoffice.request.ServicePost;

public final class Post_office extends JavaPlugin {


    private PostCall postCall;
    public static Post_office instance;

    public static Post_office getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {

        saveDefaultConfig();
        instance = this;

        postCall = new PostCall(); // register PostCall Event

        // Commands
        getCommand("post-request").setExecutor(new ServicePost(postCall)); // get command
        getCommand("sendmail").setExecutor(new ManageDispenser());

        // Events

        // Sub Events
        getServer().getPluginManager().registerEvents(postCall,this); // register post-call call


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
