package tocraft.craftedcore.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import tocraft.craftedcore.CraftedCore;
import tocraft.craftedcore.network.ModernNetworking;
import tocraft.craftedcore.platform.PlatformData;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a SimpleJsonResourceReloadListener that synchronizes with every client and therefore should run serverside
 */
@SuppressWarnings("unused")
public abstract class SynchronizedJsonReloadListener extends SimpleJsonResourceReloadListener {
    protected final ResourceLocation RELOAD_SYNC;
    protected final String directory;
    protected final Gson gson;
    private final Map<ResourceLocation, JsonElement> map = new HashMap<>();

    public SynchronizedJsonReloadListener(Gson gson, String directory) {
        super(gson, directory);
        this.gson = gson;
        this.directory = directory;
        this.RELOAD_SYNC = CraftedCore.id("data_sync_" + directory);
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
        this.map.clear();
        if (compound != null) {
            for (String key : compound.getAllKeys()) {
                this.map.put(new ResourceLocation(key), JsonParser.parseString(compound.getString(key)));
            }
        }
        if (PlatformData.getEnv() == EnvType.CLIENT) {
            this.onApply(map);
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerPacketReceiver() {
        ModernNetworking.registerReceiver(ModernNetworking.Side.S2C, RELOAD_SYNC, this::onPacketReceive);
    }
}
