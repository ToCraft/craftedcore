package dev.tocraft.craftedcore.permission.fabric;

import dev.tocraft.craftedcore.CraftedCore;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Clean Fabric implementation of the PermissionManager using Fabric Permissions API
 * Based on the clean approach used in OldSchoolJail
 */
@SuppressWarnings("unused")
public class PermissionCheckerImpl {
    private static final AtomicBoolean CRASHED = new AtomicBoolean(false);

    public static boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        // Try to use Fabric Permissions API if available
        try {
            if (!CRASHED.get()) {
                Class<?> clazz = Class.forName("me.lucko.fabric.api.permissions.v0.Permissions");
                Method method = clazz.getDeclaredMethod("check", Entity.class, String.class, Integer.class);
                return (boolean) method.invoke(null, player, namespace + "." + permission, 2);
            }
        } catch (Throwable e) {
            CraftedCore.LOGGER.error("Could not access Fabric-Permission-API-v0!", e);
        }
        // Permissions API not available, fall back to OP level 2
        return player.hasPermissions(2);

    }
}
