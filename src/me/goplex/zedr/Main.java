package me.goplex.zedr;

import me.goplex.zedr.NPC.NPC;
import me.goplex.zedr.NPC.NPCConfig;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.ItemInWorldManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getLogger;

public class Main extends JavaPlugin {
    public static ArrayList<NPC> NPCs = new ArrayList<>();

    @Override
    public void onEnable() {
        getLogger().info("ZedR has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ZedR has been disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("[ZedR] Only in-game players can run this command.");
            return true;
        }

        String commandName = command.getName();

        // Handle commands
        switch (commandName) {
            case "makeafuckinnpc": {
                if (args.length < 1) {
                    sender.sendMessage("[ZedR] Missing args!");
                    return true;
                }
                sender.sendMessage("[ZedR] Creating a new NPC...");

                Player player = (Player) sender;

                NPCConfig npcConfig = new NPCConfig(// UUID (null for random UUID)
                        player.getLocation(), // Spawn location (null for spawn at world spawn)
                        false, // GodMode
                        0, // Max health
                        false, // Respawn?
                        false, // Timed (spawns and despawns at a specified time, when players aren't nearby)
                        0, // Start time (when NPC should spawn)
                        0, // End time (when NPC should despawn)
                        false, // Can walk?
                        false // Can drive?
                );

                NPC npc = new NPC(
                        ((CraftServer) Bukkit.getServer()).getServer(), // The server
                        (CraftWorld) player.getWorld(), // The world
                        args[0], // Name (a "&" is automatically added, don't include here unless you want to have two)
                        npcConfig, // The NPCs config
                        new ItemInWorldManager(((CraftWorld) player.getWorld()).getHandle()) // The ItemInWorldManager. Whatever that is.
                );

                sender.sendMessage("[ZedR] Created a new NPC named " + npc.name + " with an ID of: " + npcConfig.getUID());
                sender.sendMessage("[ZedR] At coordinates " + "x: " + npc.locX + "y: " + npc.locY + "z: " + npc.locZ);
                return true;
            }
            default: {
                return false;
            }
        }
    }
}
