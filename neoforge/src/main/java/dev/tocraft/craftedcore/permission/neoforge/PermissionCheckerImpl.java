package dev.tocraft.craftedcore.permission.neoforge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * NeoForge implementation of the PermissionManager using NeoForge PermissionAPI
 */
@SuppressWarnings("unused")
public class PermissionCheckerImpl {

    // Cache for permission nodes to avoid recreating them
    private static final ConcurrentMap<ResourceLocation, PermissionNode<Boolean>> PERMISSION_NODES = new ConcurrentHashMap<>();

    public static boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        // Get or create permission node
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, permission);
        PermissionNode<Boolean> node = PERMISSION_NODES.get(id);

        // Check permission using NeoForge PermissionAPI
        return node != null ? PermissionAPI.getPermission(player, node) : player.hasPermissions(2);
    }

    /**
     * This MUST be called while registering the nodes!
     */
    public static PermissionNode<Boolean> createNode(@NotNull String namespace, @NotNull String permission) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(namespace, permission);
        return PERMISSION_NODES.put(id, new PermissionNode<>(id, PermissionTypes.BOOLEAN,
                (player, playerUUID, context) -> player != null && player.hasPermissions(2)));
    }
}
