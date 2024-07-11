package tocraft.craftedcore.patched.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

//#if MC<=1182
import com.mojang.util.UUIDTypeAdapter;
//#endif

import java.util.UUID;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class CMinecraft {
    public static UUID getLocalPlayerUUID() {
        //#if MC>1182
        //$$ return Minecraft.getInstance().getUser().getProfileId();
        //#else
        return UUIDTypeAdapter.fromString(Minecraft.getInstance().getUser().getUuid());
        //#endif
    }

    public static boolean isLocalPlayer(UUID playerId) {
        return playerId == getLocalPlayerUUID();
    }
}
