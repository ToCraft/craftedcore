package dev.tocraft.craftedcore.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import dev.tocraft.craftedcore.config.annotions.Synchronize;
import org.jetbrains.annotations.NotNull;

public class SynchronizeStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(@NotNull FieldAttributes f) {
        return f.getAnnotations().stream().noneMatch(annotation -> annotation instanceof Synchronize);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
