package dev.tocraft.craftedcore.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import dev.tocraft.craftedcore.CraftedCore;
import dev.tocraft.craftedcore.network.ModernNetworking;
import dev.tocraft.craftedcore.network.client.ClientNetworking;
import dev.tocraft.craftedcore.registration.PlayerDataRegistry;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class PlayerDataSynchronizer {
    private static final String PLAYER_DATA_SYNC = "player_data_sync";
    public static final ResourceLocation PLAYER_DATA_SYNC_ID = CraftedCore.id(PLAYER_DATA_SYNC);

    public static void registerPacketHandler() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, PLAYER_DATA_SYNC_ID, (context, tag) -> {
            if (tag != null) {
                ListTag list = (ListTag) tag.get(PLAYER_DATA_SYNC);
                Optional<int[]> uuidia = tag.getIntArray("uuid"); // Get UUID from the main packet tag
                if (list != null) {
                    for (Tag entry : list) {
                        for (String key : ((CompoundTag) entry).keySet()) {
                            if (Objects.equals(key, "DELETED")) {
                                ClientNetworking.runOrQueue(context, player -> {
                                    PlayerDataProvider playerDataProvider;
                                    if (uuidia.isPresent()) {
                                        UUID uuid = UUIDUtil.uuidFromIntArray(uuidia.get());
                                        playerDataProvider = (PlayerDataProvider) player.level().getPlayerByUUID(uuid);
                                    } else {
                                        playerDataProvider = (PlayerDataProvider) player;
                                    }
                                    if (playerDataProvider != null) {
                                        String tkey = Objects.requireNonNull(((CompoundTag) entry).get(key)).toString();
                                        playerDataProvider.craftedcore$writeTag(tkey, null);
                                    }
                                });
                            } else {
                                ClientNetworking.runOrQueue(context, player -> {
                                    PlayerDataProvider playerDataProvider;
                                    if (uuidia.isPresent()) {
                                        UUID uuid = UUIDUtil.uuidFromIntArray(uuidia.get());
                                        playerDataProvider = (PlayerDataProvider) player.level().getPlayerByUUID(uuid);
                                    } else {
                                        playerDataProvider = (PlayerDataProvider) player;
                                    }
                                    if (playerDataProvider != null) {
                                        // Deserialize the NBT Tag back to the original object using the codec
                                        Tag inputTag = ((CompoundTag) entry).get(key);
                                        // Ensure the codec is available before parsing
                                        if (PlayerDataRegistry.getTagCodec(key) != null) {
                                            DataResult<?> result = PlayerDataRegistry.getTagCodec(key).parse(NbtOps.INSTANCE, inputTag);
                                            result.result().ifPresent(obj -> playerDataProvider.craftedcore$writeTag(key, obj));
                                        } else {
                                            CraftedCore.LOGGER.warn("Codec not registered for key: {} during client sync.", key);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * Synchronize data from the server to the client
     */
    public static void sync(ServerPlayer player) {
        syncToSelf(player);
        syncToAll(player);
    }

    private static void syncToSelf(ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        PlayerDataProvider playerData = ((PlayerDataProvider) player);

        for (String key : playerData.craftedcore$keySet()) {
            // ignore key if it shouldn't be synchronized to the client
            if (!PlayerDataRegistry.shouldSyncTagToSelf(key)) {
                continue;
            }

            CompoundTag entry = new CompoundTag();
            Object value = playerData.craftedcore$readTag(key); // Get the raw object

            if (value != null) {
                // Serialize the Java object to an NBT Tag using the codec
                @SuppressWarnings("unchecked")
                Codec<Object> codec = (Codec<Object>) PlayerDataRegistry.getTagCodec(key);
                if (codec != null) {
                    codec.encodeStart(NbtOps.INSTANCE, value)
                            .resultOrPartial(CraftedCore.LOGGER::error)
                            .ifPresent(nbtTag -> entry.put(key, nbtTag));
                } else {
                    CraftedCore.LOGGER.warn("Codec not registered for key: {} during server sync (to self).", key);
                }
            } else {
                entry.put("DELETED", StringTag.valueOf(key));
            }
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);

        ModernNetworking.sendToPlayer(player, PLAYER_DATA_SYNC_ID, tag);
    }

    private static void syncToAll(@NotNull ServerPlayer player) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();

        UUID uuid = player.getUUID();
        int[] uuidia = UUIDUtil.uuidToIntArray(uuid);
        tag.putIntArray("uuid", uuidia);
        PlayerDataProvider playerData = ((PlayerDataProvider) player);

        for (String key : playerData.craftedcore$keySet()) {
            // ignore key if it shouldn't be synchronized to the client
            if (!PlayerDataRegistry.shouldSyncTagToAll(key)) {
                continue;
            }

            CompoundTag entry = new CompoundTag();
            Object value = playerData.craftedcore$readTag(key); // Get the raw object

            if (value != null) {
                // Serialize the Java object to an NBT Tag using the codec
                @SuppressWarnings("unchecked")
                Codec<Object> codec = (Codec<Object>) PlayerDataRegistry.getTagCodec(key);
                if (codec != null) {
                    codec.encodeStart(NbtOps.INSTANCE, value)
                            .resultOrPartial(CraftedCore.LOGGER::error)
                            .ifPresent(nbtTag -> entry.put(key, nbtTag));
                } else {
                    CraftedCore.LOGGER.warn("Codec not registered for key: " + key + " during server sync (to all).");
                }
            } else {
                entry.put("DELETED", StringTag.valueOf(key));
            }
            list.add(entry);
        }
        tag.put(PLAYER_DATA_SYNC, list);
        //noinspection resource
        ModernNetworking.sendToPlayers(player.level().players(), PLAYER_DATA_SYNC_ID, tag);
    }
}