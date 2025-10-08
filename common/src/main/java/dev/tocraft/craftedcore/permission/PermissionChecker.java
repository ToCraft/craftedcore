package dev.tocraft.craftedcore.permission;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

/**
 * Cross-platform permission manager interface for ReMorphed mod.
 * Implementations handle platform-specific permission checks.
 * NOTE: Requires Fabric Permissions API on Fabric to work properly
 */
@SuppressWarnings("unused")
public class PermissionChecker {

    /**
     * Check if a player has a specific permission node
     *
     * @param player     The player to check
     * @param namespace The namespace of the permission, most time the mod id
     * @param permission The permission node to check
     * @return true if the player has the permission
     */
    @ExpectPlatform
    public static boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        throw new AssertionError();
    }
}
