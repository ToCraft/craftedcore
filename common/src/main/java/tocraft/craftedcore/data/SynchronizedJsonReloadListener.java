package tocraft.craftedcore.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a SimpleJsonResourceReloadListener that synchronizes with every client and therefore should run serverside
 */
@SuppressWarnings("unused")
public abstract class SynchronizedJsonReloadListener extends
        SimplePreparableReloadListener<Map<ResourceLocation, JsonElement>> {
    public final ResourceLocation RELOAD_SYNC;
    protected final String directory;
    protected final Gson gson;
    private final Map<ResourceLocation, JsonElement> map = new HashMap<>();

    public SynchronizedJsonReloadListener(Gson gson, String directory) {
        this.gson = gson;
        this.directory = directory;
        this.RELOAD_SYNC = CraftedCore.id("data_sync_" + directory);
    }

    protected @NotNull Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        Map<ResourceLocation, JsonElement> map = new HashMap<>();
        scanDirectory(resourceManager, map);
        return map;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.map.clear();
        this.map.putAll(map);
        this.onApply(map);
    }

    protected abstract void onApply(Map<ResourceLocation, JsonElement> map);

    public void sendSyncPacket(ServerPlayer player) {
        // Serialize unlocked to tag
        CompoundTag compound = new CompoundTag();
        this.map.forEach((key, json) -> compound.putString(key.toString(), json.toString()));

        // Send to client
        ModernNetworking.sendToPlayer(player, RELOAD_SYNC, compound);
    }

    @Environment(EnvType.CLIENT)
    private void onPacketReceive(ModernNetworking.Context context, CompoundTag compound) {
        Map<ResourceLocation, JsonElement> map = new HashMap<>();
        if (compound != null) {
            for (String key : compound.getAllKeys()) {
                this.map.put(ResourceLocation.parse(key), JsonParser.parseString(compound.getString(key)));
            }
        }
        this.onApply(map);
    }

    @Environment(EnvType.CLIENT)
    public void registerPacketReceiver() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, RELOAD_SYNC, this::onPacketReceive);
    }

    private void scanDirectory(ResourceManager resourceManager, Map<ResourceLocation, JsonElement> map) {
        FileToIdConverter var4 = FileToIdConverter.json(directory);
        Iterable<Map.Entry<ResourceLocation, Resource>> entrySet = var4.listMatchingResources(resourceManager).entrySet();
        for (Map.Entry<ResourceLocation, Resource> entry : entrySet) {
            ResourceLocation var7 = entry.getKey();
            ResourceLocation var8 = var4.fileToId(var7);
            try {
                BufferedReader reader = entry.getValue().openAsReader();
                try {
                    JsonElement var10 = GsonHelper.fromJson(gson, reader, JsonElement.class);
                    JsonElement var11 = map.put(var8, var10);
                    if (var11 != null) {
                        throw new IllegalStateException("Duplicate data file ignored with ID " + var8);
                    }
                } catch (Throwable var13) {
                    try {
                        reader.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                    throw var13;
                }
                reader.close();
            } catch (IllegalArgumentException | IOException | JsonParseException var14) {
                LogUtils.getLogger().error("Couldn't parse data file {} from {}", var8, var7, var14);
            }
        }
    }
}
