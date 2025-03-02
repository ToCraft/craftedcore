package tocraft.craftedcore.util;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@SuppressWarnings("unused")
public class Maths {
    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector3f POSITIVE_X() {
        return new Vector3f(1, 0, 0);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector3f POSITIVE_Y() {
        return new Vector3f(0, 1, 0);
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull Vector3f POSITIVE_Z() {
        return new Vector3f(0, 0, 1);
    }

    public static Quaternionf getDegreesQuaternion(Vector3f axis, float angle) {
        return new Quaternionf().fromAxisAngleDeg(axis, angle);
    }
}
