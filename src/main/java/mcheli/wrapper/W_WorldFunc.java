/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.wrapper;

import javax.annotation.Nullable;
import mcheli.__helper.MCH_SoundEvents;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_MOD;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class W_WorldFunc {
    public static void DEF_playSoundEffect(World w, double x, double y, double z, String name, float volume, float pitch) {
        MCH_SoundEvents.playSound(w, x, y, z, name, volume, pitch);
    }

    public static void MOD_playSoundEffect(World w, double x, double y, double z, String name, float volume, float pitch) {
        W_WorldFunc.DEF_playSoundEffect(w, x, y, z, W_MOD.DOMAIN + ":" + name, volume, pitch);
    }

    private static void playSoundAtEntity(Entity e, String name, float volume, float pitch) {
        e.func_184185_a(MCH_SoundEvents.getSound(name), volume, pitch);
    }

    public static void MOD_playSoundAtEntity(Entity e, String name, float volume, float pitch) {
        W_WorldFunc.playSoundAtEntity(e, W_MOD.DOMAIN + ":" + name, volume, pitch);
    }

    public static int getBlockId(World w, int x, int y, int z) {
        return Block.func_149682_b((Block)W_WorldFunc.getBlock(w, x, y, z));
    }

    public static Block getBlock(World w, int x, int y, int z) {
        return W_WorldFunc.getBlock(w, new BlockPos(x, y, z));
    }

    public static Block getBlock(World w, BlockPos blockpos) {
        return w.func_180495_p(blockpos).func_177230_c();
    }

    public static Material getBlockMaterial(World w, int x, int y, int z) {
        return w.func_180495_p(new BlockPos(x, y, z)).func_185904_a();
    }

    public static boolean isBlockWater(World w, int x, int y, int z) {
        return W_WorldFunc.isEqualBlock(w, x, y, z, W_Block.getWater());
    }

    public static boolean isEqualBlock(World w, int x, int y, int z, Block block) {
        return Block.func_149680_a((Block)W_WorldFunc.getBlock(w, x, y, z), (Block)block);
    }

    @Nullable
    public static RayTraceResult clip(World w, Vec3d par1Vec3, Vec3d par2Vec3) {
        return w.func_72933_a(par1Vec3, par2Vec3);
    }

    @Nullable
    public static RayTraceResult clip(World w, Vec3d par1Vec3, Vec3d par2Vec3, boolean b) {
        return w.func_72901_a(par1Vec3, par2Vec3, b);
    }

    @Nullable
    public static RayTraceResult clip(World w, Vec3d par1Vec3, Vec3d par2Vec3, boolean b1, boolean b2, boolean b3) {
        return w.func_147447_a(par1Vec3, par2Vec3, b1, b2, b3);
    }

    public static boolean setBlock(World w, int a, int b, int c, Block d) {
        return W_WorldFunc.setBlock(w, new BlockPos(a, b, c), d);
    }

    public static boolean setBlock(World w, BlockPos blockpos, Block d) {
        return w.func_175656_a(blockpos, d.func_176223_P());
    }

    public static void setBlock(World w, int x, int y, int z, IBlockState blockstate, int j) {
        w.func_180501_a(new BlockPos(x, y, x), blockstate, j);
    }

    public static boolean destroyBlock(World w, int x, int y, int z, boolean par4) {
        return W_WorldFunc.destroyBlock(w, new BlockPos(x, y, z), par4);
    }

    public static boolean destroyBlock(World w, BlockPos blockpos, boolean dropBlock) {
        return w.func_175655_b(blockpos, dropBlock);
    }

    public static Vec3d getWorldVec3(World w, double x, double y, double z) {
        return new Vec3d(x, y, z);
    }

    public static Vec3d getWorldVec3EntityPos(Entity e) {
        return W_WorldFunc.getWorldVec3(e.field_70170_p, e.field_70165_t, e.field_70163_u, e.field_70161_v);
    }
}
