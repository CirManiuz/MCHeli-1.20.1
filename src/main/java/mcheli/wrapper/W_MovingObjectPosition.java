/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.wrapper;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

public class W_MovingObjectPosition {
    public static boolean isHitTypeEntity(RayTraceResult m) {
        if (m == null) {
            return false;
        }
        return m.field_72313_a == RayTraceResult.Type.ENTITY;
    }

    public static boolean isHitTypeTile(RayTraceResult m) {
        if (m == null) {
            return false;
        }
        return m.field_72313_a == RayTraceResult.Type.BLOCK;
    }

    public static RayTraceResult newMOP(int p1, int p2, int p3, int p4, Vec3d p5, boolean p6) {
        return new RayTraceResult(p5, EnumFacing.func_82600_a((int)p4), new BlockPos(p1, p2, p3));
    }
}

