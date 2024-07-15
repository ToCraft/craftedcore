package tocraft.craftedcore.patched;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.commands.CommandSourceStack;

//#if MC>1182
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.world.entity.EntityType;
import net.minecraft.core.Registry;
//#else
//$$ import net.minecraft.commands.arguments.EntitySummonArgument;
//$$ import org.jetbrains.annotations.Nullable;
//#endif

@SuppressWarnings("unused")
public class CEntitySummonArgument {
    //#if MC>1182
    public static ResourceArgument<EntityType<?>> id(CommandBuildContext ctx) {
        //noinspection unchecked
        return ResourceArgument.resource(ctx, (((Registry<EntityType<?>>) CRegistries.getRegistry(Identifier.parse("entity_type"))).key()));
    }
    
    public static ResourceLocation getEntityTypeId(CommandContext<CommandSourceStack> context, String string) throws CommandSyntaxException {
        return EntityType.getKey(ResourceArgument.getSummonableEntityType(context, string).value());
    }
    //#else
    //$$ public static EntitySummonArgument id(@SuppressWarnings("unused") @Nullable Object somethingNull) {
    //$$     return EntitySummonArgument.id();
    //$$ }
    //$$
    //$$ public static ResourceLocation getEntityTypeId(CommandContext<CommandSourceStack> context, String string) throws CommandSyntaxException {
    //$$     return EntitySummonArgument.getSummonableEntity(context, string);
    //$$ }
    //#endif
}
