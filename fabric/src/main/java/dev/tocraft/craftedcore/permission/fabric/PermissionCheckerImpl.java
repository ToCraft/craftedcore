package dev.tocraft.craftedcore.permission.fabric;

import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.permission.PermissionCheckerService;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class PermissionCheckerImpl implements PermissionCheckerService {
    private static final AtomicBoolean CRASHED = new AtomicBoolean(false);

    @Override
    public boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission) {
        try {
            if (!CRASHED.get()) {
                Class<?> clazz = Class.forName("me.lucko.fabric.api.permissions.v0.Permissions");
                Method method = clazz.getDeclaredMethod("check", Entity.class, String.class, int.class);
                return (boolean) method.invoke(null, player, namespace + "." + permission, 2);
            }
        } catch (Throwable e) {
            CraftedCore.LOGGER.error("Could not access Fabric-Permission-API-v0!", e);
            CRASHED.set(true);
        }
        return player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER);
    }
}
