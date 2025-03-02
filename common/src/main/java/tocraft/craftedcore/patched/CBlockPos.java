package tocraft.craftedcore.patched;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CBlockPos {
    @Contract("_ -> new")
    public static @NotNull BlockPos containing(Vec3 vec3) {
        return BlockPos.containing(vec3);
    }
}
