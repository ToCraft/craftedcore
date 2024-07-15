package tocraft.craftedcore.event.common;

import com.mojang.brigadier.CommandDispatcher;
//#if MC>1182
import net.minecraft.commands.CommandBuildContext;
//#endif
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import tocraft.craftedcore.event.Event;
import tocraft.craftedcore.event.EventFactory;

@SuppressWarnings("unused")
public final class CommandEvents {
    public static final Event<CommandRegistration> REGISTRATION = EventFactory.createWithVoid();

    @FunctionalInterface
    public interface CommandRegistration {
        //#if MC>1182
        void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection);
        //#else
        //$$ void register(CommandDispatcher<CommandSourceStack> dispatcher, Commands.CommandSelection selection);
        //#endif
    }
}
