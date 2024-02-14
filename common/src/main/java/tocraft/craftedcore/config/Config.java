package tocraft.craftedcore.config;

import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.List;

public interface Config {
    @Nullable
    default String getName() {
        List<String> possibleNames = ConfigLoader.getConfigNames(this);
        return !possibleNames.isEmpty() ? possibleNames.get(0) : null;
    }

    @Nullable
    default Path getPath() {
        return ConfigLoader.getConfigPath(getName());
    }

    default void save() {
        ConfigLoader.writeConfigFile(this);
    }
}
