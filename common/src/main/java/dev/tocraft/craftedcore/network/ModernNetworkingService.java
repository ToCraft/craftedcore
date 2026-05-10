package dev.tocraft.craftedcore.network;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public interface ModernNetworkingService {
    void registerReceiver(ModernNetworking.Side side, Identifier id, ModernNetworking.Receiver receiver);

    void registerType(Identifier id);

    Packet<?> toPacket(ModernNetworking.Side side, CustomPacketPayload payload);
}
