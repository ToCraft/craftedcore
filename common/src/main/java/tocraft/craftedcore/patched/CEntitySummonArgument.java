package tocraft.craftedcore.patched;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

@SuppressWarnings("unused")
public class CEntitySummonArgument {
    public static ResourceArgument<EntityType<?>> id(CommandBuildContext ctx) {
        //noinspection unchecked
        return ResourceArgument.resource(ctx, (((Registry<EntityType<?>>) CRegistries.getRegistry(Identifier.parse("entity_type"))).key()));
    }

    public static ResourceLocation getEntityTypeId(CommandContext<CommandSourceStack> context, String string) throws CommandSyntaxException {
        return EntityType.getKey(ResourceArgument.getSummonableEntityType(context, string).value());
    }
}
