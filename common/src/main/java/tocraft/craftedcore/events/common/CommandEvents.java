package tocraft.craftedcore.events.common;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import tocraft.craftedcore.events.Event;
import tocraft.craftedcore.events.EventBuilder;

public interface CommandEvents {
	
	Event<CommandRegistration> REGISTRATION = EventBuilder.createLoop();
    
	
	interface CommandRegistration {
		
	    void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection);
    }
}
