package dev.tocraft.craftedcore.fabric;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import org.jetbrains.annotations.ApiStatus;
import dev.tocraft.craftedcore.CraftedCoreConfig;
import dev.tocraft.craftedcore.config.Config;
import dev.tocraft.craftedcore.config.ConfigLoader;
import dev.tocraft.craftedcore.fabric.client.CraftedCoreFabricClient;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@ApiStatus.Internal
public class CraftedCoreModMenu implements ModMenuApi {
    @Override
    public Map<String, ConfigScreenFactory<?>> getProvidedConfigScreenFactories() {
        Map<String, ConfigScreenFactory<?>> map = new HashMap<>();

        for (String name : CraftedCoreFabricClient.CONFIGS) {
            Config config = ConfigLoader.getConfigByName(name);
            if (config != null) {
                map.put(name, config::constructConfigScreen);
            }
        }

        return map;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return CraftedCoreConfig.INSTANCE::constructConfigScreen;
    }
}
