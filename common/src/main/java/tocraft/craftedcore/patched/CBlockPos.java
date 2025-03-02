package tocraft.craftedcore.patched;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class CBlockPos {
    public static BlockPos containing(Vec3 vec3) {
        return BlockPos.containing(vec3);
    }
}
