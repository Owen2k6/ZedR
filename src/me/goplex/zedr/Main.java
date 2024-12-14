package me.goplex.zedr;

import me.goplex.zedr.NPC.NPC;
import me.goplex.zedr.NPC.NPCSpawner;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Random;

import static org.bukkit.Bukkit.getLogger;

public class Main extends JavaPlugin {
    public static Random random = new Random();
    public static ArrayList<NPCSpawner> NPCSpawners = new ArrayList<>();
    public static ArrayList<NPC> NPCs = new ArrayList<>();

    @Override
    public void onEnable() {
        // Todo: We need to load NPCs, then the spawners.
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this::checkNPCs, 0L, 5000L);
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

        String commandName = command.getName().toLowerCase();

        for (String e : args) e = e.toLowerCase(); // Off with the case sensitivity!

        // Handle commands
        switch (commandName) {
            case "zedr":{
                return true; // I do not permit things to happen. Ok maybe someday.
            }
            case "rman": {
                if (args.length < 3) {
                    // I have a Google doc that details all possible commands for /rman, and 3 args is the minimum.
                    // Thus, we can skip right to checking for if it has that many.
                    sender.sendMessage("[ZedR] Missing args!");
                    return true;
                }

                if (args[0].equals("npc") || args[0].equals("n")) return handleNPCCommands(args); // Handle NPC related commands
                else if (args[0].equals("spawner") || args[0].equals("s")) return handleSpawnerCommands(args); // Handle Spawner related Commands
                else {
                    sender.sendMessage("[ZedR] \"" + args[0] + "\" is NOT a valid argument.");
                    return true;
                }
            }
            default: {
                return false;
            }
        }
    }

    public static boolean isPlayerNear(int dimension, double x, double y, double z, double distance) {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            double d4 = x - player.getLocation().getX();
            double d5 = y - player.getLocation().getY();
            double d6 = z - player.getLocation().getZ();

            if (d4 * d4 + d5 * d5 + d6 * d6 < distance * distance && dimension == player.getWorld().getEnvironment().getId())
                return true;
        }
        return false;
    }

    private boolean handleNPCCommands(String[] args) {
        return false;
    }

    private boolean handleSpawnerCommands(String[] args) {
        return true;
    }

    private void checkNPCs() {
        // If there are any spawners, tell them to check on their NPCs too.
        if (NPCSpawners.size() > 0) for (NPCSpawner spawner : NPCSpawners) spawner.checkNPCs();

        // If there are any NPCs, check on them
        if (NPCs.size() > 0) {
            for (NPC npc : NPCs) {
                // If the NPC failed to spawn and needs to try again, then do it
                if (npc.spawnFlag) {
                    npc.spawnFlag = !npc.spawn();
                }
            }
        }
    }
}