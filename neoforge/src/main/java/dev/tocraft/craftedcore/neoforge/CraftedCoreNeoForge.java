package dev.tocraft.craftedcore.neoforge;

import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.neoforge.client.CraftedCoreNeoForgeClient;
import dev.tocraft.craftedcore.registration.neoforge.RegistryRegistryImpl;
import net.minecraft.core.Registry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
@ApiStatus.Internal
@Mod(CraftedCore.MODID)
public class CraftedCoreNeoForge {

    public CraftedCoreNeoForge(IEventBus bus) {
        if (FMLEnvironment.getDist().isClient()) {
            new CraftedCoreNeoForgeClient(bus);
        }
        new CraftedCore().initialize();

        NeoForge.EVENT_BUS.register(new CraftedCoreNeoForgeEventHandler());

        bus.addListener(CraftedCoreNeoForge::registerRegistries);

    }

    private static void registerRegistries(@NotNull NewRegistryEvent event) {
        for (Registry<?> registry : RegistryRegistryImpl.getRegistries()) {
            event.register(registry);
        }
    }

    public static IEventBus getEventBus() {
        return Objects.requireNonNull(ModList.get().getModContainerById(CraftedCore.MODID).orElseThrow().getEventBus());
    }
}
