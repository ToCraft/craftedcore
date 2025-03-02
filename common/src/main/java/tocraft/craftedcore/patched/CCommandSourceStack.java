package tocraft.craftedcore.patched;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

@SuppressWarnings("unused")
public class CCommandSourceStack {
    public static void sendSuccess(CommandSourceStack source, Component text, boolean bl) {
        source.sendSuccess(() -> text, bl);
    }
}
