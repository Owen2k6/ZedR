package me.goplex.zedr.NPC;

import com.legacyminecraft.poseidon.event.PlayerDeathEvent;
import me.goplex.zedr.EzPacket;
import me.goplex.zedr.Main;
import net.minecraft.server.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NPC extends EntityPlayer {
    private CraftWorld world;
    private NPCConfig config;
    private EzPacket ezPacket;

    public NPC(MinecraftServer minecraftserver, CraftWorld world, String name , NPCConfig config, ItemInWorldManager iteminworldmanager) {
        super(minecraftserver, world.getHandle(), "&" + name, iteminworldmanager);

        this.world = world;
        this.config = config;

        // Set up some things
        ezPacket = new EzPacket(world.getHandle().server.serverConfigurationManager);
        health = 20;

        Main.NPCs.add(this);

        // Spawn NPC into the world
        spawn();
    }

    public void spawn() {
        Location location = config.getSpawnLocation();
        this.setLocation(location.getX(), location.getY(), location.getZ(), 0, 0);
        world.getHandle().addEntity(this);
        getWorldServer().players.remove(this);
        world.getHandle().entityList.remove(this);
    }

    public void kill() {
        world.getHandle().removeEntity(this);
    }

    public void delete() {
        kill();
        Main.NPCs.remove(this);
    }

    @Override
    public boolean damageEntity(Entity entity, int i) {
        if (config.godMode) return false;
        else {
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

            this.health -= i;

            if (entity instanceof EntityPlayer) {
                EntityPlayer player = (EntityPlayer) entity;

                ezPacket.ezSendNearby(this.locX, this.locY, this.locZ, 20, this.dimension, new Packet3Chat("<" + this.name + "> Ouch!"));
            }

            if (this.health <= 0) {
                if (config.getCanRespawn()) kill();
                else delete();
            }
            return true;
        }
    }

    public NPCConfig getConfig() { return config; }
}
