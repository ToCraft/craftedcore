package tocraft.craftedcore.testmod;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
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

        EntityEvents.LIVING_BREATHE.register((entity, canBreathe) -> {
            if (entity instanceof Player) {
                if (canBreathe) {
                    LOGGER.info("In and out.");
                } else {
                    LOGGER.info("I need air!");
                }
                // revert value, the players will need to breathe underwater now
                return !canBreathe;
            } else {
                LOGGER.info("something is breathing here...");
                return !canBreathe;
            }
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

            RenderEvents.HUD_RENDERING.register((graphics, tickDelta) -> LOGGER.debug("Rendering HUD..."));

            ClientTickEvents.CLIENT_LEVEL_PRE.register(level -> LOGGER.debug("tick"));
            ClientTickEvents.CLIENT_LEVEL_PRE.register(level -> LOGGER.debug("tock"));
            ClientTickEvents.CLIENT_PRE.register(mc -> LOGGER.debug("TICK"));
            ClientTickEvents.CLIENT_POST.register(mc -> LOGGER.debug("TOCK"));
        }
    }
}
