package dev.tocraft.craftedcore.fabric.client;

import dev.tocraft.craftedcore.client.CraftedCoreClient;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class CraftedCoreFabricClient implements ClientModInitializer {
    @ApiStatus.Internal
    public static final List<String> CONFIGS = new CopyOnWriteArrayList<>();

    @Override
    public void onInitializeClient() {
        CraftedCoreFabricEventHandlerClient.initialize();

        new CraftedCoreClient().initialize();
    }
}
