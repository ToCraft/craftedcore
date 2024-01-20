package tocraft.craftedcore.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

public class math {
    final public static Vector3f POSITIVE_X() {
        return new Vector3f(1, 0, 0);
    }

    final public static Vector3f POSITIVE_Y() {
        return new Vector3f(0, 1, 0);
    }

    final public static Vector3f POSITIVE_Z() {
        return new Vector3f(0, 0, 1);
    }

    public static Quaternion getDegreesQuaternion(Vector3f axis, float angle) {
        return axis.rotation(angle);
    }
}