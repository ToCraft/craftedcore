package tocraft.craftedcore.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.config.annotions.Comment;
import tocraft.craftedcore.patched.TComponent;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class ClothConfigApi {
    @SuppressWarnings("unchecked")
    public static @Nullable Screen constructConfigScreen(Config config, Screen parent) {
        try {
            Config defaultC = config.getClass().getDeclaredConstructor().newInstance();

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(parent)
                    .setTitle(TComponent.literal(String.format("Config Screen for %s", config.getName())))
                    .setSavingRunnable(config::save);

            ConfigCategory general = builder.getOrCreateCategory(TComponent.literal("General"));

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
                        general.addEntry(entryBuilder.startBooleanToggle(TComponent.literal(name), field.getBoolean(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue(field.getBoolean(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setBoolean(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                        CraftedCore.LOGGER.warn("registered boolean " + name + " info; " + tooltip);
                    } else if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startIntField(TComponent.literal(name), field.getInt(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue(field.getInt(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setInt(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                    } else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startFloatField(TComponent.literal(name), field.getFloat(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue(field.getFloat(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setFloat(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                    } else if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startDoubleField(TComponent.literal(name), field.getDouble(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue(field.getDouble(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setDouble(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                    } else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startLongField(TComponent.literal(name), field.getLong(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue(field.getLong(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.setLong(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                    } else if (String.class.isAssignableFrom(field.getType())) {
                        general.addEntry(entryBuilder.startStrField(TComponent.literal(name), (String) field.get(config))
                                .setTooltip(TComponent.literal(tooltip))
                                .setDefaultValue((String) field.get(defaultC))
                                .setSaveConsumer(n -> {
                                    try {
                                        field.set(config, n);
                                    } catch (IllegalAccessException e) {
                                        CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                    }
                                })
                                .build());
                    } else if (List.class.isAssignableFrom(field.getType())) {
                        List<?> list = (List<?>) field.get(config);
                        if (list.isEmpty()) {
                            list = (List<?>) field.get(defaultC);
                        }

                        if (!list.isEmpty()) {
                            if (list.stream().allMatch(e -> e instanceof String)) {
                                general.addEntry(entryBuilder.startStrList(TComponent.literal(name), (List<String>) field.get(config))
                                        .setTooltip(TComponent.literal(tooltip))
                                        .setDefaultValue((List<String>) field.get(defaultC))
                                        .setSaveConsumer(n -> {
                                            try {
                                                field.set(config, n);
                                            } catch (IllegalAccessException e) {
                                                CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                            }
                                        })
                                        .build());
                            } else if (list.stream().allMatch(e -> e instanceof Integer)) {
                                general.addEntry(entryBuilder.startIntList(TComponent.literal(name), (List<Integer>) field.get(config))
                                        .setTooltip(TComponent.literal(tooltip))
                                        .setDefaultValue((List<Integer>) field.get(defaultC))
                                        .setSaveConsumer(n -> {
                                            try {
                                                field.set(config, n);
                                            } catch (IllegalAccessException e) {
                                                CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                            }
                                        })
                                        .build());
                            } else if (list.stream().allMatch(e -> e instanceof Float)) {
                                general.addEntry(entryBuilder.startFloatList(TComponent.literal(name), (List<Float>) field.get(config))
                                        .setTooltip(TComponent.literal(tooltip))
                                        .setDefaultValue((List<Float>) field.get(defaultC))
                                        .setSaveConsumer(n -> {
                                            try {
                                                field.set(config, n);
                                            } catch (IllegalAccessException e) {
                                                CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                            }
                                        })
                                        .build());
                            } else if (list.stream().allMatch(e -> e instanceof Double)) {
                                general.addEntry(entryBuilder.startDoubleList(TComponent.literal(name), (List<Double>) field.get(config))
                                        .setTooltip(TComponent.literal(tooltip))
                                        .setDefaultValue((List<Double>) field.get(defaultC))
                                        .setSaveConsumer(n -> {
                                            try {
                                                field.set(config, n);
                                            } catch (IllegalAccessException e) {
                                                CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                            }
                                        })
                                        .build());
                            } else if (list.stream().allMatch(e -> e instanceof Long)) {
                                general.addEntry(entryBuilder.startLongList(TComponent.literal(name), (List<Long>) field.get(config))
                                        .setTooltip(TComponent.literal(tooltip))
                                        .setDefaultValue((List<Long>) field.get(defaultC))
                                        .setSaveConsumer(n -> {
                                            try {
                                                field.set(config, n);
                                            } catch (IllegalAccessException e) {
                                                CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                            }
                                        })
                                        .build());
                            }
                        }
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
}
