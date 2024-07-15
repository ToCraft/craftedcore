package tocraft.craftedcore.patched;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

public class CEntity {
    public static Level level(Entity entity) {
        //#if MC>1194
        return entity.level();
        //#else
        //$$ return entity.level;
        //#endif
    }

    public static boolean isOnGround(Entity entity) {
        //#if MC>1194
        return entity.onGround();
        //#else
        //$$ return entity.isOnGround();
        //#endif
    }
}
