/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.BlockPos
 */
package mcheli.wrapper;

import net.minecraft.util.math.BlockPos;

public class W_ChunkPosition {
    public static int getChunkPosX(BlockPos c) {
        return c.func_177958_n();
    }

    public static int getChunkPosY(BlockPos c) {
        return c.func_177956_o();
    }

    public static int getChunkPosZ(BlockPos c) {
        return c.func_177952_p();
    }
}

