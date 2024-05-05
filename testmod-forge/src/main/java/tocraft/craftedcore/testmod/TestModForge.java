package tocraft.craftedcore.testmod;

import net.minecraftforge.fml.common.Mod;

@SuppressWarnings("unused")
@Mod(TestMod.MODID)
public class TestModForge {
    public TestModForge() {
        TestMod.initialize();
    }
}
