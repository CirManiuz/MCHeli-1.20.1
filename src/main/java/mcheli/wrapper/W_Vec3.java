/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.wrapper;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class W_Vec3 {
    public static Vec3d rotateRoll(float par1, Vec3d vOut) {
        float f1 = MathHelper.func_76134_b((float)par1);
        float f2 = MathHelper.func_76126_a((float)par1);
        double d0 = vOut.field_72450_a * (double)f1 + vOut.field_72448_b * (double)f2;
        double d1 = vOut.field_72448_b * (double)f1 - vOut.field_72450_a * (double)f2;
        double d2 = vOut.field_72449_c;
        return new Vec3d(d0, d1, d2);
    }
}

