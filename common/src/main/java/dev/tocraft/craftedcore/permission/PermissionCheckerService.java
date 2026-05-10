package dev.tocraft.craftedcore.permission;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public interface PermissionCheckerService {
    boolean hasPermission(@NotNull ServerPlayer player, @NotNull String namespace, @NotNull String permission);
}
