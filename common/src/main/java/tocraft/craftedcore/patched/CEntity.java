package tocraft.craftedcore.patched;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CEntity {
    public static Level level(Entity entity) {
        return entity.level();
    }

    public static boolean isOnGround(Entity entity) {
        return entity.onGround();
    }
}
