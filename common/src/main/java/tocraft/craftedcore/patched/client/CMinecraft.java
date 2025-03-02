package tocraft.craftedcore.patched.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CMinecraft {
    public static @NotNull UUID getLocalPlayerUUID() {
        return Minecraft.getInstance().getUser().getProfileId();
    }

    public static boolean isLocalPlayer(UUID playerId) {
        return playerId == getLocalPlayerUUID();
    }
}
