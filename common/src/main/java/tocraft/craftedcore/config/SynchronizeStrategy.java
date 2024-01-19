package tocraft.craftedcore.config;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import tocraft.craftedcore.config.annotions.Synchronize;

public class SynchronizeStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotations().stream().noneMatch(annotation -> annotation instanceof Synchronize);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
