package tocraft.craftedcore.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.config.annotions.Comment;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.patched.TComponent;
import tocraft.craftedcore.platform.PlatformData;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.List;

@SuppressWarnings("unused")
public interface Config {
    @Nullable
    String getName();

    @Nullable
    default Path getPath() {
        return getName() != null ? ConfigLoader.getConfigPath(getName()) : null;
    }

    default void save() {
        ConfigLoader.writeConfigFile(this);
    }

    default void sendToPlayer(ServerPlayer target) {
        ModernNetworking.sendToPlayer(target, ConfigLoader.CONFIG_SYNC, ConfigLoader.getConfigSyncTag(this));
    }

    default void sendToAllPlayers(ServerLevel serverLevel) {
        for (ServerPlayer target : serverLevel.players()) {
            sendToPlayer(target);
        }
    }

    @SuppressWarnings("unchecked")
    @Environment(EnvType.CLIENT)
    @Nullable
    default Screen constructConfigScreen(Screen parent) {
        if (PlatformData.isModLoaded("cloth-config") || PlatformData.isModLoaded("cloth_config")) {
            try {
                Config defaultC = getClass().getDeclaredConstructor().newInstance();

                ConfigBuilder builder = ConfigBuilder.create()
                        .setParentScreen(parent)
                        .setTitle(TComponent.literal(String.format("Config Screen for %s", getName())))
                        .setSavingRunnable(this::save);

                ConfigCategory general = builder.getOrCreateCategory(TComponent.literal("General"));

                // Fields
                for (Field field : this.getClass().getDeclaredFields()) {
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

                        CraftedCore.LOGGER.warn("Clazz of field {} is {}", name, field.getType());

                        if (boolean.class.isAssignableFrom(field.getType()) || Boolean.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startBooleanToggle(TComponent.literal(name), field.getBoolean(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue(field.getBoolean(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.setBoolean(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                            CraftedCore.LOGGER.warn("registered boolean " + name + " info; " + tooltip);
                        } else if (int.class.isAssignableFrom(field.getType()) || Integer.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startIntField(TComponent.literal(name), field.getInt(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue(field.getInt(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.setInt(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                        } else if (float.class.isAssignableFrom(field.getType()) || Float.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startFloatField(TComponent.literal(name), field.getFloat(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue(field.getFloat(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.setFloat(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                        } else if (double.class.isAssignableFrom(field.getType()) || Double.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startDoubleField(TComponent.literal(name), field.getDouble(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue(field.getDouble(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.setDouble(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                        } else if (long.class.isAssignableFrom(field.getType()) || Long.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startLongField(TComponent.literal(name), field.getLong(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue(field.getLong(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.setLong(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                        } else if (String.class.isAssignableFrom(field.getType())) {
                            general.addEntry(entryBuilder.startStrField(TComponent.literal(name), (String) field.get(this))
                                    .setTooltip(TComponent.literal(tooltip))
                                    .setDefaultValue((String) field.get(defaultC))
                                    .setSaveConsumer(n -> {
                                        try {
                                            field.set(this, n);
                                        } catch (IllegalAccessException e) {
                                            CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                        }
                                    })
                                    .build());
                        } else if (List.class.isAssignableFrom(field.getType())) {
                            List<?> list = (List<?>) field.get(this);
                            if (list.isEmpty()) {
                                list = (List<?>) field.get(defaultC);
                            }

                            if (!list.isEmpty()) {
                                if (list.stream().allMatch(e -> e instanceof String)) {
                                    general.addEntry(entryBuilder.startStrList(TComponent.literal(name), (List<String>) field.get(this))
                                            .setTooltip(TComponent.literal(tooltip))
                                            .setDefaultValue((List<String>) field.get(defaultC))
                                            .setSaveConsumer(n -> {
                                                try {
                                                    field.set(this, n);
                                                } catch (IllegalAccessException e) {
                                                    CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                                }
                                            })
                                            .build());
                                } else if (list.stream().allMatch(e -> e instanceof Integer)) {
                                    general.addEntry(entryBuilder.startIntList(TComponent.literal(name), (List<Integer>) field.get(this))
                                            .setTooltip(TComponent.literal(tooltip))
                                            .setDefaultValue((List<Integer>) field.get(defaultC))
                                            .setSaveConsumer(n -> {
                                                try {
                                                    field.set(this, n);
                                                } catch (IllegalAccessException e) {
                                                    CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                                }
                                            })
                                            .build());
                                } else if (list.stream().allMatch(e -> e instanceof Float)) {
                                    general.addEntry(entryBuilder.startFloatList(TComponent.literal(name), (List<Float>) field.get(this))
                                            .setTooltip(TComponent.literal(tooltip))
                                            .setDefaultValue((List<Float>) field.get(defaultC))
                                            .setSaveConsumer(n -> {
                                                try {
                                                    field.set(this, n);
                                                } catch (IllegalAccessException e) {
                                                    CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                                }
                                            })
                                            .build());
                                } else if (list.stream().allMatch(e -> e instanceof Double)) {
                                    general.addEntry(entryBuilder.startDoubleList(TComponent.literal(name), (List<Double>) field.get(this))
                                            .setTooltip(TComponent.literal(tooltip))
                                            .setDefaultValue((List<Double>) field.get(defaultC))
                                            .setSaveConsumer(n -> {
                                                try {
                                                    field.set(this, n);
                                                } catch (IllegalAccessException e) {
                                                    CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                                }
                                            })
                                            .build());
                                } else if (list.stream().allMatch(e -> e instanceof Long)) {
                                    general.addEntry(entryBuilder.startLongList(TComponent.literal(name), (List<Long>) field.get(this))
                                            .setTooltip(TComponent.literal(tooltip))
                                            .setDefaultValue((List<Long>) field.get(defaultC))
                                            .setSaveConsumer(n -> {
                                                try {
                                                    field.set(this, n);
                                                } catch (IllegalAccessException e) {
                                                    CraftedCore.LOGGER.error("Couldn't save config field {}", name);
                                                }
                                            })
                                            .build());
                                }
                            }
                        }
                    } catch (IllegalAccessException e) {
                        CraftedCore.LOGGER.error("Couldn't create config entry {} for config {}", name, getName());
                    }
                }

                return builder.build();
            } catch (Exception e) {
                CraftedCore.LOGGER.error("Couldn't create config file {}", getName());
                return null;
            }
        } else {
            CraftedCore.LOGGER.warn("Cloth config not found!");
            return null;
        }
    }
}
