package me.goplex.zedr.NPC;

import me.goplex.zedr.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Random;
import java.util.UUID;

public class NPCConfig {
    private String name;
    private int uid;
    private Location spawnLocation;
    protected boolean godMode;
    private int maxHealth;
    private boolean canRespawn;
    private boolean timed;
    private long startTime;
    private long endTime;
    protected boolean delayEndTime;
    protected boolean canWalk;
    protected boolean canDrive;

    public NPCConfig(final Location spawnLocation, final boolean godMode, final int maxHealth, final boolean canRespawn, final boolean timed, final long startTime, final long endTime, final boolean canWalk, final boolean canDrive) {
        Random random = new Random();
        while (uid == 0) {
            boolean isUIDalreadyUsed = false;
            int randomNumber = random.nextInt(1000000);
            for (int i = 0; i < Main.NPCs.size(); i++) if (randomNumber == Main.NPCs.get(i).getConfig().getUID()) isUIDalreadyUsed = true;
            if (!isUIDalreadyUsed) uid = randomNumber;
        }

        this.spawnLocation = spawnLocation == null ? Bukkit.getServer().getWorlds().get(0).getSpawnLocation() : spawnLocation;
        this.godMode = godMode;
        this.maxHealth = maxHealth == 0 ? 20 : maxHealth;
        this.canRespawn = canRespawn;
        this.timed = timed;
        this.startTime = startTime;
        this.endTime = endTime;
        this.delayEndTime = false;
        this.canWalk = canWalk;
        this.canDrive = canDrive;
    }

    public int getUID() { return uid; }
    public Location getSpawnLocation() { return spawnLocation; }
    public int getMaxHealth() { return maxHealth; }
    public boolean getCanRespawn() { return canRespawn; }
    public boolean getTimed() { return timed; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
}
