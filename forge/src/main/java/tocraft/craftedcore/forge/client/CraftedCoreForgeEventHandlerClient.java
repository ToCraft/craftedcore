package tocraft.craftedcore.forge.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
//#if MC>1194
//$$ import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
//#elseif MC>1182
//$$ import net.minecraftforge.client.event.RenderGuiEvent;
//#else
import net.minecraftforge.client.event.RenderGameOverlayEvent;
//#endif
//#if MC>1182
//$$ import net.minecraftforge.client.event.RenderGuiOverlayEvent;
//#else
import net.minecraftforge.client.gui.OverlayRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import tocraft.craftedcore.event.client.ClientPlayerEvents;
import tocraft.craftedcore.event.client.ClientTickEvents;
import tocraft.craftedcore.event.client.RenderEvents;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class CraftedCoreForgeEventHandlerClient {
    @SubscribeEvent
    //#if MC>1194
    //$$ public void event(CustomizeGuiOverlayEvent event) {
    //$$     RenderEvents.HUD_RENDERING.invoke().render(event.getGuiGraphics(), event.getPartialTick());
    //$$ }
    //#elseif MC>1182
    //$$ public void event(RenderGuiEvent.Post event) {
    //$$     RenderEvents.HUD_RENDERING.invoke().render(event.getPoseStack(), event.getPartialTick());
    //$$ }
    //#else
    public void event(RenderGameOverlayEvent.Post event) {
        RenderEvents.HUD_RENDERING.invoke().render(event.getMatrixStack(), event.getPartialTicks());
    }
    //#endif

    @SubscribeEvent
    public void event(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            ClientTickEvents.CLIENT_PRE.invoke().tick(Minecraft.getInstance());
        } else if (event.phase == TickEvent.Phase.END) {
            ClientTickEvents.CLIENT_POST.invoke().tick(Minecraft.getInstance());
        }
    }

    @SubscribeEvent
    //#if MC>1182
    //$$ public void event(RenderGuiOverlayEvent.Pre event) {
    //#else
    public void event(RenderGameOverlayEvent.PreLayer event) {
    //#endif
        //#if MC>1194
        //$$ var graphics = event.getGuiGraphics();
        //#elseif MC>1182
        //$$ var graphics = event.getPoseStack();
        //#else
        var graphics = event.getMatrixStack();
        //#endif
        //#if MC>1182
        //$$ switch (event.getOverlay().id().getPath()) {
        //$$     case "player_health" -> {
        //#else
        OverlayRegistry.OverlayEntry overlayEntry = OverlayRegistry.getEntry(event.getOverlay());
        if (overlayEntry != null)
        switch (overlayEntry.getDisplayName()) {
            case "Player Health" -> {
        //#endif
                InteractionResult result = RenderEvents.RENDER_HEALTH.invoke().render(graphics, Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            //#if MC>1182
            //$$ case "food_level" -> {
            //#else
            case "Food Level" -> {
            //#endif
                InteractionResult result = RenderEvents.RENDER_FOOD.invoke().render(graphics, Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
            //#if MC>1182
            //$$ case "air_level" -> {
            //#else
            case "Air Level" -> {
                //#endif
                InteractionResult result = RenderEvents.RENDER_BREATH.invoke().render(graphics, Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
        //#if MC>1182
        //$$ case "vehicle_health" -> {
            //#else
            case "Mount Health" -> {
                //#endif
                InteractionResult result = RenderEvents.RENDER_MOUNT_HEALTH.invoke().render(graphics, Minecraft.getInstance().player);
                if (result == InteractionResult.FAIL) {
                    event.setCanceled(true);
                }
            }
        }
    }
    @SubscribeEvent
    //#if MC>1182
    //$$ public void event(ClientPlayerNetworkEvent.LoggingOut event) {
    //#else
    public void event(ClientPlayerNetworkEvent.LoggedOutEvent event) {
    //#endif
        ClientPlayerEvents.CLIENT_PLAYER_QUIT.invoke().quit(event.getPlayer());
    }
}
