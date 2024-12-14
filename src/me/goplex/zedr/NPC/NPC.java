package me.goplex.zedr.NPC;

import me.goplex.zedr.EzPacket;
import me.goplex.zedr.Main;
import net.minecraft.server.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;

public class NPC extends EntityPlayer {
    private CraftWorld world;
    private EzPacket ezPacket;

    private int uid;
    private NPCSpawner parent;

    private Location spawnLocation;
    private boolean godMode;
    private boolean canDrive;
    private boolean canWalk;
    private boolean canRespawn;

    public boolean spawnFlag; // This gets set to true when the NPC fails to spawn when a player is near.
    // It will then attempt to spawn every five seconds until the player leaves the area.

    // This is for manually spawned NPCs
    public NPC(MinecraftServer minecraftserver, CraftWorld world, String name, int uid, Location spawnLocation, boolean godMode, boolean canDrive, boolean canWalk, boolean canRespawn, ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world.getHandle(), "&" + name, iteminworldmanager);

        this.world = world;

        for (NPC e : Main.NPCs) if (uid == e.getUid()) uid = 0; // Check if UID is already used, if so set UID to 0

        while (uid == 0) { // Loop if the uid is 0
            // Set UID to random 6-digit number. Easy enough to type and should be plenty. Also, apparently the entity class has a Random built in, so we don't need to set up our own.
            uid = random.nextInt(1000000);
            for (NPC e : Main.NPCs) if (uid == e.getUid()) uid = 0; // Check if it's unique. If not, set it to 0 so it loops again
        }

        this.parent = null; // Not a spawner NPC so it doesn't have a parent

        // Set up some things
        ezPacket = new EzPacket(world.getHandle().server.serverConfigurationManager);
        health = 20;

        Main.NPCs.add(this); // Add NPC to Main's NPC list

        // Spawn NPC into the world. We will disregard nearby players for this as if someone is manually spawning an NPC it should show up.
        spawn();
    }

    // This is for Spawner NPCs
    // They have no individuality (so they don't get UIDs) and are easily replaceable (so we don't give them the option to respawn)
    public NPC(MinecraftServer minecraftserver, CraftWorld world, String name, NPCSpawner parent, Location spawnLocation, boolean godMode, boolean canDrive, boolean canWalk, ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world.getHandle(), "&" + name, iteminworldmanager);

        this.world = world;

        this.uid = -1; // This type of NPC doesn't need a UID, you aren't supposed to interact with them (directly) via commands.
        // We do technically need to give it a value, but nothing unique or useful in any way.

        this.parent = parent; // Instead of a UID it has a parent

        // Set up some things
        this.canRespawn = false; // These NPCs are replaceable, and will be replaced.
        ezPacket = new EzPacket(world.getHandle().server.serverConfigurationManager);
        health = 20;

        // Spawn NPC into the world (and set spawnFlag accordingly. If spawn fails due to a nearby player we need to wait until they leave).
        spawnFlag = !spawn();
    }

    public boolean spawn() {
        // If a player is near DO NOT spawn.
        // I tested the range in GMC and it was 208 blocks for max render distance, but I added a few more for safety.
        if (Main.isPlayerNear(this.dimension, spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), 222)) return false;

        this.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getPitch(), spawnLocation.getYaw()); // Set NPC location
        world.getHandle().addEntity(this); // Add NPC to world
        getWorldServer().players.remove(this); // Remove NPC from list of players (so the server doesn't try to send packets to its non-existent network stuff and crap itself)
        world.getHandle().entityList.remove(this); // Remove NPC from list of entities (so the server doesn't try to send it things like time updates, which will also make it crap itself)
        return true;
    }

    public void kill() {
        world.getHandle().removeEntity(this); // Delete NPC from the world, but not fully from existence.
        if (!canRespawn) delete(); // If the NPC can't respawn, properly delete it.
        else spawnFlag = !spawn(); // Otherwise, respawn the NPC (and set spawnFlag accordingly)
    }

    public void delete() {
        if (parent != null) parent.NPCs.remove(this); // If it's a spawner NPC, then delete it from the list of NPCs in its parent
        else Main.NPCs.remove(this); // If not, then delete it from the list of NPCs in Main
    }

    @Override
    public boolean damageEntity(Entity entity, int i) {
        if (godMode) return false;
        else {
            // If PVP is off and the NPC is hit by something a player did, forget the idea of getting hurt.
            if (!this.world.getPVP()) {
                if (entity instanceof EntityHuman) {
                    return false;
                }

                if (entity instanceof EntityArrow) {
                    EntityArrow entityarrow = (EntityArrow) entity;

                    if (entityarrow.shooter instanceof EntityHuman) {
                        return false;
                    }
                }
            }

            // So the NPC was actually hit by something it can be hit by. Remove the amount of damage from the health.
            // Todo: Make this act like the actual damage code.
            // I'm pretty sure the way it works is a little more complicated than this. It works tho, so i'm most certainly not doing it any time soon.
            this.health -= i;

            // If it was hit by a player send a message to nearby players.
            // This is only really a test, a much better system for this will come at some point.
            if (entity instanceof EntityPlayer) ezPacket.ezSendNearby(this.locX, this.locY, this.locZ, 20, this.dimension, new Packet3Chat("<" + this.name + "> Ouch!"));

            // The NPC has reached 0 (or lower) health. It must now DIE!
            if (this.health <= 0) kill();
            return true;
        }
    }

    // Below here it's just getters. No need to read it
    public int getUid() { return uid; }
}
