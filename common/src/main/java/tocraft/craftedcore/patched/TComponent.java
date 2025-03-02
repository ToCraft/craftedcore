package tocraft.craftedcore.patched;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class TComponent {
    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translatable(String text, Object... objects) {
        return Component.translatable(text, objects);
    }
}
