package tocraft.craftedcore.patched;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CEntity {
    public static @NotNull Level level(@NotNull Entity entity) {
        return entity.level();
    }

    public static boolean isOnGround(@NotNull Entity entity) {
        return entity.onGround();
    }
}
