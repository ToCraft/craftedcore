package tocraft.craftedcore.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.config.annotions.Comment;
import tocraft.craftedcore.config.annotions.Synchronize;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;
import tocraft.craftedcore.util.JsonUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// heavily based on the work of Draylar - https://github.com/Draylar/omega-config/ and therefore this class is licensed under MIT
public class ConfigLoader {
    public static final ResourceLocation CONFIG_SYNC = CraftedCore.id("config_sync");
    private static final Map<String, Config> LOADED_CONFIGS = new ConcurrentHashMap<>();
    private static final List<Config> CLIENT_CONFIGS = new ArrayList<>();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().setLenient().create();
    private static final Gson SYNC_ONLY_GSON = new GsonBuilder().addSerializationExclusionStrategy(new SynchronizeStrategy()).create();

    @Environment(EnvType.CLIENT)
    public static void registerConfigSyncHandler() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, CONFIG_SYNC, ConfigLoader::handleConfigSyncPackage);

        // unload configs and load local ones
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.register(player -> {
            for (Config config : ConfigLoader.CLIENT_CONFIGS) {
                for (Config potentiallySynced : ConfigLoader.LOADED_CONFIGS.values()) {
                    if (config.getClass().getSimpleName().equals(potentiallySynced.getClass().getSimpleName())) {
                        boolean allConfigSyncs = Arrays.stream(config.getClass().getAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize);

                        // mutate object in registered configurations
                        for (Field field : config.getClass().getDeclaredFields()) {
                            if (allConfigSyncs || Arrays.stream(field.getAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize)) {
                                try {
                                    field.setAccessible(true);
                                    Object preSyncValue = field.get(config);
                                    field.set(potentiallySynced, preSyncValue);
                                } catch (IllegalAccessException e) {
                                    CraftedCore.LOGGER.error("Failed reverting modifications on config {}", config.getClass().getSimpleName(), e);
                                }
                            }
                        }
                    }
                }
            }
            ConfigLoader.CLIENT_CONFIGS.clear();
        });
    }

    @SafeVarargs
    public static <C extends Config> C register(String name, C... typeGetter) {
        //noinspection unchecked
        Class<C> clazz = (Class<C>) typeGetter.getClass().getComponentType();

        C config = read(name, clazz);

        if (PlatformData.getEnv() == EnvType.CLIENT) {
            PlatformData.registerConfigScreen(name);
        }

        return config;
    }

    public static <C extends Config> C read(String configName, Class<C> configClass) {
        try {
            Path configFile = getConfigPath(configName);

            if (!Files.exists(configFile)) {
                // read old config file if exists
                Path oldConfigFile = PlatformData.getConfigPath().resolve(configName + ".json");

                if (Files.exists(oldConfigFile)) {
                    C oldConfig = GSON.fromJson(Files.readString(oldConfigFile), configClass);
                    // config was readable
                    if (oldConfig != null) {
                        // write to new config file
                        writeConfigFile(configFile, oldConfig);
                        // delete old config file
                        Files.delete(oldConfigFile);

                        LOADED_CONFIGS.put(configName, oldConfig);
                        return oldConfig;
                    }
                }

                // if this was reached, the old config couldn't be read
                // Write & return a new configuration file
                C config = configClass.getDeclaredConstructor().newInstance();
                writeConfigFile(configFile, config);

                LOADED_CONFIGS.put(configName, config);
                return config;
            } else {
                C newConfig = GSON.fromJson(Files.readString(configFile), configClass);

                // some files might be malfunctions
                if (newConfig == null) {
                    newConfig = configClass.getDeclaredConstructor().newInstance();
                    CraftedCore.LOGGER.error("The Configuration '{}.json' is null. This isn't normal. It will be overwritten with default values.", configName);
                }

                // If the configuration existed, it's read now. This overrides it again to ensure every field is represented
                writeConfigFile(configFile, newConfig);

                LOADED_CONFIGS.put(configName, newConfig);
                return newConfig;
            }
        } catch (Exception e) {
            CraftedCore.LOGGER.error("Failed reading config {}", configName, e);
            return null;
        }
    }

    private static <C extends Config> void writeConfigFile(Path file, C config) {
        String json = GSON.toJson(config);
        Map<String, String> fieldToComments = new HashMap<>();

        // map comments to the fields
        for (Field field : config.getClass().getDeclaredFields()) {
            for (Annotation annotation : field.getDeclaredAnnotations()) {
                if (annotation instanceof Comment comment) {
                    fieldToComments.put(field.getName(), comment.value());
                    break;
                }
            }
        }

        String jsonWithComment = JsonUtils.addComments(json, fieldToComments);

        try {
            if (!Files.exists(file)) {
                Files.createFile(file);
            }

            Files.writeString(file, jsonWithComment);
        } catch (IOException e) {
            CraftedCore.LOGGER.error("Failed saving config at {}", file, e);
        }
    }

    public static CompoundTag getConfigSyncTag(Config config) {
        CompoundTag configTag = new CompoundTag();
        // Synchronize whole class if triggered
        if (Arrays.stream(config.getClass().getDeclaredAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize)) {
            configTag.putString("ConfigName", config.getClass().getSimpleName());
            configTag.putString("Serialized", GSON.toJson(config));
            configTag.putBoolean("AllSync", true);
        }
        // triggered if not the whole class is to be synchronized
        else {
            configTag.putString("ConfigName", config.getClass().getSimpleName());
            configTag.putString("Serialized", SYNC_ONLY_GSON.toJson(config));
            configTag.putBoolean("AllSync", false);
        }

        return configTag;
    }

    public static void sendConfigSyncPackages(ServerPlayer target) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        // forEach is required for some iteration stuff to prevent "CurrentModificationException"
        LOADED_CONFIGS.values().forEach(config -> list.add(getConfigSyncTag(config)));

        tag.put("configs", list);
        if (!list.isEmpty()) ModernNetworking.sendToPlayer(target, CONFIG_SYNC, tag);
    }

    @SuppressWarnings("unused")
    private static void handleConfigSyncPackage(ModernNetworking.Context context, CompoundTag tag) {
        CLIENT_CONFIGS.clear();

        if (tag != null && tag.contains("configs")) {
            ListTag list = (ListTag) tag.get("configs");

            if (list != null) {
                for (Tag compound : list) {
                    handleConfigTag((CompoundTag) compound);
                }
            }
        } else if (tag != null && tag.contains("ConfigName")) {
            handleConfigTag(tag);
        } else {
            CraftedCore.LOGGER.error("Failed to handle Config Sync Package.");
        }

    }

    private static void handleConfigTag(CompoundTag syncedConfiguration) {
        String name = syncedConfiguration.getString("ConfigName");
        String json = syncedConfiguration.getString("Serialized");
        boolean allSync = syncedConfiguration.getBoolean("AllSync");

        // get all loaded configs
        for (Config config : LOADED_CONFIGS.values()) {
            if (config.getClass().getSimpleName().equals(name)) {
                // get send configuration as serverConfig
                Config serverConfig = GSON.fromJson(json, config.getClass());

                Config cachedClient = GSON.fromJson(GSON.toJson(config), config.getClass());
                CLIENT_CONFIGS.add(cachedClient);

                // override local fields with server ones
                for (Field field : config.getClass().getDeclaredFields()) {
                    if (allSync || Arrays.stream(field.getAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize)) {
                        try {
                            field.setAccessible(true);
                            Object serverValue = field.get(serverConfig);
                            field.set(config, serverValue);
                        } catch (IllegalAccessException e) {
                            CraftedCore.LOGGER.error("Failed modifying config {}", config.getClass().getSimpleName(), e);
                        }
                    }
                }

                break;
            }
        }
    }

    @SuppressWarnings("unused")
    @Nullable
    public static Config getConfigByName(String configName) {
        return LOADED_CONFIGS.get(configName);
    }

    @ApiStatus.Internal
    @NotNull
    public static Path getConfigPath(String configName) {
        return PlatformData.getConfigPath().resolve(configName + ".json5");
    }

    @ApiStatus.Internal
    public static <C extends Config> void writeConfigFile(C config) {
        writeConfigFile(getConfigPath(config.getName()), config);
    }
}
