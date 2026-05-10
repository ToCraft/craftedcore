package dev.tocraft.craftedcore.permission;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

@SuppressWarnings("unused")
public class PermissionChecker {
    private static final PermissionCheckerService SERVICE =
            ServiceLoader.load(PermissionCheckerService.class).findFirst().orElseThrow();

    /**
     * Check if a player has a specific permission node
     *
     * @param player     The player to check
     * @param namespace The namespace of the permission, most time the mod id
     * @param permission The permission node to check
     * @return true if the player has the permission
     */
    public static boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        return SERVICE.hasPermission(player, namespace, permission);
    }
}
