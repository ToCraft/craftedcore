package tocraft.craftedcore.registration.forge;

import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ClientRegistry;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class KeyBindingRegistryImpl {
    public static void register(KeyMapping keyMapping) {
        ClientRegistry.registerKeyBinding(keyMapping);
    }
}