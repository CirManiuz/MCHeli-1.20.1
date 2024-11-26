/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.entity.MoverType
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.tank;

import java.util.List;
import java.util.Random;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.tank.MCH_EntityWheel;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WheelManager {
    public final MCH_EntityAircraft parent;
    public MCH_EntityWheel[] wheels;
    private double minZ;
    private double maxZ;
    private double avgZ;
    public Vec3d weightedCenter;
    public float targetPitch;
    public float targetRoll;
    public float prevYaw;
    private static Random rand = new Random();

    public MCH_WheelManager(MCH_EntityAircraft ac) {
        this.parent = ac;
        this.wheels = new MCH_EntityWheel[0];
        this.weightedCenter = Vec3d.field_186680_a;
    }

    public void createWheels(World w, List<MCH_AircraftInfo.Wheel> list, Vec3d weightedCenter) {
        this.wheels = new MCH_EntityWheel[list.size() * 2];
        this.minZ = 999999.0;
        this.maxZ = -999999.0;
        this.weightedCenter = weightedCenter;
        for (int i = 0; i < this.wheels.length; ++i) {
            MCH_EntityWheel wheel = new MCH_EntityWheel(w);
            wheel.setParents(this.parent);
            Vec3d wp = list.get((int)(i / 2)).pos;
            wheel.setWheelPos(new Vec3d(i % 2 == 0 ? wp.field_72450_a : -wp.field_72450_a, wp.field_72448_b, wp.field_72449_c), this.weightedCenter);
            Vec3d v = this.parent.getTransformedPosition(wheel.pos.field_72450_a, wheel.pos.field_72448_b, wheel.pos.field_72449_c);
            wheel.func_70012_b(v.field_72450_a, v.field_72448_b + 1.0, v.field_72449_c, 0.0f, 0.0f);
            this.wheels[i] = wheel;
            if (wheel.pos.field_72449_c <= this.minZ) {
                this.minZ = wheel.pos.field_72449_c;
            }
            if (!(wheel.pos.field_72449_c >= this.maxZ)) continue;
            this.maxZ = wheel.pos.field_72449_c;
        }
        this.avgZ = this.maxZ - this.minZ;
    }

    public void move(double x, double y, double z) {
        MCH_EntityWheel w2;
        int i;
        boolean showLog;
        MCH_EntityAircraft ac = this.parent;
        if (ac.getAcInfo() == null) {
            return;
        }
        boolean bl = showLog = ac.field_70173_aa % 1 == 1;
        if (showLog) {
            MCH_Lib.DbgLog(ac.field_70170_p, "[" + (ac.field_70170_p.field_72995_K ? "Client" : "Server") + "] ==============================", new Object[0]);
        }
        for (MCH_EntityWheel wheel : this.wheels) {
            wheel.field_70169_q = wheel.field_70165_t;
            wheel.field_70167_r = wheel.field_70163_u;
            wheel.field_70166_s = wheel.field_70161_v;
            Vec3d v = ac.getTransformedPosition(wheel.pos.field_72450_a, wheel.pos.field_72448_b, wheel.pos.field_72449_c);
            wheel.field_70159_w = v.field_72450_a - wheel.field_70165_t + x;
            wheel.field_70181_x = v.field_72448_b - wheel.field_70163_u;
            wheel.field_70179_y = v.field_72449_c - wheel.field_70161_v + z;
        }
        for (MCH_EntityWheel wheel : this.wheels) {
            wheel.field_70181_x *= 0.15;
            wheel.func_70091_d(MoverType.SELF, wheel.field_70159_w, wheel.field_70181_x, wheel.field_70179_y);
            double f = 1.0;
            wheel.func_70091_d(MoverType.SELF, 0.0, -0.1 * f, 0.0);
        }
        int zmog = -1;
        for (i = 0; i < this.wheels.length / 2; ++i) {
            zmog = i;
            MCH_EntityWheel w1 = this.wheels[i * 2 + 0];
            w2 = this.wheels[i * 2 + 1];
            if (w1.isPlus || !w1.field_70122_E && !w2.field_70122_E) continue;
            zmog = -1;
            break;
        }
        if (zmog >= 0) {
            this.wheels[zmog * 2 + 0].field_70122_E = true;
            this.wheels[zmog * 2 + 1].field_70122_E = true;
        }
        zmog = -1;
        for (i = this.wheels.length / 2 - 1; i >= 0; --i) {
            zmog = i;
            MCH_EntityWheel w1 = this.wheels[i * 2 + 0];
            w2 = this.wheels[i * 2 + 1];
            if (!w1.isPlus || !w1.field_70122_E && !w2.field_70122_E) continue;
            zmog = -1;
            break;
        }
        if (zmog >= 0) {
            this.wheels[zmog * 2 + 0].field_70122_E = true;
            this.wheels[zmog * 2 + 1].field_70122_E = true;
        }
        Vec3d rv = Vec3d.field_186680_a;
        Vec3d wc = ac.getTransformedPosition(this.weightedCenter);
        wc = new Vec3d(wc.field_72450_a - ac.field_70165_t, this.weightedCenter.field_72448_b, wc.field_72449_c - ac.field_70161_v);
        for (int i2 = 0; i2 < this.wheels.length / 2; ++i2) {
            MCH_EntityWheel w1 = this.wheels[i2 * 2 + 0];
            MCH_EntityWheel w22 = this.wheels[i2 * 2 + 1];
            Vec3d v1 = new Vec3d(w1.field_70165_t - (ac.field_70165_t + wc.field_72450_a), w1.field_70163_u - (ac.field_70163_u + wc.field_72448_b), w1.field_70161_v - (ac.field_70161_v + wc.field_72449_c));
            Vec3d v2 = new Vec3d(w22.field_70165_t - (ac.field_70165_t + wc.field_72450_a), w22.field_70163_u - (ac.field_70163_u + wc.field_72448_b), w22.field_70161_v - (ac.field_70161_v + wc.field_72449_c));
            Vec3d v = w1.pos.field_72449_c >= 0.0 ? v2.func_72431_c(v1) : v1.func_72431_c(v2);
            v = v.func_72432_b();
            double f = Math.abs(w1.pos.field_72449_c / this.avgZ);
            if (!w1.field_70122_E && !w22.field_70122_E) {
                f = 0.0;
            }
            rv = rv.func_72441_c(v.field_72450_a * f, v.field_72448_b * f, v.field_72449_c * f);
            if (!showLog) continue;
            v = v.func_178785_b((float)((double)ac.getRotYaw() * Math.PI / 180.0));
            MCH_Lib.DbgLog(ac.field_70170_p, "%2d : %.2f :[%+.1f, %+.1f, %+.1f][%s %d %d][%+.2f(%+.2f), %+.2f(%+.2f)][%+.1f, %+.1f, %+.1f]", i2, f, v.field_72450_a, v.field_72448_b, v.field_72449_c, w1.isPlus ? "+" : "-", w1.field_70122_E ? 1 : 0, w22.field_70122_E ? 1 : 0, w1.field_70163_u - w1.field_70167_r, w1.field_70181_x, w22.field_70163_u - w22.field_70167_r, w22.field_70181_x, v.field_72450_a, v.field_72448_b, v.field_72449_c);
        }
        rv = rv.func_72432_b();
        if (rv.field_72448_b > 0.01 && rv.field_72448_b < 0.7) {
            ac.field_70159_w += rv.field_72450_a / 50.0;
            ac.field_70179_y += rv.field_72449_c / 50.0;
        }
        rv = rv.func_178785_b((float)((double)ac.getRotYaw() * Math.PI / 180.0));
        float pitch = (float)(90.0 - Math.atan2(rv.field_72448_b, rv.field_72449_c) * 180.0 / Math.PI);
        float roll = -((float)(90.0 - Math.atan2(rv.field_72448_b, rv.field_72450_a) * 180.0 / Math.PI));
        float ogpf = ac.getAcInfo().onGroundPitchFactor;
        if (pitch - ac.getRotPitch() > ogpf) {
            pitch = ac.getRotPitch() + ogpf;
        }
        if (pitch - ac.getRotPitch() < -ogpf) {
            pitch = ac.getRotPitch() - ogpf;
        }
        float ogrf = ac.getAcInfo().onGroundRollFactor;
        if (roll - ac.getRotRoll() > ogrf) {
            roll = ac.getRotRoll() + ogrf;
        }
        if (roll - ac.getRotRoll() < -ogrf) {
            roll = ac.getRotRoll() - ogrf;
        }
        this.targetPitch = pitch;
        this.targetRoll = roll;
        if (!W_Lib.isClientPlayer(ac.getRiddenByEntity())) {
            ac.setRotPitch(pitch);
            ac.setRotRoll(roll);
        }
        if (showLog) {
            MCH_Lib.DbgLog(ac.field_70170_p, "%+03d, %+03d :[%.2f, %.2f, %.2f] yaw=%.2f, pitch=%.2f, roll=%.2f", (int)pitch, (int)roll, rv.field_72450_a, rv.field_72448_b, rv.field_72449_c, Float.valueOf(ac.getRotYaw()), Float.valueOf(this.targetPitch), Float.valueOf(this.targetRoll));
        }
        for (MCH_EntityWheel wheel : this.wheels) {
            Vec3d v = this.getTransformedPosition(wheel.pos.field_72450_a, wheel.pos.field_72448_b, wheel.pos.field_72449_c, ac, ac.getRotYaw(), this.targetPitch, this.targetRoll);
            double rangeH = 2.0;
            double poy = wheel.field_70138_W / 2.0f;
            if (wheel.field_70165_t > v.field_72450_a + rangeH) {
                wheel.field_70165_t = v.field_72450_a + rangeH;
                wheel.field_70163_u = v.field_72448_b + poy;
            }
            if (wheel.field_70165_t < v.field_72450_a - rangeH) {
                wheel.field_70165_t = v.field_72450_a - rangeH;
                wheel.field_70163_u = v.field_72448_b + poy;
            }
            if (wheel.field_70161_v > v.field_72449_c + rangeH) {
                wheel.field_70161_v = v.field_72449_c + rangeH;
                wheel.field_70163_u = v.field_72448_b + poy;
            }
            if (wheel.field_70161_v < v.field_72449_c - rangeH) {
                wheel.field_70161_v = v.field_72449_c - rangeH;
                wheel.field_70163_u = v.field_72448_b + poy;
            }
            wheel.func_70080_a(wheel.field_70165_t, wheel.field_70163_u, wheel.field_70161_v, 0.0f, 0.0f);
        }
    }

    public Vec3d getTransformedPosition(double x, double y, double z, MCH_EntityAircraft ac, float yaw, float pitch, float roll) {
        Vec3d v = MCH_Lib.RotVec3(x, y, z, -yaw, -pitch, -roll);
        return v.func_72441_c(ac.field_70165_t, ac.field_70163_u, ac.field_70161_v);
    }

    public void updateBlock() {
        if (!MCH_Config.Collision_DestroyBlock.prmBool) {
            return;
        }
        MCH_EntityAircraft ac = this.parent;
        for (MCH_EntityWheel w : this.wheels) {
            Vec3d v = ac.getTransformedPosition(w.pos);
            int x = (int)(v.field_72450_a + 0.5);
            int y = (int)(v.field_72448_b + 0.5);
            int z = (int)(v.field_72449_c + 0.5);
            BlockPos blockpos = new BlockPos(x, y, z);
            IBlockState iblockstate = ac.field_70170_p.func_180495_p(blockpos);
            if (iblockstate.func_177230_c() == W_Block.getSnowLayer()) {
                ac.field_70170_p.func_175698_g(blockpos);
            }
            if (iblockstate.func_177230_c() != W_Blocks.field_150392_bi && iblockstate.func_177230_c() != W_Blocks.field_150414_aQ) continue;
            W_WorldFunc.destroyBlock(ac.field_70170_p, x, y, z, false);
        }
    }

    public void particleLandingGear() {
        if (this.wheels.length <= 0) {
            return;
        }
        MCH_EntityAircraft ac = this.parent;
        double d = ac.field_70159_w * ac.field_70159_w + ac.field_70179_y * ac.field_70179_y + (double)Math.abs(this.prevYaw - ac.getRotYaw());
        this.prevYaw = ac.getRotYaw();
        if (d > 0.001) {
            for (int i = 0; i < 2; ++i) {
                int z;
                int y;
                MCH_EntityWheel w = this.wheels[rand.nextInt(this.wheels.length)];
                Vec3d v = ac.getTransformedPosition(w.pos);
                int x = MathHelper.func_76128_c((double)(v.field_72450_a + 0.5));
                BlockPos blockpos = new BlockPos(x, y = MathHelper.func_76128_c((double)(v.field_72448_b - 0.5)), z = MathHelper.func_76128_c((double)(v.field_72449_c + 0.5)));
                IBlockState iblockstate = ac.field_70170_p.func_180495_p(blockpos);
                if (Block.func_149680_a((Block)iblockstate.func_177230_c(), (Block)Blocks.field_150350_a)) {
                    y = MathHelper.func_76128_c((double)(v.field_72448_b + 0.5));
                    blockpos = new BlockPos(x, y, z);
                    iblockstate = ac.field_70170_p.func_180495_p(blockpos);
                }
                if (Block.func_149680_a((Block)iblockstate.func_177230_c(), (Block)Blocks.field_150350_a)) continue;
                MCH_ParticlesUtil.spawnParticleTileCrack(ac.field_70170_p, x, y, z, v.field_72450_a + ((double)rand.nextFloat() - 0.5), v.field_72448_b + 0.1, v.field_72449_c + ((double)rand.nextFloat() - 0.5), -ac.field_70159_w * 4.0 + ((double)rand.nextFloat() - 0.5) * 0.1, (double)rand.nextFloat() * 0.5, -ac.field_70179_y * 4.0 + ((double)rand.nextFloat() - 0.5) * 0.1);
            }
        }
    }
}

