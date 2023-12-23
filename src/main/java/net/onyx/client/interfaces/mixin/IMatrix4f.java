package net.onyx.client.interfaces.mixin;

import net.minecraft.util.math.Vec3d;
import net.onyx.client.misc.maths.Vec4;
import net.onyx.client.utils.Vec4d;

public interface IMatrix4f {
    void multiplyMatrix(Vec4 vec4, Vec4 mmmat4);
    Vec3d mul(Vec3d vec);

    Vec4d multiply(Vec4d v);

    Vec3d multiply(Vec3d v);
}
