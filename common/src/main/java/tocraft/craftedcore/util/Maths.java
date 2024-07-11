package tocraft.craftedcore.util;

//#if MC>1182
//$$ import org.joml.Quaternionf;
//$$ import org.joml.Vector3f;
//#else
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
//#endif

@SuppressWarnings("unused")
public class Maths {
    public static Vector3f POSITIVE_X() {
        return new Vector3f(1, 0, 0);
    }

    public static Vector3f POSITIVE_Y() {
        return new Vector3f(0, 1, 0);
    }

    public static Vector3f POSITIVE_Z() {
        return new Vector3f(0, 0, 1);
    }

    //#if MC>1182
    //$$ public static Quaternionf getDegreesQuaternion(Vector3f axis, float angle) {
    //$$     return new Quaternionf().fromAxisAngleDeg(axis, angle);
    //$$ }
    //#else
    public static Quaternion getDegreesQuaternion(Vector3f axis, float angle) {
        return axis.rotation(angle);
    }
    //#endif
}
