package tocraft.craftedcore.data;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import tocraft.craftedcore.CraftedCore;

import java.util.HashMap;
import java.util.Map;

/**
 * This is a SimpleJsonResourceReloadListener that synchronizes with every client and therefore should run serverside
 */
@SuppressWarnings("unused")
public abstract class SynchronizedJsonReloadListener extends SimpleJsonResourceReloadListener {
    protected final ResourceLocation RELOAD_SYNC;
    private final CustomPacketPayload.Type<PacketPayload> PACKET_TYPE;
    private final StreamCodec<RegistryFriendlyByteBuf, PacketPayload> PACKET_CODEC;

    protected final String directory;
    protected final Gson gson;
    private final Map<ResourceLocation, JsonElement> map = new HashMap<>();

    public SynchronizedJsonReloadListener(Gson gson, String directory) {
        super(gson, directory);
        this.gson = gson;
        this.directory = directory;
        this.RELOAD_SYNC = CraftedCore.id("data_sync_" + directory);
        this.PACKET_TYPE = new CustomPacketPayload.Type<>(RELOAD_SYNC);
        this.PACKET_CODEC = CustomPacketPayload.codec(PacketPayload::write, PacketPayload::new);
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
        NetworkManager.sendToPlayer(player, new PacketPayload(compound));
    }

    @Environment(EnvType.CLIENT)
    private void onPacketReceive(PacketPayload packet, NetworkManager.PacketContext context) {
        this.map.clear();
        CompoundTag compound = packet.nbt();
        if (compound != null) {
            for (String key : compound.getAllKeys()) {
                this.map.put(new ResourceLocation(key), JsonParser.parseString(compound.getString(key)));
            }
        }
        if (Platform.getEnv() == EnvType.CLIENT) {
            this.onApply(map);
        }
    }

    @Environment(EnvType.CLIENT)
    public void registerPacketReceiver() {
        NetworkManager.registerReceiver(NetworkManager.Side.S2C, PACKET_TYPE, PACKET_CODEC, this::onPacketReceive);
    }

    // this is a class since records are static and it requires the PACKET_TYPE
    private final class PacketPayload implements CustomPacketPayload {
        private final CompoundTag nbt;
        public PacketPayload(CompoundTag nbt) {
            this.nbt = nbt;
        }
        public PacketPayload(RegistryFriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        public CompoundTag nbt() {
            return nbt;
        }

        public void write(RegistryFriendlyByteBuf buf) {
            buf.writeNbt(nbt);
        }

        @Override
        public @NotNull Type<? extends CustomPacketPayload> type() {
            return PACKET_TYPE;
        }
    }
}
