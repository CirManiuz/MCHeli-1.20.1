/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockFence
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockWall
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.crash.CrashReport
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.entity.item.EntityXPOrb
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.projectile.EntityArrow
 *  net.minecraft.init.Blocks
 *  net.minecraft.item.Item
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.ReportedException
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.tank;

import java.util.List;
import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.MCH_Math;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_BoundingBox;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_EntityHitBox;
import mcheli.aircraft.MCH_EntitySeat;
import mcheli.aircraft.MCH_PacketStatusRequest;
import mcheli.aircraft.MCH_Parts;
import mcheli.chain.MCH_EntityChain;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.tank.MCH_TankInfo;
import mcheli.tank.MCH_TankInfoManager;
import mcheli.tank.MCH_WheelManager;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.weapon.MCH_WeaponSet;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockWall;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_EntityTank
extends MCH_EntityAircraft {
    private MCH_TankInfo tankInfo = null;
    public float soundVolume;
    public float soundVolumeTarget;
    public float rotationRotor;
    public float prevRotationRotor;
    public float addkeyRotValue;
    public final MCH_WheelManager WheelMng;

    public MCH_EntityTank(World world) {
        super(world);
        this.currentSpeed = 0.07;
        this.field_70156_m = true;
        this.func_70105_a(2.0f, 0.7f);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.weapons = this.createWeapon(0);
        this.soundVolume = 0.0f;
        this.field_70138_W = 0.6f;
        this.rotationRotor = 0.0f;
        this.prevRotationRotor = 0.0f;
        this.WheelMng = new MCH_WheelManager(this);
    }

    @Override
    public String getKindName() {
        return "tanks";
    }

    @Override
    public String getEntityType() {
        return "Vehicle";
    }

    @Nullable
    public MCH_TankInfo getTankInfo() {
        return this.tankInfo;
    }

    @Override
    public void changeType(String type) {
        MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityTank.changeType " + type + " : " + this.toString(), new Object[0]);
        if (!type.isEmpty()) {
            this.tankInfo = MCH_TankInfoManager.get(type);
        }
        if (this.tankInfo == null) {
            MCH_Lib.Log(this, "##### MCH_EntityTank changeTankType() Tank info null %d, %s, %s", W_Entity.getEntityId(this), type, this.getEntityName());
            this.func_70106_y();
        } else {
            this.setAcInfo(this.tankInfo);
            this.newSeats(this.getAcInfo().getNumSeatAndRack());
            this.switchFreeLookModeClient(this.getAcInfo().defaultFreelook);
            this.weapons = this.createWeapon(1 + this.getSeatNum());
            this.initPartRotation(this.getRotYaw(), this.getRotPitch());
            this.WheelMng.createWheels(this.field_70170_p, this.getAcInfo().wheels, new Vec3d(0.0, -0.35, (double)this.getTankInfo().weightedCenterZ));
        }
    }

    @Override
    @Nullable
    public Item getItem() {
        return this.getTankInfo() != null ? this.getTankInfo().item : null;
    }

    @Override
    public boolean canMountWithNearEmptyMinecart() {
        return MCH_Config.MountMinecartTank.prmBool;
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
    }

    @Override
    public float getGiveDamageRot() {
        return 91.0f;
    }

    @Override
    protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        super.func_70014_b(par1NBTTagCompound);
    }

    @Override
    protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        super.func_70037_a(par1NBTTagCompound);
        if (this.tankInfo == null) {
            this.tankInfo = MCH_TankInfoManager.get(this.getTypeName());
            if (this.tankInfo == null) {
                MCH_Lib.Log(this, "##### MCH_EntityTank readEntityFromNBT() Tank info null %d, %s", W_Entity.getEntityId(this), this.getEntityName());
                this.func_70106_y();
            } else {
                this.setAcInfo(this.tankInfo);
            }
        }
    }

    @Override
    public void func_70106_y() {
        super.func_70106_y();
    }

    @Override
    public void onInteractFirst(EntityPlayer player) {
        this.addkeyRotValue = 0.0f;
        player.field_70759_as = player.field_70758_at = this.getLastRiderYaw();
        player.field_70126_B = player.field_70177_z = this.getLastRiderYaw();
        player.field_70125_A = this.getLastRiderPitch();
    }

    @Override
    public boolean canSwitchGunnerMode() {
        if (!super.canSwitchGunnerMode()) {
            return false;
        }
        return false;
    }

    @Override
    public void onUpdateAircraft() {
        if (this.tankInfo == null) {
            this.changeType(this.getTypeName());
            this.field_70169_q = this.field_70165_t;
            this.field_70167_r = this.field_70163_u;
            this.field_70166_s = this.field_70161_v;
            return;
        }
        if (!this.isRequestedSyncStatus) {
            this.isRequestedSyncStatus = true;
            if (this.field_70170_p.field_72995_K) {
                MCH_PacketStatusRequest.requestStatus(this);
            }
        }
        if (this.lastRiddenByEntity == null && this.getRiddenByEntity() != null) {
            this.initCurrentWeapon(this.getRiddenByEntity());
        }
        this.updateWeapons();
        this.onUpdate_Seats();
        this.onUpdate_Control();
        this.prevRotationRotor = this.rotationRotor;
        this.rotationRotor = (float)((double)this.rotationRotor + this.getCurrentThrottle() * (double)this.getAcInfo().rotorSpeed);
        if (this.rotationRotor > 360.0f) {
            this.rotationRotor -= 360.0f;
            this.prevRotationRotor -= 360.0f;
        }
        if (this.rotationRotor < 0.0f) {
            this.rotationRotor += 360.0f;
            this.prevRotationRotor += 360.0f;
        }
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (this.isDestroyed() && this.getCurrentThrottle() > 0.0) {
            if (MCH_Lib.getBlockIdY(this, 3, -2) > 0) {
                this.setCurrentThrottle(this.getCurrentThrottle() * 0.8);
            }
            if (this.isExploded()) {
                this.setCurrentThrottle(this.getCurrentThrottle() * 0.98);
            }
        }
        this.updateCameraViewers();
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_Client();
        } else {
            this.onUpdate_Server();
        }
    }

    @SideOnly(value=Side.CLIENT)
    public boolean func_90999_ad() {
        return this.isDestroyed() || super.func_90999_ad();
    }

    @Override
    public void updateExtraBoundingBox() {
        if (this.field_70170_p.field_72995_K) {
            super.updateExtraBoundingBox();
        } else if (this.getCountOnUpdate() <= 1) {
            super.updateExtraBoundingBox();
            super.updateExtraBoundingBox();
        }
    }

    public ClacAxisBB calculateXOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double x) {
        for (int j5 = 0; j5 < list.size(); ++j5) {
            x = list.get(j5).func_72316_a(bb, x);
        }
        return new ClacAxisBB(x, bb.func_72317_d(x, 0.0, 0.0));
    }

    public ClacAxisBB calculateYOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double y) {
        return this.calculateYOffset(list, bb, bb, y);
    }

    public ClacAxisBB calculateYOffset(List<AxisAlignedBB> list, AxisAlignedBB calcBB, AxisAlignedBB offsetBB, double y) {
        for (int k = 0; k < list.size(); ++k) {
            y = list.get(k).func_72323_b(calcBB, y);
        }
        return new ClacAxisBB(y, offsetBB.func_72317_d(0.0, y, 0.0));
    }

    public ClacAxisBB calculateZOffset(List<AxisAlignedBB> list, AxisAlignedBB bb, double z) {
        for (int k5 = 0; k5 < list.size(); ++k5) {
            z = list.get(k5).func_72322_c(bb, z);
        }
        return new ClacAxisBB(z, bb.func_72317_d(0.0, 0.0, z));
    }

    @Override
    public void func_70091_d(MoverType type, double x, double y, double z) {
        BlockPos blockpos1;
        IBlockState iblockstate1;
        Block block1;
        ClacAxisBB v;
        this.field_70170_p.field_72984_F.func_76320_a("move");
        double d2 = x;
        double d3 = y;
        double d4 = z;
        List<AxisAlignedBB> list1 = MCH_EntityTank.getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(x, y, z));
        AxisAlignedBB axisalignedbb = this.func_174813_aQ();
        if (y != 0.0) {
            v = this.calculateYOffset(list1, this.func_174813_aQ(), y);
            y = v.value;
            this.func_174826_a(v.bb);
        }
        boolean flag = this.field_70122_E || d3 != y && d3 < 0.0;
        for (MCH_BoundingBox ebb : this.extraBoundingBox) {
            ebb.updatePosition(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.getRotYaw(), this.getRotPitch(), this.getRotRoll());
        }
        if (x != 0.0) {
            v = this.calculateXOffset(list1, this.func_174813_aQ(), x);
            x = v.value;
            if (x != 0.0) {
                this.func_174826_a(v.bb);
            }
        }
        if (z != 0.0) {
            v = this.calculateZOffset(list1, this.func_174813_aQ(), z);
            z = v.value;
            if (z != 0.0) {
                this.func_174826_a(v.bb);
            }
        }
        if (this.field_70138_W > 0.0f && flag && (d2 != x || d4 != z)) {
            double d14 = x;
            double d6 = y;
            double d7 = z;
            AxisAlignedBB axisalignedbb1 = this.func_174813_aQ();
            this.func_174826_a(axisalignedbb);
            y = this.field_70138_W;
            List<AxisAlignedBB> list = MCH_EntityTank.getCollisionBoxes(this, this.func_174813_aQ().func_72321_a(d2, y, d4));
            AxisAlignedBB axisalignedbb2 = this.func_174813_aQ();
            AxisAlignedBB axisalignedbb3 = axisalignedbb2.func_72321_a(d2, 0.0, d4);
            double d8 = y;
            v = this.calculateYOffset(list, axisalignedbb3, axisalignedbb2, d8);
            d8 = v.value;
            axisalignedbb2 = v.bb;
            double d18 = d2;
            v = this.calculateXOffset(list, axisalignedbb2, d18);
            d18 = v.value;
            axisalignedbb2 = v.bb;
            double d19 = d4;
            v = this.calculateZOffset(list, axisalignedbb2, d19);
            d19 = v.value;
            axisalignedbb2 = v.bb;
            AxisAlignedBB axisalignedbb4 = this.func_174813_aQ();
            double d20 = y;
            v = this.calculateYOffset(list, axisalignedbb4, d20);
            d20 = v.value;
            axisalignedbb4 = v.bb;
            double d21 = d2;
            v = this.calculateXOffset(list, axisalignedbb4, d21);
            d21 = v.value;
            axisalignedbb4 = v.bb;
            double d22 = d4;
            v = this.calculateZOffset(list, axisalignedbb4, d22);
            d22 = v.value;
            axisalignedbb4 = v.bb;
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
            v = this.calculateYOffset(list, this.func_174813_aQ(), y);
            y = v.value;
            this.func_174826_a(v.bb);
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

    private void rotationByKey(float partialTicks) {
        float rot = 0.2f;
        if (this.moveLeft && !this.moveRight) {
            this.addkeyRotValue -= rot * partialTicks;
        }
        if (this.moveRight && !this.moveLeft) {
            this.addkeyRotValue += rot * partialTicks;
        }
    }

    @Override
    public void onUpdateAngles(float partialTicks) {
        boolean isFly;
        if (this.isDestroyed()) {
            return;
        }
        if (this.isGunnerMode) {
            this.setRotPitch(this.getRotPitch() * 0.95f);
            this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 0.2f);
            if (MathHelper.func_76135_e((float)this.getRotRoll()) > 20.0f) {
                this.setRotRoll(this.getRotRoll() * 0.95f);
            }
        }
        this.updateRecoil(partialTicks);
        this.setRotPitch(this.getRotPitch() + (this.WheelMng.targetPitch - this.getRotPitch()) * partialTicks);
        this.setRotRoll(this.getRotRoll() + (this.WheelMng.targetRoll - this.getRotRoll()) * partialTicks);
        boolean bl = isFly = MCH_Lib.getBlockIdY(this, 3, -3) == 0;
        if (!isFly || this.getAcInfo().isFloat && this.getWaterDepth() > 0.0) {
            float gmy = 1.0f;
            if (!isFly) {
                Block block;
                gmy = this.getAcInfo().mobilityYawOnGround;
                if (!(this.getAcInfo().canRotOnGround || W_Block.isEqual(block = MCH_Lib.getBlockY(this, 3, -2, false), W_Block.getWater()) || W_Block.isEqual(block, W_Blocks.field_150350_a))) {
                    gmy = 0.0f;
                }
            }
            float pivotTurnThrottle = this.getAcInfo().pivotTurnThrottle;
            double dx = this.field_70165_t - this.field_70169_q;
            double dz = this.field_70161_v - this.field_70166_s;
            double dist = dx * dx + dz * dz;
            if (pivotTurnThrottle <= 0.0f || this.getCurrentThrottle() >= (double)pivotTurnThrottle || this.throttleBack >= pivotTurnThrottle / 10.0f || dist > (double)this.throttleBack * 0.01) {
                float flag;
                float sf = (float)Math.sqrt(dist <= 1.0 ? dist : 1.0);
                if (pivotTurnThrottle <= 0.0f) {
                    sf = 1.0f;
                }
                float f = flag = !this.throttleUp && this.throttleDown && this.getCurrentThrottle() < (double)pivotTurnThrottle + 0.05 ? -1.0f : 1.0f;
                if (this.moveLeft && !this.moveRight) {
                    this.setRotYaw(this.getRotYaw() - 0.6f * gmy * partialTicks * flag * sf);
                }
                if (this.moveRight && !this.moveLeft) {
                    this.setRotYaw(this.getRotYaw() + 0.6f * gmy * partialTicks * flag * sf);
                }
            }
        }
        this.addkeyRotValue = (float)((double)this.addkeyRotValue * (1.0 - (double)(0.1f * partialTicks)));
    }

    protected void onUpdate_Control() {
        if (this.isGunnerMode && !this.canUseFuel()) {
            this.switchGunnerMode(false);
        }
        this.throttleBack = (float)((double)this.throttleBack * 0.8);
        if (this.getBrake()) {
            this.throttleBack = (float)((double)this.throttleBack * 0.5);
            if (this.getCurrentThrottle() > 0.0) {
                this.addCurrentThrottle(-0.02 * (double)this.getAcInfo().throttleUpDown);
            } else {
                this.setCurrentThrottle(0.0);
            }
        }
        if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L && this.isCanopyClose() && this.canUseFuel() && !this.isDestroyed()) {
            this.onUpdate_ControlSub();
        } else if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
            this.throttleUp = true;
            this.onUpdate_ControlSub();
        } else if (this.getCurrentThrottle() > 0.0) {
            this.addCurrentThrottle(-0.0025 * (double)this.getAcInfo().throttleUpDown);
        } else {
            this.setCurrentThrottle(0.0);
        }
        if (this.getCurrentThrottle() < 0.0) {
            this.setCurrentThrottle(0.0);
        }
        if (this.field_70170_p.field_72995_K) {
            if (!W_Lib.isClientPlayer(this.getRiddenByEntity()) || this.getCountOnUpdate() % 200 == 0) {
                double ct = this.getThrottle();
                if (this.getCurrentThrottle() > ct) {
                    this.addCurrentThrottle(-0.005);
                }
                if (this.getCurrentThrottle() < ct) {
                    this.addCurrentThrottle(0.005);
                }
            }
        } else {
            this.setThrottle(this.getCurrentThrottle());
        }
    }

    protected void onUpdate_ControlSub() {
        if (!this.isGunnerMode) {
            float throttleUpDown = this.getAcInfo().throttleUpDown;
            if (this.throttleUp) {
                float f = throttleUpDown;
                if (this.func_184187_bx() != null) {
                    double mx = this.func_184187_bx().field_70159_w;
                    double mz = this.func_184187_bx().field_70179_y;
                    f *= MathHelper.func_76133_a((double)(mx * mx + mz * mz)) * this.getAcInfo().throttleUpDownOnEntity;
                }
                if (this.getAcInfo().enableBack && this.throttleBack > 0.0f) {
                    this.throttleBack = (float)((double)this.throttleBack - 0.01 * (double)f);
                } else {
                    this.throttleBack = 0.0f;
                    if (this.getCurrentThrottle() < 1.0) {
                        this.addCurrentThrottle(0.01 * (double)f);
                    } else {
                        this.setCurrentThrottle(1.0);
                    }
                }
            } else if (this.throttleDown) {
                if (this.getCurrentThrottle() > 0.0) {
                    this.addCurrentThrottle(-0.01 * (double)throttleUpDown);
                } else {
                    this.setCurrentThrottle(0.0);
                    if (this.getAcInfo().enableBack) {
                        this.throttleBack = (float)((double)this.throttleBack + 0.0025 * (double)throttleUpDown);
                        if (this.throttleBack > 0.6f) {
                            this.throttleBack = 0.6f;
                        }
                    }
                }
            } else if (this.cs_tankAutoThrottleDown && this.getCurrentThrottle() > 0.0) {
                this.addCurrentThrottle(-0.005 * (double)throttleUpDown);
                if (this.getCurrentThrottle() <= 0.0) {
                    this.setCurrentThrottle(0.0);
                }
            }
        }
    }

    protected void onUpdate_Particle2() {
        int d;
        if (!this.field_70170_p.field_72995_K) {
            return;
        }
        if ((double)this.getHP() >= (double)this.getMaxHP() * 0.5) {
            return;
        }
        if (this.getTankInfo() == null) {
            return;
        }
        int bbNum = this.getTankInfo().extraBoundingBox.size();
        if (bbNum < 0) {
            bbNum = 0;
        }
        if (this.isFirstDamageSmoke || this.prevDamageSmokePos.length != bbNum + 1) {
            this.prevDamageSmokePos = new Vec3d[bbNum + 1];
        }
        float yaw = this.getRotYaw();
        float pitch = this.getRotPitch();
        float roll = this.getRotRoll();
        for (int ri = 0; ri < bbNum; ++ri) {
            if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2 && this.getMaxHP() > 0) {
                d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2) / 0.3 * 15.0);
                if (d > 0 && this.field_70146_Z.nextInt(d) <= 0) continue;
            }
            MCH_BoundingBox bb = (MCH_BoundingBox)this.getTankInfo().extraBoundingBox.get(ri);
            Vec3d pos = this.getTransformedPosition(bb.offsetX, bb.offsetY, bb.offsetZ);
            double x = pos.field_72450_a;
            double y = pos.field_72448_b;
            double z = pos.field_72449_c;
            this.onUpdate_Particle2SpawnSmoke(ri, x, y, z, 1.0f);
        }
        boolean b = true;
        if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2 && this.getMaxHP() > 0 && (d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2) / 0.3 * 15.0)) > 0 && this.field_70146_Z.nextInt(d) > 0) {
            b = false;
        }
        if (b) {
            double px = this.field_70165_t;
            double py = this.field_70163_u;
            double pz = this.field_70161_v;
            if (this.getSeatInfo(0) != null && this.getSeatInfo((int)0).pos != null) {
                Vec3d pos = MCH_Lib.RotVec3(0.0, this.getSeatInfo((int)0).pos.field_72448_b, -2.0, -yaw, -pitch, -roll);
                px += pos.field_72450_a;
                py += pos.field_72448_b;
                pz += pos.field_72449_c;
            }
            this.onUpdate_Particle2SpawnSmoke(bbNum, px, py, pz, bbNum == 0 ? 2.0f : 1.0f);
        }
        this.isFirstDamageSmoke = false;
    }

    public void onUpdate_Particle2SpawnSmoke(int ri, double x, double y, double z, float size) {
        if (this.isFirstDamageSmoke || this.prevDamageSmokePos[ri] == null) {
            this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
        }
        int num = 1;
        for (int i = 0; i < num; ++i) {
            float c = 0.2f + this.field_70146_Z.nextFloat() * 0.3f;
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", x, y, z);
            prm.motionX = (double)size * (this.field_70146_Z.nextDouble() - 0.5) * 0.3;
            prm.motionY = (double)size * this.field_70146_Z.nextDouble() * 0.1;
            prm.motionZ = (double)size * (this.field_70146_Z.nextDouble() - 0.5) * 0.3;
            prm.size = size * ((float)this.field_70146_Z.nextInt(5) + 5.0f) * 1.0f;
            prm.setColor(0.7f + this.field_70146_Z.nextFloat() * 0.1f, c, c, c);
            MCH_ParticlesUtil.spawnParticle(prm);
        }
        this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
    }

    public void onUpdate_Particle2SpawnSmode(int ri, double x, double y, double z, float size) {
        if (this.isFirstDamageSmoke) {
            this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
        }
        Vec3d prev = this.prevDamageSmokePos[ri];
        double dx = x - prev.field_72450_a;
        double dy = y - prev.field_72448_b;
        double dz = z - prev.field_72449_c;
        int num = (int)((double)MathHelper.func_76133_a((double)(dx * dx + dy * dy + dz * dz)) / 0.3) + 1;
        for (int i = 0; i < num; ++i) {
            float c = 0.2f + this.field_70146_Z.nextFloat() * 0.3f;
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", x, y, z);
            prm.motionX = (double)size * (this.field_70146_Z.nextDouble() - 0.5) * 0.3;
            prm.motionY = (double)size * this.field_70146_Z.nextDouble() * 0.1;
            prm.motionZ = (double)size * (this.field_70146_Z.nextDouble() - 0.5) * 0.3;
            prm.size = size * ((float)this.field_70146_Z.nextInt(5) + 5.0f) * 1.0f;
            prm.setColor(0.7f + this.field_70146_Z.nextFloat() * 0.1f, c, c, c);
            MCH_ParticlesUtil.spawnParticle(prm);
        }
        this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
    }

    public void onUpdate_ParticleLandingGear() {
        this.WheelMng.particleLandingGear();
    }

    private void onUpdate_ParticleSplash() {
        if (this.getAcInfo() == null) {
            return;
        }
        if (!this.field_70170_p.field_72995_K) {
            return;
        }
        double mx = this.field_70165_t - this.field_70169_q;
        double mz = this.field_70161_v - this.field_70166_s;
        double dist = mx * mx + mz * mz;
        if (dist > 1.0) {
            dist = 1.0;
        }
        for (MCH_AircraftInfo.ParticleSplash p : this.getAcInfo().particleSplashs) {
            for (int i = 0; i < p.num; ++i) {
                if (!(dist > 0.03 + (double)this.field_70146_Z.nextFloat() * 0.1)) continue;
                this.setParticleSplash(p.pos, -mx * (double)p.acceleration, p.motionY, -mz * (double)p.acceleration, p.gravity, (double)p.size * (0.5 + dist * 0.5), p.age);
            }
        }
    }

    private void setParticleSplash(Vec3d pos, double mx, double my, double mz, float gravity, double size, int age) {
        Vec3d v = this.getTransformedPosition(pos);
        v = v.func_72441_c(this.field_70146_Z.nextDouble() - 0.5, (this.field_70146_Z.nextDouble() - 0.5) * 0.5, this.field_70146_Z.nextDouble() - 0.5);
        int x = (int)(v.field_72450_a + 0.5);
        int y = (int)(v.field_72448_b + 0.0);
        int z = (int)(v.field_72449_c + 0.5);
        if (W_WorldFunc.isBlockWater(this.field_70170_p, x, y, z)) {
            float c = this.field_70146_Z.nextFloat() * 0.3f + 0.7f;
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", v.field_72450_a, v.field_72448_b, v.field_72449_c);
            prm.motionX = mx + ((double)this.field_70146_Z.nextFloat() - 0.5) * 0.7;
            prm.motionY = my;
            prm.motionZ = mz + ((double)this.field_70146_Z.nextFloat() - 0.5) * 0.7;
            prm.size = (float)size * (this.field_70146_Z.nextFloat() * 0.2f + 0.8f);
            prm.setColor(0.9f, c, c, c);
            prm.age = age + (int)((double)this.field_70146_Z.nextFloat() * 0.5 * (double)age);
            prm.gravity = gravity;
            MCH_ParticlesUtil.spawnParticle(prm);
        }
    }

    @Override
    public void destroyAircraft() {
        super.destroyAircraft();
        this.rotDestroyedPitch = 0.0f;
        this.rotDestroyedRoll = 0.0f;
        this.rotDestroyedYaw = 0.0f;
    }

    @Override
    public int getClientPositionDelayCorrection() {
        return this.getTankInfo().weightType == 1 ? 2 : (this.getTankInfo() == null ? 7 : 7);
    }

    protected void onUpdate_Client() {
        if (this.getRiddenByEntity() != null && W_Lib.isClientPlayer(this.getRiddenByEntity())) {
            this.getRiddenByEntity().field_70125_A = this.getRiddenByEntity().field_70127_C;
        }
        if (this.aircraftPosRotInc > 0) {
            this.applyServerPositionAndRotation();
        } else {
            this.func_70107_b(this.field_70165_t + this.field_70159_w, this.field_70163_u + this.field_70181_x, this.field_70161_v + this.field_70179_y);
            if (!this.isDestroyed() && (this.field_70122_E || MCH_Lib.getBlockIdY(this, 1, -2) > 0)) {
                this.field_70159_w *= 0.95;
                this.field_70179_y *= 0.95;
                this.applyOnGroundPitch(0.95f);
            }
            if (this.func_70090_H()) {
                this.field_70159_w *= 0.99;
                this.field_70179_y *= 0.99;
            }
        }
        this.updateWheels();
        this.onUpdate_Particle2();
        this.updateSound();
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_ParticleLandingGear();
            this.onUpdate_ParticleSplash();
            this.onUpdate_ParticleSandCloud(true);
        }
        this.updateCamera(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    @Override
    public void applyOnGroundPitch(float factor) {
    }

    private void onUpdate_Server() {
        float speedLimit;
        double motion;
        Block block;
        double prevMotion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        double dp = 0.0;
        if (this.canFloatWater()) {
            dp = this.getWaterDepth();
        }
        boolean levelOff = this.isGunnerMode;
        if (dp == 0.0) {
            if (!levelOff) {
                this.field_70181_x += 0.04 + (double)(!this.func_70090_H() ? this.getAcInfo().gravity : this.getAcInfo().gravityInWater);
                this.field_70181_x += -0.047 * (1.0 - this.getCurrentThrottle());
            } else {
                this.field_70181_x *= 0.8;
            }
        } else if (MathHelper.func_76135_e((float)this.getRotRoll()) >= 40.0f || dp < 1.0) {
            this.field_70181_x -= 1.0E-4;
            this.field_70181_x += 0.007 * this.getCurrentThrottle();
        } else {
            if (this.field_70181_x < 0.0) {
                this.field_70181_x /= 2.0;
            }
            this.field_70181_x += 0.007;
        }
        float throttle = (float)(this.getCurrentThrottle() / 10.0);
        Vec3d v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - 10.0f);
        if (!levelOff) {
            this.field_70181_x += v.field_72448_b * (double)throttle / 8.0;
        }
        boolean canMove = true;
        if (!(this.getAcInfo().canMoveOnGround || W_Block.isEqual(block = MCH_Lib.getBlockY(this, 3, -2, false), W_Block.getWater()) || W_Block.isEqual(block, W_Blocks.field_150350_a))) {
            canMove = false;
        }
        if (canMove) {
            if (this.getAcInfo().enableBack && this.throttleBack > 0.0f) {
                this.field_70159_w -= v.field_72450_a * (double)this.throttleBack;
                this.field_70179_y -= v.field_72449_c * (double)this.throttleBack;
            } else {
                this.field_70159_w += v.field_72450_a * (double)throttle;
                this.field_70179_y += v.field_72449_c * (double)throttle;
            }
        }
        if ((motion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y)) > (double)(speedLimit = this.getMaxSpeed())) {
            this.field_70159_w *= (double)speedLimit / motion;
            this.field_70179_y *= (double)speedLimit / motion;
            motion = speedLimit;
        }
        if (motion > prevMotion && this.currentSpeed < (double)speedLimit) {
            this.currentSpeed += ((double)speedLimit - this.currentSpeed) / 35.0;
            if (this.currentSpeed > (double)speedLimit) {
                this.currentSpeed = speedLimit;
            }
        } else {
            this.currentSpeed -= (this.currentSpeed - 0.07) / 35.0;
            if (this.currentSpeed < 0.07) {
                this.currentSpeed = 0.07;
            }
        }
        if (this.field_70122_E || MCH_Lib.getBlockIdY(this, 1, -2) > 0) {
            this.field_70159_w *= (double)this.getAcInfo().motionFactor;
            this.field_70179_y *= (double)this.getAcInfo().motionFactor;
            if (MathHelper.func_76135_e((float)this.getRotPitch()) < 40.0f) {
                this.applyOnGroundPitch(0.8f);
            }
        }
        this.updateWheels();
        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        this.field_70181_x *= 0.95;
        this.field_70159_w *= (double)this.getAcInfo().motionFactor;
        this.field_70179_y *= (double)this.getAcInfo().motionFactor;
        this.func_70101_b(this.getRotYaw(), this.getRotPitch());
        this.onUpdate_updateBlock();
        this.updateCollisionBox();
        if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
            this.unmountEntity();
        }
    }

    private void collisionEntity(AxisAlignedBB bb) {
        if (bb == null) {
            return;
        }
        double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
        if (speed <= 0.05) {
            return;
        }
        Entity rider = this.getRiddenByEntity();
        float damage = (float)(speed * 15.0);
        MCH_EntityAircraft rideAc = this.func_184187_bx() instanceof MCH_EntitySeat ? ((MCH_EntitySeat)this.func_184187_bx()).getParent() : (this.func_184187_bx() instanceof MCH_EntityAircraft ? (MCH_EntityAircraft)this.func_184187_bx() : null);
        List list = this.field_70170_p.func_175674_a((Entity)this, bb.func_72314_b(0.3, 0.3, 0.3), e -> {
            MCH_EntityTank tank;
            if (e == rideAc || e instanceof EntityItem || e instanceof EntityXPOrb || e instanceof MCH_EntityBaseBullet || e instanceof MCH_EntityChain || e instanceof MCH_EntitySeat) {
                return false;
            }
            if (e instanceof MCH_EntityTank && (tank = (MCH_EntityTank)e).getTankInfo() != null && tank.getTankInfo().weightType == 2) {
                return MCH_Config.Collision_EntityTankDamage.prmBool;
            }
            return MCH_Config.Collision_EntityDamage.prmBool;
        });
        for (int i = 0; i < list.size(); ++i) {
            Entity e2 = (Entity)list.get(i);
            if (!this.shouldCollisionDamage(e2)) continue;
            double dx = e2.field_70165_t - this.field_70165_t;
            double dz = e2.field_70161_v - this.field_70161_v;
            double dist = Math.sqrt(dx * dx + dz * dz);
            if (dist > 5.0) {
                dist = 5.0;
            }
            damage = (float)((double)damage + (5.0 - dist));
            DamageSource ds = rider instanceof EntityLivingBase ? DamageSource.func_76358_a((EntityLivingBase)((EntityLivingBase)rider)) : DamageSource.field_76377_j;
            MCH_Lib.applyEntityHurtResistantTimeConfig(e2);
            e2.func_70097_a(ds, damage);
            if (e2 instanceof MCH_EntityAircraft) {
                e2.field_70159_w += this.field_70159_w * 0.05;
                e2.field_70179_y += this.field_70179_y * 0.05;
            } else if (e2 instanceof EntityArrow) {
                e2.func_70106_y();
            } else {
                e2.field_70159_w += this.field_70159_w * 1.5;
                e2.field_70179_y += this.field_70179_y * 1.5;
            }
            if (this.getTankInfo().weightType != 2 && (e2.field_70130_N >= 1.0f || (double)e2.field_70131_O >= 1.5)) {
                ds = e2 instanceof EntityLivingBase ? DamageSource.func_76358_a((EntityLivingBase)((EntityLivingBase)e2)) : DamageSource.field_76377_j;
                this.func_70097_a(ds, damage / 3.0f);
            }
            MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityTank.collisionEntity damage=%.1f %s", Float.valueOf(damage), e2.toString());
        }
    }

    private boolean shouldCollisionDamage(Entity e) {
        MCH_EntityAircraft ac;
        if (this.getSeatIdByEntity(e) >= 0) {
            return false;
        }
        if (this.noCollisionEntities.containsKey(e)) {
            return false;
        }
        if (e instanceof MCH_EntityHitBox && ((MCH_EntityHitBox)e).parent != null && this.noCollisionEntities.containsKey(ac = ((MCH_EntityHitBox)e).parent)) {
            return false;
        }
        if (e.func_184187_bx() instanceof MCH_EntityAircraft && this.noCollisionEntities.containsKey(e.func_184187_bx())) {
            return false;
        }
        return !(e.func_184187_bx() instanceof MCH_EntitySeat) || ((MCH_EntitySeat)e.func_184187_bx()).getParent() == null || !this.noCollisionEntities.containsKey(((MCH_EntitySeat)e.func_184187_bx()).getParent());
    }

    public void updateCollisionBox() {
        if (this.getAcInfo() == null) {
            return;
        }
        this.WheelMng.updateBlock();
        for (MCH_BoundingBox bb : this.extraBoundingBox) {
            if (this.field_70146_Z.nextInt(3) != 0) continue;
            if (MCH_Config.Collision_DestroyBlock.prmBool) {
                Vec3d v = this.getTransformedPosition(bb.offsetX, bb.offsetY, bb.offsetZ);
                this.destoryBlockRange(v, bb.width, bb.height);
            }
            this.collisionEntity(bb.getBoundingBox());
        }
        if (MCH_Config.Collision_DestroyBlock.prmBool) {
            this.destoryBlockRange(this.getTransformedPosition(0.0, 0.0, 0.0), (double)this.field_70130_N * 1.5, this.field_70131_O * 2.0f);
        }
        this.collisionEntity(this.func_70046_E());
    }

    public void destoryBlockRange(Vec3d v, double w, double h) {
        if (this.getAcInfo() == null) {
            return;
        }
        List<Block> destroyBlocks = MCH_Config.getBreakableBlockListFromType(this.getTankInfo().weightType);
        List<Block> noDestroyBlocks = MCH_Config.getNoBreakableBlockListFromType(this.getTankInfo().weightType);
        List<Material> destroyMaterials = MCH_Config.getBreakableMaterialListFromType(this.getTankInfo().weightType);
        int ws = (int)(w + 2.0) / 2;
        int hs = (int)(h + 2.0) / 2;
        for (int x = -ws; x <= ws; ++x) {
            block1: for (int z = -ws; z <= ws; ++z) {
                block2: for (int y = -hs; y <= hs + 1; ++y) {
                    int bx = (int)(v.field_72450_a + (double)x - 0.5);
                    int by = (int)(v.field_72448_b + (double)y - 1.0);
                    int bz = (int)(v.field_72449_c + (double)z - 0.5);
                    BlockPos blockpos = new BlockPos(bx, by, bz);
                    IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos);
                    Block block = by >= 0 && by < 256 ? iblockstate.func_177230_c() : Blocks.field_150350_a;
                    Material mat = iblockstate.func_185904_a();
                    if (Block.func_149680_a((Block)block, (Block)Blocks.field_150350_a)) continue;
                    for (Block c : noDestroyBlocks) {
                        if (!Block.func_149680_a((Block)block, (Block)c)) continue;
                        block = null;
                        break;
                    }
                    if (block == null) continue block1;
                    for (Block c : destroyBlocks) {
                        if (!Block.func_149680_a((Block)block, (Block)c)) continue;
                        this.destroyBlock(blockpos);
                        mat = null;
                        break;
                    }
                    if (mat == null) continue block1;
                    for (Material m : destroyMaterials) {
                        if (iblockstate.func_185904_a() != m) continue;
                        this.destroyBlock(blockpos);
                        continue block2;
                    }
                }
            }
        }
    }

    public void destroyBlock(BlockPos blockpos) {
        if (this.field_70146_Z.nextInt(8) == 0) {
            W_WorldFunc.destroyBlock(this.field_70170_p, blockpos, true);
        } else {
            this.field_70170_p.func_175698_g(blockpos);
        }
    }

    private void updateWheels() {
        this.WheelMng.move(this.field_70159_w, this.field_70181_x, this.field_70179_y);
    }

    public float getMaxSpeed() {
        return this.getTankInfo().speed + 0.0f;
    }

    @Override
    public void setAngles(Entity player, boolean fixRot, float fixYaw, float fixPitch, float deltaX, float deltaY, float x, float y, float partialTicks) {
        if (partialTicks < 0.03f) {
            partialTicks = 0.4f;
        }
        if (partialTicks > 0.9f) {
            partialTicks = 0.6f;
        }
        this.lowPassPartialTicks.put(partialTicks);
        partialTicks = this.lowPassPartialTicks.getAvg();
        float ac_pitch = this.getRotPitch();
        float ac_yaw = this.getRotYaw();
        float ac_roll = this.getRotRoll();
        if (this.isFreeLookMode()) {
            y = 0.0f;
            x = 0.0f;
        }
        float yaw = 0.0f;
        float pitch = 0.0f;
        float roll = 0.0f;
        MCH_Math.FMatrix m_add = MCH_Math.newMatrix();
        MCH_Math.MatTurnZ(m_add, roll / 180.0f * (float)Math.PI);
        MCH_Math.MatTurnX(m_add, pitch / 180.0f * (float)Math.PI);
        MCH_Math.MatTurnY(m_add, yaw / 180.0f * (float)Math.PI);
        MCH_Math.MatTurnZ(m_add, (float)((double)(this.getRotRoll() / 180.0f) * Math.PI));
        MCH_Math.MatTurnX(m_add, (float)((double)(this.getRotPitch() / 180.0f) * Math.PI));
        MCH_Math.MatTurnY(m_add, (float)((double)(this.getRotYaw() / 180.0f) * Math.PI));
        MCH_Math.FVector3D v = MCH_Math.MatrixToEuler(m_add);
        v.x = MCH_Lib.RNG(v.x, -90.0f, 90.0f);
        v.z = MCH_Lib.RNG(v.z, -90.0f, 90.0f);
        if (v.z > 180.0f) {
            v.z -= 360.0f;
        }
        if (v.z < -180.0f) {
            v.z += 360.0f;
        }
        this.setRotYaw(v.y);
        this.setRotPitch(v.x);
        this.setRotRoll(v.z);
        this.onUpdateAngles(partialTicks);
        if (this.getAcInfo().limitRotation) {
            v.x = MCH_Lib.RNG(this.getRotPitch(), -90.0f, 90.0f);
            v.z = MCH_Lib.RNG(this.getRotRoll(), -90.0f, 90.0f);
            this.setRotPitch(v.x);
            this.setRotRoll(v.z);
        }
        if (MathHelper.func_76135_e((float)this.getRotPitch()) > 90.0f) {
            MCH_Lib.DbgLog(true, "MCH_EntityAircraft.setAngles Error:Pitch=%.1f", Float.valueOf(this.getRotPitch()));
            this.setRotPitch(0.0f);
        }
        if (this.getRotRoll() > 180.0f) {
            this.setRotRoll(this.getRotRoll() - 360.0f);
        }
        if (this.getRotRoll() < -180.0f) {
            this.setRotRoll(this.getRotRoll() + 360.0f);
        }
        this.prevRotationRoll = this.getRotRoll();
        this.field_70127_C = this.getRotPitch();
        if (this.func_184187_bx() == null) {
            this.field_70126_B = this.getRotYaw();
        }
        float deltaLimit = this.getAcInfo().cameraRotationSpeed * partialTicks;
        MCH_WeaponSet ws = this.getCurrentWeapon(player);
        if (deltaX > (deltaLimit *= ws != null && ws.getInfo() != null ? ws.getInfo().cameraRotationSpeedPitch : 1.0f)) {
            deltaX = deltaLimit;
        }
        if (deltaX < -deltaLimit) {
            deltaX = -deltaLimit;
        }
        if (deltaY > deltaLimit) {
            deltaY = deltaLimit;
        }
        if (deltaY < -deltaLimit) {
            deltaY = -deltaLimit;
        }
        if (this.isOverridePlayerYaw() || fixRot) {
            if (this.func_184187_bx() == null) {
                player.field_70126_B = this.getRotYaw() + fixYaw;
            } else {
                if (this.getRotYaw() - player.field_70177_z > 180.0f) {
                    player.field_70126_B += 360.0f;
                }
                if (this.getRotYaw() - player.field_70177_z < -180.0f) {
                    player.field_70126_B -= 360.0f;
                }
            }
            player.field_70177_z = this.getRotYaw() + fixYaw;
        } else {
            player.func_70082_c(deltaX, 0.0f);
        }
        if (this.isOverridePlayerPitch() || fixRot) {
            player.field_70127_C = this.getRotPitch() + fixPitch;
            player.field_70125_A = this.getRotPitch() + fixPitch;
        } else {
            player.func_70082_c(0.0f, deltaY);
        }
        float playerYaw = MathHelper.func_76142_g((float)(this.getRotYaw() - player.field_70177_z));
        float playerPitch = this.getRotPitch() * MathHelper.func_76134_b((float)((float)((double)playerYaw * Math.PI / 180.0))) + -this.getRotRoll() * MathHelper.func_76126_a((float)((float)((double)playerYaw * Math.PI / 180.0)));
        if (MCH_MOD.proxy.isFirstPerson()) {
            player.field_70125_A = MCH_Lib.RNG(player.field_70125_A, playerPitch + this.getAcInfo().minRotationPitch, playerPitch + this.getAcInfo().maxRotationPitch);
            player.field_70125_A = MCH_Lib.RNG(player.field_70125_A, -90.0f, 90.0f);
        }
        player.field_70127_C = player.field_70125_A;
        if (this.func_184187_bx() == null && ac_yaw != this.getRotYaw() || ac_pitch != this.getRotPitch() || ac_roll != this.getRotRoll()) {
            this.aircraftRotChanged = true;
        }
    }

    @Override
    public float getSoundVolume() {
        if (this.getAcInfo() != null && this.getAcInfo().throttleUpDown <= 0.0f) {
            return 0.0f;
        }
        return this.soundVolume * 0.7f;
    }

    public void updateSound() {
        float target = (float)this.getCurrentThrottle();
        if (this.getRiddenByEntity() != null && (this.partCanopy == null || this.getCanopyRotation() < 1.0f)) {
            target += 0.1f;
        }
        if (this.moveLeft || this.moveRight || this.throttleDown) {
            this.soundVolumeTarget += 0.1f;
            if (this.soundVolumeTarget > 0.75f) {
                this.soundVolumeTarget = 0.75f;
            }
        } else {
            this.soundVolumeTarget *= 0.8f;
        }
        if (target < this.soundVolumeTarget) {
            target = this.soundVolumeTarget;
        }
        if (this.soundVolume < target) {
            this.soundVolume += 0.02f;
            if (this.soundVolume >= target) {
                this.soundVolume = target;
            }
        } else if (this.soundVolume > target) {
            this.soundVolume -= 0.02f;
            if (this.soundVolume <= target) {
                this.soundVolume = target;
            }
        }
    }

    @Override
    public float getSoundPitch() {
        float target2;
        float target1 = (float)(0.5 + this.getCurrentThrottle() * 0.5);
        return target1 > (target2 = (float)(0.5 + (double)this.soundVolumeTarget * 0.5)) ? target1 : target2;
    }

    @Override
    public String getDefaultSoundName() {
        return "prop";
    }

    @Override
    public boolean hasBrake() {
        return true;
    }

    @Override
    public void updateParts(int stat) {
        MCH_Parts[] parts;
        super.updateParts(stat);
        if (this.isDestroyed()) {
            return;
        }
        for (MCH_Parts p : parts = new MCH_Parts[0]) {
            if (p == null) continue;
            p.updateStatusClient(stat);
            p.update();
        }
    }

    @Override
    public float getUnfoldLandingGearThrottle() {
        return 0.7f;
    }

    private static class ClacAxisBB {
        public final double value;
        public final AxisAlignedBB bb;

        public ClacAxisBB(double value, AxisAlignedBB bb) {
            this.value = value;
            this.bb = bb;
        }
    }
}

