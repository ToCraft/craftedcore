package tocraft.craftedcore.testmod;

import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;

@SuppressWarnings("unused")
@ApiStatus.Internal
@Mod(TestMod.MODID)
public class TestModNeoForge {
    public TestModNeoForge() {
        TestMod.initialize();
    }
}
