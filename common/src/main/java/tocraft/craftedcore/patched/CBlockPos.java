package tocraft.craftedcore.patched;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

@SuppressWarnings("unused")
public class CBlockPos {
    //#if MC>1182
    public static BlockPos containing(Vec3 vec3) {
        return BlockPos.containing(vec3);
    }
    //#else
    //$$ public static BlockPos containing(Vec3 vec3) {
    //$$     return new BlockPos(vec3);
    //$$ }
    //#endif
}
