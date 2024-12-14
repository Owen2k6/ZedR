package me.goplex.zedr;

import net.minecraft.server.Packet;
import net.minecraft.server.ServerConfigurationManager;
public class EzPacket {
    private ServerConfigurationManager serverConfigurationManager;

    public EzPacket(final ServerConfigurationManager serverConfigurationManager) { this.serverConfigurationManager = serverConfigurationManager; }

    public void ezSendNearby(final double x, final double y, final double z, final double distance, final int dimension, final Packet packet) {
        serverConfigurationManager.sendPacketNearby(x, y, z, distance, dimension, packet);
    }

    public void ezSend(final Packet packet) {
        serverConfigurationManager.sendAll(packet);
    }
}
