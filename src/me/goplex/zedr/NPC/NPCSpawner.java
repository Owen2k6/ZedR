package me.goplex.zedr.NPC;

import java.util.ArrayList;

public class NPCSpawner {
    protected ArrayList<NPC> NPCs;
    private int id;

    public void checkNPCs() {
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

    // Todo: this entire class
}
