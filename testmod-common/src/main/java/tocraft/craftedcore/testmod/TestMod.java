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
        PlayerEvents.PLAYER_JOIN.register(player -> LOGGER.debug("player {} joined.", player != null ? player.getName().getString() : ""));
        PlayerEvents.PLAYER_QUIT.register(player -> LOGGER.debug("player {} quit.", player != null ? player.getName().getString() : ""));
        PlayerEvents.AWARD_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.debug("{} unlocked advancement {}", player != null ? player.getDisplayName() : "", criterionKey));
        PlayerEvents.REVOKE_ADVANCEMENT.register((player, advancement, criterionKey) -> LOGGER.debug("{} revoked advancement {}", player != null ? player.getDisplayName() : "", criterionKey));
        EntityEvents.INTERACT_WITH_PLAYER.register((player, entity, hand) -> {
            LOGGER.debug("player {}just interacted with {}", player != null ? player.getName().getString() : "", entity.getName().getString());
            return InteractionResult.PASS;
        });

        EntityEvents.LIVING_DEATH.register((entity, source) -> {
            LOGGER.debug("{}Oh, I just died in your arms tonight.", entity != null ? entity.getName().getString() : "");
            return InteractionResult.PASS;
        });

        EntityEvents.LIVING_BREATHE.register((entity, canBreathe) -> {
            if (entity instanceof Player) {
                if (canBreathe) {
                    LOGGER.debug("In and out.");
                } else {
                    LOGGER.debug("I need air!");
                }
                // revert value, the players will need to breathe underwater now
                return !canBreathe;
            } else {
                LOGGER.debug("something is breathing here...");
                return canBreathe;
            }
        });

        if (PlatformData.getEnv() == EnvType.CLIENT) {
            TestModClient.initialize();
        }
    }

    @Environment(EnvType.CLIENT)
    static class TestModClient {
        public static void initialize() {
            ClientPlayerEvents.CLIENT_PLAYER_JOIN.register(player -> LOGGER.debug("client player {} joined.", player != null ? player.getName().getString() : ""));
            ClientPlayerEvents.CLIENT_PLAYER_QUIT.register(player -> LOGGER.debug("client player {} quit.", player != null ? player.getName().getString() : ""));
            ClientPlayerEvents.CLIENT_PLAYER_RESPAWN.register((oldPlayer, newPlayer) -> LOGGER.debug("{} respawned and was previously named {}", newPlayer != null ? Objects.requireNonNull(newPlayer.getDisplayName()).getString() : "", oldPlayer != null ? Objects.requireNonNull(oldPlayer.getDisplayName()).getString() : ""));

            RenderEvents.RENDER_MOUNT_HEALTH.register((player, graphics) -> InteractionResult.FAIL);

            RenderEvents.HUD_RENDERING.register((graphics, tickDelta) -> LOGGER.debug("Rendering HUD..."));

            ClientTickEvents.CLIENT_PRE.register(mc -> LOGGER.debug("TICK"));
            ClientTickEvents.CLIENT_POST.register(mc -> LOGGER.debug("TOCK"));
        }
    }
}
