package tocraft.craftedcore.patched;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class CCommandSourceStack {
    public static void sendSuccess(@NotNull CommandSourceStack source, Component text, boolean bl) {
        source.sendSuccess(() -> text, bl);
    }
}
