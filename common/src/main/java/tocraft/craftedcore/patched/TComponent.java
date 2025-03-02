package tocraft.craftedcore.patched;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TComponent {
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull MutableComponent literal(String text) {
        return Component.literal(text);
    }

    @Contract("_, _ -> new")
    public static @NotNull MutableComponent translatable(String text, Object... objects) {
        return Component.translatable(text, objects);
    }
}
