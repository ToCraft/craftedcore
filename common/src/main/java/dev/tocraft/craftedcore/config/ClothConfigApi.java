package dev.tocraft.craftedcore.config;

import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.config.annotions.Comment;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ClothConfigApi {
    public static @Nullable Screen constructConfigScreen(Config config, Screen parent) {
        try {
            Config defaultC = config.getClass().getDeclaredConstructor().newInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(Component.literal(String.format("Config Screen for %s", config.getName())))
                    .setSavingRunnable(config::save);

            ConfigCategory general = builder.getOrCreateCategory(Component.literal("General"));

            // Fields
            for (Field field : config.getClass().getDeclaredFields()) {
                String name = field.getName();
                ConfigEntryBuilder entryBuilder = builder.entryBuilder();

                try {
                    String tooltip = "";

                    for (Annotation annotation : field.getDeclaredAnnotations()) {
                        if (annotation instanceof Comment comment) {
                            tooltip = comment.value();
                            break;
                        }
                    }

                    CraftedCore.LOGGER.warn("Class of field {} is {}", name, field.getType());

                    if (boolean.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startBooleanToggle(Component.literal(name), field.getBoolean(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue(field.getBoolean(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setBoolean(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                        CraftedCore.LOGGER.warn("registered boolean {} info; {}", name, tooltip);
                    } else if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startIntField(Component.literal(name), field.getInt(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue(field.getInt(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setInt(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                    } else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startFloatField(Component.literal(name), field.getFloat(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue(field.getFloat(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setFloat(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                    } else if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startDoubleField(Component.literal(name), field.getDouble(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue(field.getDouble(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setDouble(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                    } else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startLongField(Component.literal(name), field.getLong(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue(field.getLong(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setLong(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                    } else if (String.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startStrField(Component.literal(name), (String) field.get(config))
                                .setTooltip(Component.literal(tooltip))
                                .setDefaultValue((String) field.get(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.set(config, n);
                                    } catch (IllegalAccessException e) {
                                        logError(name);
                                    }
                                })
                                .build());
                    }
                } catch (IllegalAccessException e) {
                    CraftedCore.LOGGER.error("Couldn't create config entry {} for config {}. Caught: {}", name, config.getName(), e);
                }
            }

            return builder.build();
        } catch (Exception e) {
            CraftedCore.LOGGER.error("Couldn't create config file {}", config.getName());
            return null;
        }
    }

    private static void logError(String field) {
        CraftedCore.LOGGER.error("Couldn't save config field {}", field);
    }
}
