package tocraft.craftedcore.patched;

import net.minecraft.network.chat.MutableComponent;
//#if MC>1182
//$$ import net.minecraft.network.chat.Component;
//#else
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
//#endif

public class TComponent {
    public static MutableComponent literal(String text) {
        //#if MC>1182
        //$$ return Component.literal(text);
        //#else
        return new TextComponent(text);
        //#endif
    }

    public static MutableComponent translatable(String text, Object... objects) {
        //#if MC>1182
        //$$ return Component.translatable(text, objects);
        //#else
        return new TranslatableComponent(text, objects);
        //#endif
    }
}
