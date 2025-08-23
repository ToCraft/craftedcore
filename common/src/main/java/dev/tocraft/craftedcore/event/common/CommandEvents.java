package dev.tocraft.craftedcore.event.common;

import com.mojang.brigadier.CommandDispatcher;
import dev.tocraft.craftedcore.event.Event;
import dev.tocraft.craftedcore.event.EventFactory;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

@SuppressWarnings("unused")
public final class CommandEvents {
    public static final Event<CommandRegistration> REGISTRATION = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface CommandRegistration {
        void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection);
    }
}
