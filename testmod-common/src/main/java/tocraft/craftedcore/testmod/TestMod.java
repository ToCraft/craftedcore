package tocraft.craftedcore.testmod;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.InteractionResult;
import org.slf4j.Logger;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.RenderEvents;
import tocraft.craftedcore.event.common.EntityEvents;
import tocraft.craftedcore.event.common.PlayerEvents;
import tocraft.craftedcore.platform.PlatformData;

import java.util.Objects;

@SuppressWarnings("unused")
public class TestMod {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MODID = "testmod";

    public static void initialize() {
        PlayerEvents.PLAYER_JOIN.register(player -> LOGGER.info("player " + (player != null ? player.getName().getString() : "") + " joined."));
        PlayerEvents.PLAYER_QUIT.register(player -> LOGGER.info("player " + (player != null ? player.getName().getString() : "") + " quit."));
        PlayerEvents.AWARD_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.info((player != null ? player.getDisplayName() : "") + " unlocked advancement " + criterionKey));
        PlayerEvents.REVOKE_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.info((player != null ? player.getDisplayName() : "") + " revoked advancement " + criterionKey));
        EntityEvents.INTERACT_WITH_PLAYER.register((player, entity, hand) -> {
            LOGGER.info("player " + (player != null ? player.getName().getString() : "") + "just interacted with " + entity.getName().getString());
            return InteractionResult.PASS;
        });

        EntityEvents.LIVING_DEATH.register((entity, source) -> {
            LOGGER.info((entity != null ? entity.getName().getString() : "") + "Oh, I just died in your arms tonight.");
            return InteractionResult.PASS;
        });

        if (PlatformData.getEnv() == EnvType.CLIENT) {
            TestModClient.initialize();
        }
    }

    @Environment(EnvType.CLIENT)
    static class TestModClient {
        public static void initialize() {
            ClientPlayerEvents.CLIENT_PLAYER_JOIN.register(player -> LOGGER.info("client player " + (player != null ? player.getName().getString() : "") + " joined."));
            ClientPlayerEvents.CLIENT_PLAYER_QUIT.register(player -> LOGGER.info("client player " + (player != null ? player.getName().getString() : "") + " quit."));
            ClientPlayerEvents.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> LOGGER.info((newPlayer != null ? Objects.requireNonNull(newPlayer.getDisplayName()).getString() : "") + " respawned and was previously named " + (oldPlayer != null ? Objects.requireNonNull(oldPlayer.getDisplayName()).getString() : "")));

            RenderEvents.RENDER_MOUNT_HEALTH.register((player, graphics) -> InteractionResult.FAIL);

            RenderEvents.HUD_RENDERING.register((graphics, tickDelta) -> LOGGER.info("Rendering HUD..."));
        }
    }
}
