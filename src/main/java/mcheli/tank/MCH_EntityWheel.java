/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockWall
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.init.Blocks
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.BlockPos$PooledMutableBlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraft.world.border.WorldBorder
 */
package mcheli.tank;

import java.util.ArrayList;
import java.util.List;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntityHitBox;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Lib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;

public class MCH_EntityWheel
extends W_Entity {
    private MCH_EntityAircraft parents;
    public Vec3d pos;
    boolean isPlus;

    public MCH_EntityWheel(World w) {
        super(w);
        this.func_70105_a(1.0f, 1.0f);
        this.field_70138_W = 1.5f;
        this.field_70178_ae = true;
        this.isPlus = false;
    }

    public void setWheelPos(Vec3d pos, Vec3d weightedCenter) {
        this.pos = pos;
        this.isPlus = pos.field_72449_c >= weightedCenter.field_72449_c;
    }

    public void travelToDimension(int dimensionId) {
    }

    public MCH_EntityAircraft getParents() {
        return this.parents;
    }

    public void setParents(MCH_EntityAircraft parents) {
        this.parents = parents;
    }

    protected void func_70037_a(NBTTagCompound compound) {
        this.func_70106_y();
    }

    protected void func_70014_b(NBTTagCompound compound) {
    }

    public void func_70091_d(MoverType type, double x, double y, double z) {
        BlockPos blockpos1;
        IBlockState iblockstate1;
        Block block1;
        boolean flag;
        this.field_70170_p.field_72984_F.func_76320_a("move");
        double d2 = x;
        double d3 = y;
        double d4 = z;
        List<AxisAlignedBB> list1 = this.getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(x, y, z));
        AxisAlignedBB axisalignedbb = this.func_174813_aQ();
        if (y != 0.0) {
            for (int k = 0; k < list1.size(); ++k) {
                y = list1.get(k).func_72323_b(this.func_174813_aQ(), y);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
        }
        boolean bl = flag = this.field_70122_E || d3 != y && d3 < 0.0;
        if (x != 0.0) {
            for (int j5 = 0; j5 < list1.size(); ++j5) {
                x = list1.get(j5).func_72316_a(this.func_174813_aQ(), x);
            }
            if (x != 0.0) {
                this.func_174826_a(this.func_174813_aQ().func_72317_d(x, 0.0, 0.0));
            }
        }
        if (z != 0.0) {
            for (int k5 = 0; k5 < list1.size(); ++k5) {
                z = list1.get(k5).func_72322_c(this.func_174813_aQ(), z);
            }
            if (z != 0.0) {
                this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, 0.0, z));
            }
        }
        if (this.field_70138_W > 0.0f && flag && (d2 != x || d4 != z)) {
            double d14 = x;
            double d6 = y;
            double d7 = z;
            AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();
            this.func_174826_a(axisalignedbb);
            y = this.field_70138_W;
            List<AxisAlignedBB> list = this.getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(d2, y, d4));
            AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
            AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0, d4);
            double d8 = y;
            for (int j1 = 0; j1 < list.size(); ++j1) {
                d8 = list.get(j1).func_72323_b(axisalignedbb3, d8);
            }
            axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, d8, 0.0);
            double d18 = d2;
            for (int l1 = 0; l1 < list.size(); ++l1) {
                d18 = list.get(l1).func_72316_a(axisalignedbb2, d18);
            }
            axisalignedbb2 = axisalignedbb2.func_72317_d(d18, 0.0, 0.0);
            double d19 = d4;
            for (int j2 = 0; j2 < list.size(); ++j2) {
                d19 = list.get(j2).func_72322_c(axisalignedbb2, d19);
            }
            axisalignedbb2 = axisalignedbb2.func_72317_d(0.0, 0.0, d19);
            AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
            double d20 = y;
            for (int l2 = 0; l2 < list.size(); ++l2) {
                d20 = list.get(l2).func_72323_b(axisalignedbb4, d20);
            }
            axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, d20, 0.0);
            double d21 = d2;
            for (int j3 = 0; j3 < list.size(); ++j3) {
                d21 = list.get(j3).func_72316_a(axisalignedbb4, d21);
            }
            axisalignedbb4 = axisalignedbb4.func_72317_d(d21, 0.0, 0.0);
            double d22 = d4;
            for (int l3 = 0; l3 < list.size(); ++l3) {
                d22 = list.get(l3).func_72322_c(axisalignedbb4, d22);
            }
            axisalignedbb4 = axisalignedbb4.func_72317_d(0.0, 0.0, d22);
            double d23 = d18 * d18 + d19 * d19;
            double d9 = d21 * d21 + d22 * d22;
            if (d23 > d9) {
                x = d18;
                z = d19;
                y = -d8;
                this.func_174826_a(axisalignedbb2);
            } else {
                x = d21;
                z = d22;
                y = -d20;
                this.func_174826_a(axisalignedbb4);
            }
            for (int j4 = 0; j4 < list.size(); ++j4) {
                y = list.get(j4).func_72323_b(this.func_174813_aQ(), y);
            }
            this.func_174826_a(this.func_174813_aQ().func_72317_d(0.0, y, 0.0));
            if (d14 * d14 + d7 * d7 >= x * x + z * z) {
                x = d14;
                y = d6;
                z = d7;
                this.func_174826_a(axisalignedbb1);
            }
        }
        this.field_70170_p.field_72984_F.func_76319_b();
        this.field_70170_p.field_72984_F.func_76320_a("rest");
        this.func_174829_m();
        this.field_70123_F = d2 != x || d4 != z;
        this.field_70124_G = d3 != y;
        this.field_70122_E = this.field_70124_G && d3 < 0.0;
        this.field_70132_H = this.field_70123_F || this.field_70124_G;
        int j6 = MathHelper.func_76128_c((double)this.field_70165_t);
        int i1 = MathHelper.func_76128_c((double)(this.field_70163_u - (double)0.2f));
        int k6 = MathHelper.func_76128_c((double)this.field_70161_v);
        BlockPos blockpos = new BlockPos(j6, i1, k6);
        IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
        if (iblockstate.func_185904_a() == Material.field_151579_a && ((block1 = (iblockstate1 = this.field_70170_p.func_180495_p(blockpos1 = blockpos.func_177977_b())).func_177230_c()) instanceof BlockFence || block1 instanceof BlockWall || block1 instanceof BlockFenceGate)) {
            iblockstate = iblockstate1;
            blockpos = blockpos1;
        }
        this.func_184231_a(y, this.field_70122_E, iblockstate, blockpos);
        if (d2 != x) {
            this.field_70159_w = 0.0;
        }
        if (d4 != z) {
            this.field_70179_y = 0.0;
        }
        Block block = iblockstate.func_177230_c();
        if (d3 != y) {
            block.func_176216_a(this.field_70170_p, (Entity)this);
        }
        try {
            this.func_145775_I();
        }
        catch (Throwable throwable) {
            CrashReport crashreport = CrashReport.func_85055_a((Throwable)throwable, (String)"Checking entity block collision");
            CrashReportCategory crashreportcategory = crashreport.func_85058_a("Entity being checked for collision");
            this.func_85029_a(crashreportcategory);
            throw new ReportedException(crashreport);
        }
        this.field_70170_p.field_72984_F.func_76319_b();
    }

    public List<AxisAlignedBB> getCollisionBoxes(Entity entityIn, AxisAlignedBB aabb) {
        ArrayList<AxisAlignedBB> collidingBoundingBoxes = new ArrayList<AxisAlignedBB>();
        this.getCollisionBoxes(entityIn, aabb, collidingBoundingBoxes);
        double d0 = 0.25;
        List list = entityIn.field_70170_p.func_72839_b(entityIn, aabb.func_72314_b(d0, d0, d0));
        for (int j2 = 0; j2 < list.size(); ++j2) {
            Entity entity = (Entity)list.get(j2);
            if (W_Lib.isEntityLivingBase(entity) || entity instanceof MCH_EntitySeat || entity instanceof MCH_EntityHitBox || entity == this.parents) continue;
            AxisAlignedBB axisalignedbb1 = entity.func_70046_E();
            if (axisalignedbb1 != null && axisalignedbb1.func_72326_a(aabb)) {
                collidingBoundingBoxes.add(axisalignedbb1);
            }
            if ((axisalignedbb1 = entityIn.func_70114_g(entity)) == null || !axisalignedbb1.func_72326_a(aabb)) continue;
            collidingBoundingBoxes.add(axisalignedbb1);
        }
        return collidingBoundingBoxes;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private boolean getCollisionBoxes(Entity entityIn, AxisAlignedBB aabb, List<AxisAlignedBB> outList) {
        int i = MathHelper.func_76128_c((double)aabb.field_72340_a) - 1;
        int j = MathHelper.func_76143_f((double)aabb.field_72336_d) + 1;
        int k = MathHelper.func_76128_c((double)aabb.field_72338_b) - 1;
        int l = MathHelper.func_76143_f((double)aabb.field_72337_e) + 1;
        int i1 = MathHelper.func_76128_c((double)aabb.field_72339_c) - 1;
        int j1 = MathHelper.func_76143_f((double)aabb.field_72334_f) + 1;
        WorldBorder worldborder = entityIn.field_70170_p.func_175723_af();
        boolean flag = entityIn != null && entityIn.func_174832_aS();
        boolean flag1 = entityIn != null && entityIn.field_70170_p.func_191503_g(entityIn);
        IBlockState iblockstate = Blocks.field_150348_b.func_176223_P();
        BlockPos.PooledMutableBlockPos blockpos = BlockPos.PooledMutableBlockPos.func_185346_s();
        try {
            for (int k1 = i; k1 < j; ++k1) {
                for (int l1 = i1; l1 < j1; ++l1) {
                    boolean flag3;
                    boolean flag2 = k1 == i || k1 == j - 1;
                    boolean bl = flag3 = l1 == i1 || l1 == j1 - 1;
                    if (flag2 && flag3 || !entityIn.field_70170_p.func_175667_e((BlockPos)blockpos.func_181079_c(k1, 64, l1))) continue;
                    for (int i2 = k; i2 < l; ++i2) {
                        if ((flag2 || flag3) && i2 == l - 1) continue;
                        if (entityIn != null && flag == flag1) {
                            entityIn.func_174821_h(!flag1);
                        }
                        blockpos.func_181079_c(k1, i2, l1);
                        IBlockState iblockstate1 = !worldborder.func_177746_a((BlockPos)blockpos) && flag1 ? iblockstate : entityIn.field_70170_p.func_180495_p((BlockPos)blockpos);
                        iblockstate1.func_185908_a(entityIn.field_70170_p, (BlockPos)blockpos, aabb, outList, entityIn, false);
                    }
                }
            }
        }
        finally {
            blockpos.func_185344_t();
        }
        return !outList.isEmpty();
    }
}

