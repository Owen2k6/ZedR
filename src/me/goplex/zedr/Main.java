package me.goplex.zedr;

import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getLogger;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("ZedR has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ZedR has been disabled!");
    }
}
