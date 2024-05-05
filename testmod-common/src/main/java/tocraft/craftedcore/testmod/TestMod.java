package tocraft.craftedcore.testmod;

import com.mojang.logging.LogUtils;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.slf4j.Logger;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.common.PlayerEvents;

import java.util.Objects;

@SuppressWarnings("unused")
public class TestMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "testmod";

    public static void initialize() {
        PlayerEvents.PLAYER_JOIN.register(player -> LOGGER.info("player " + (player != null ? Objects.requireNonNull(player.getDisplayName()).getString() : "") + " joined."));
        PlayerEvents.PLAYER_QUIT.register(player -> LOGGER.info("player " + (player != null ? Objects.requireNonNull(player.getDisplayName()).getString() : "") + " quit."));
        PlayerEvents.AWARD_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.info((player != null ? player.getDisplayName() : "") + " unlocked advancement " + criterionKey));
        PlayerEvents.REVOKE_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.info((player != null ? player.getDisplayName() : "") + " revoked advancement " + criterionKey));

        if (Platform.getEnv() == EnvType.CLIENT) {
            TestModClient.initialize();
        }
    }

    @Environment(EnvType.CLIENT)
    static class TestModClient {
        public static void initialize() {
            ClientPlayerEvents.CLIENT_PLAYER_JOIN.register(player -> LOGGER.info("client player " + (player != null ? Objects.requireNonNull(player.getDisplayName()).getString() : "") + " joined."));
            ClientPlayerEvents.CLIENT_PLAYER_QUIT.register(player -> LOGGER.info("client player " + (player != null ? Objects.requireNonNull(player.getDisplayName()).getString() : "") + " quit."));
            ClientPlayerEvents.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> LOGGER.info((newPlayer != null ? Objects.requireNonNull(newPlayer.getDisplayName()).getString() : "") + " respawned and was previously named " + (oldPlayer != null ? Objects.requireNonNull(oldPlayer.getDisplayName()).getString() : "")));
        }
    }
}
