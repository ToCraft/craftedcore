package tocraft.craftedcore.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.netty.buffer.Unpooled;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.config.annotions.Synchronize;
import tocraft.craftedcore.events.client.ClientPlayerEvents;
import tocraft.craftedcore.network.NetworkManager;
import tocraft.craftedcore.platform.Platform;

public class ConfigLoader {
	static ResourceLocation CONFIG_SYNC = CraftedCore.id("config_sync");
	
	private static final List<Config> LOADED_CONFIGS = new ArrayList<Config>();
	private static final List<Config> CLIENT_CONFIGS = new ArrayList<Config>();

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Gson SYNC_ONLY_GSON = new GsonBuilder().addSerializationExclusionStrategy(new SynchronizeStrategy()).setPrettyPrinting().create();

    public static void registerConfigSyncHandler() {
    	
		NetworkManager.registerReceiver(NetworkManager.Side.S2C, CONFIG_SYNC,
				ConfigLoader::handleConfigSyncPackage);

		// unload configs and load local ones
		ClientPlayerEvents.CLIENT_PLAYER_QUIT.register(player -> {
			for (Config config : ConfigLoader.CLIENT_CONFIGS) {
	            for (Config potentiallySynced : ConfigLoader.LOADED_CONFIGS) {
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
	                                e.printStackTrace();
	                            }
	                        }
	                    }
	                }
	            }
	        }
			ConfigLoader.CLIENT_CONFIGS.clear();
		});
	}
    
	public static <C extends Config> C read(String configName, Class<C> configClass) {
		try {
			Path configFolder = Platform.getConfigPath();
			Path configFile = Paths.get(configFolder.toString(), configName + ".json");

			if (!Files.exists(configFile)) {
				// Write & return a configuration file
				C config = configClass.getDeclaredConstructor().newInstance();
				writeConfigFile(configFile, config);
				
				LOADED_CONFIGS.add(config);
				return config;
			}
			else {
				C newConfig = GSON.fromJson(Files.readString(configFile), configClass);

				// If the configuration existed, it's read now. This overrides it again to ensure every field is represented
				writeConfigFile(configFile, newConfig);

				LOADED_CONFIGS.add(newConfig);
				return newConfig;
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private static <C extends Config> void writeConfigFile(Path file, C config) {
		try {
			if (!Files.exists(file)) {
				Files.createFile(file);
			}

			Files.writeString(file, GSON.toJson(config));
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static <C extends Config> void sendConfigSyncPackages(ServerPlayer target) {
		FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

		CompoundTag tag = new CompoundTag();
		ListTag list = new ListTag();
		for (Config config : LOADED_CONFIGS) {
			CompoundTag configTag = new CompoundTag();
			// Synchronize whole class if trigerred
			if (Arrays.stream(config.getClass().getDeclaredAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize)) {
				configTag.putString("ConfigName", config.getClass().getSimpleName());
				configTag.putString("Serialized", GSON.toJson(config));
	            configTag.putBoolean("AllSync", true);
			}
			// trigerred if not the whole class is to be synchronized
			else {
				configTag.putString("ConfigName", config.getClass().getSimpleName());
				configTag.putString("Serialized", SYNC_ONLY_GSON.toJson(config));
	            configTag.putBoolean("AllSync", false);
			}
			
			list.add(configTag);
		};
		tag.put("configs", list);
        packet.writeNbt(tag);
        if (!list.isEmpty())
        	NetworkManager.sendToPlayer(target, CONFIG_SYNC, packet);
    }
	
	private static void handleConfigSyncPackage(FriendlyByteBuf packet, NetworkManager.PacketContext contex) {
		CLIENT_CONFIGS.clear();
		
		CompoundTag tag = packet.readNbt();
		ListTag list = (ListTag) tag.get("configs");
		
		list.forEach(compound -> {
            CompoundTag syncedConfiguration = (CompoundTag) compound;
            String name = syncedConfiguration.getString("ConfigName");
            String json = syncedConfiguration.getString("Serialized");
            boolean allSync = syncedConfiguration.getBoolean("AllSync");

            // get all loaded configs
            for (Config config : LOADED_CONFIGS) {
                if (config.getClass().getSimpleName().equals(name)) {
                	// get send configuration as serverConfig
                    Config serverConfig = GSON.fromJson(json, config.getClass());

                    Config cachedClient = GSON.fromJson(GSON.toJson(config), config.getClass());
                    CLIENT_CONFIGS.add(cachedClient);

                    // override local fields with server ones
                    for (Field field : serverConfig.getClass().getDeclaredFields()) {
                        if (allSync || Arrays.stream(field.getAnnotations()).anyMatch(annotation -> annotation instanceof Synchronize)) {
                            try {
                                field.setAccessible(true);
                                Object serverValue = field.get(serverConfig);
                                field.set(config, serverValue);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    break;
                }
            }
        });
	}
}
