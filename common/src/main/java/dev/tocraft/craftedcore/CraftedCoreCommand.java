package dev.tocraft.craftedcore;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import dev.tocraft.craftedcore.event.common.CommandEvents;
import dev.tocraft.craftedcore.network.ModernNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.NotNull;

public class CraftedCoreCommand {

    public static void initialize() {
        CommandEvents.REGISTRATION.register((dispatcher, ctx, selection) -> register(dispatcher));
    }

    private static void register(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> craftedcore = Commands.literal(CraftedCore.MODID)
                .then(Commands.literal("clear").then(Commands.literal("cache")
                        .executes(context -> {
                            CraftedCore.clearCache();
                            for (ServerLevel level : context.getSource().getServer().getAllLevels()) {
                                ModernNetworking.sendToPlayers(level.players(), CraftedCore.CLEAR_CACHE_PACKET, new CompoundTag());
                            }
                            context.getSource().sendSuccess(() -> Component.translatable("craftedcore.command.clear_cache"), true);
                            return 1;
                        })))
                .build();
        dispatcher.getRoot().addChild(craftedcore);
    }
}
