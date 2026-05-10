package dev.tocraft.craftedcore.permission.neoforge;

import dev.tocraft.craftedcore.permission.PermissionCheckerService;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.neoforged.neoforge.server.permission.PermissionAPI;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;
import net.neoforged.neoforge.server.permission.nodes.PermissionTypes;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@SuppressWarnings("unused")
public class PermissionCheckerImpl implements PermissionCheckerService {
    private static final ConcurrentMap<Identifier, PermissionNode<Boolean>> PERMISSION_NODES = new ConcurrentHashMap<>();

    @Override
    public boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, permission);
        PermissionNode<Boolean> node = PERMISSION_NODES.get(id);
        return node != null ? PermissionAPI.getPermission(player, node) : player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
    }

    public static PermissionNode<Boolean> createNode(@NotNull String namespace, @NotNull String permission) {
        Identifier id = Identifier.fromNamespaceAndPath(namespace, permission);
        return PERMISSION_NODES.put(id, new PermissionNode<>(id, PermissionTypes.BOOLEAN,
                (player, playerUUID, context) -> player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER)));
    }
}
