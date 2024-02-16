package tocraft.craftedcore.config;

import org.jetbrains.annotations.NotNull;
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
        return getName() != null ? ConfigLoader.getConfigPath(getName()) : null;
    }

    default void save() {
        ConfigLoader.writeConfigFile(this);
    }
}
