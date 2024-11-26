/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.MoverType
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.Item
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.plane;

import javax.annotation.Nullable;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_PacketStatusRequest;
import mcheli.aircraft.MCH_Parts;
import mcheli.particles.MCH_ParticleParam;
import mcheli.particles.MCH_ParticlesUtil;
import mcheli.plane.MCP_PlaneInfo;
import mcheli.plane.MCP_PlaneInfoManager;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Blocks;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCP_EntityPlane
extends MCH_EntityAircraft {
    private MCP_PlaneInfo planeInfo = null;
    public float soundVolume;
    public MCH_Parts partNozzle;
    public MCH_Parts partWing;
    public float rotationRotor;
    public float prevRotationRotor;
    public float addkeyRotValue;

    public MCP_EntityPlane(World world) {
        super(world);
        this.currentSpeed = 0.07;
        this.field_70156_m = true;
        this.func_70105_a(2.0f, 0.7f);
        this.field_70159_w = 0.0;
        this.field_70181_x = 0.0;
        this.field_70179_y = 0.0;
        this.weapons = this.createWeapon(0);
        this.soundVolume = 0.0f;
        this.partNozzle = null;
        this.partWing = null;
        this.field_70138_W = 0.6f;
        this.rotationRotor = 0.0f;
        this.prevRotationRotor = 0.0f;
    }

    @Override
    public String getKindName() {
        return "planes";
    }

    @Override
    public String getEntityType() {
        return "Plane";
    }

    public MCP_PlaneInfo getPlaneInfo() {
        return this.planeInfo;
    }

    @Override
    public void changeType(String type) {
        MCH_Lib.DbgLog(this.field_70170_p, "MCP_EntityPlane.changeType " + type + " : " + this.toString(), new Object[0]);
        if (!type.isEmpty()) {
            this.planeInfo = MCP_PlaneInfoManager.get(type);
        }
        if (this.planeInfo == null) {
            MCH_Lib.Log(this, "##### MCP_EntityPlane changePlaneType() Plane info null %d, %s, %s", W_Entity.getEntityId(this), type, this.getEntityName());
            this.func_70106_y();
        } else {
            this.setAcInfo(this.planeInfo);
            this.newSeats(this.getAcInfo().getNumSeatAndRack());
            this.partNozzle = this.createNozzle(this.planeInfo);
            this.partWing = this.createWing(this.planeInfo);
            this.weapons = this.createWeapon(1 + this.getSeatNum());
            this.initPartRotation(this.getRotYaw(), this.getRotPitch());
        }
    }

    @Override
    @Nullable
    public Item getItem() {
        return this.getPlaneInfo() != null ? this.getPlaneInfo().item : null;
    }

    @Override
    public boolean canMountWithNearEmptyMinecart() {
        return MCH_Config.MountMinecartPlane.prmBool;
    }

    @Override
    protected void func_70088_a() {
        super.func_70088_a();
    }

    @Override
    protected void func_70014_b(NBTTagCompound par1NBTTagCompound) {
        super.func_70014_b(par1NBTTagCompound);
    }

    @Override
    protected void func_70037_a(NBTTagCompound par1NBTTagCompound) {
        super.func_70037_a(par1NBTTagCompound);
        if (this.planeInfo == null) {
            this.planeInfo = MCP_PlaneInfoManager.get(this.getTypeName());
            if (this.planeInfo == null) {
                MCH_Lib.Log(this, "##### MCP_EntityPlane readEntityFromNBT() Plane info null %d, %s", W_Entity.getEntityId(this), this.getEntityName());
                this.func_70106_y();
            } else {
                this.setAcInfo(this.planeInfo);
            }
        }
    }

    @Override
    public void func_70106_y() {
        super.func_70106_y();
    }

    @Override
    public int getNumEjectionSeat() {
        if (this.getAcInfo() != null && this.getAcInfo().isEnableEjectionSeat) {
            int n = this.getSeatNum() + 1;
            return n <= 2 ? n : 0;
        }
        return 0;
    }

    @Override
    public void onInteractFirst(EntityPlayer player) {
        this.addkeyRotValue = 0.0f;
    }

    @Override
    public boolean canSwitchGunnerMode() {
        if (!super.canSwitchGunnerMode()) {
            return false;
        }
        float roll = MathHelper.func_76135_e((float)MathHelper.func_76142_g((float)this.getRotRoll()));
        float pitch = MathHelper.func_76135_e((float)MathHelper.func_76142_g((float)this.getRotPitch()));
        if (roll > 40.0f || pitch > 40.0f) {
            return false;
        }
        return this.getCurrentThrottle() > (double)0.6f && MCH_Lib.getBlockIdY(this, 3, -5) == 0;
    }

    @Override
    public void onUpdateAircraft() {
        if (this.planeInfo == null) {
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
        if (this.field_70122_E && this.getVtolMode() == 0 && this.planeInfo.isDefaultVtol) {
            this.swithVtolMode(true);
        }
        this.field_70169_q = this.field_70165_t;
        this.field_70167_r = this.field_70163_u;
        this.field_70166_s = this.field_70161_v;
        if (!this.isDestroyed() && this.isHovering() && MathHelper.func_76135_e((float)this.getRotPitch()) < 70.0f) {
            this.setRotPitch(this.getRotPitch() * 0.95f, "isHovering()");
        }
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

    @Override
    public boolean canUpdateYaw(Entity player) {
        return super.canUpdateYaw(player) && !this.isHovering();
    }

    @Override
    public boolean canUpdatePitch(Entity player) {
        return super.canUpdatePitch(player) && !this.isHovering();
    }

    @Override
    public boolean canUpdateRoll(Entity player) {
        return super.canUpdateRoll(player) && !this.isHovering();
    }

    @Override
    public float getYawFactor() {
        float yaw = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolYaw : super.getYawFactor();
        return yaw * 0.8f;
    }

    @Override
    public float getPitchFactor() {
        float pitch = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolPitch : super.getPitchFactor();
        return pitch * 0.8f;
    }

    @Override
    public float getRollFactor() {
        float roll = this.getVtolMode() > 0 ? this.getPlaneInfo().vtolYaw : super.getRollFactor();
        return roll * 0.8f;
    }

    @Override
    public boolean isOverridePlayerPitch() {
        return super.isOverridePlayerPitch() && !this.isHovering();
    }

    @Override
    public boolean isOverridePlayerYaw() {
        return super.isOverridePlayerYaw() && !this.isHovering();
    }

    @Override
    public float getControlRotYaw(float mouseX, float mouseY, float tick) {
        if (MCH_Config.MouseControlFlightSimMode.prmBool) {
            this.rotationByKey(tick);
            return this.addkeyRotValue * 20.0f;
        }
        return mouseX;
    }

    @Override
    public float getControlRotPitch(float mouseX, float mouseY, float tick) {
        return mouseY;
    }

    @Override
    public float getControlRotRoll(float mouseX, float mouseY, float tick) {
        if (MCH_Config.MouseControlFlightSimMode.prmBool) {
            return mouseX * 2.0f;
        }
        if (this.getVtolMode() == 0) {
            return mouseX * 0.5f;
        }
        return mouseX;
    }

    private void rotationByKey(float partialTicks) {
        float rot = 0.2f;
        if (!MCH_Config.MouseControlFlightSimMode.prmBool && this.getVtolMode() != 0) {
            rot *= 0.0f;
        }
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
        boolean bl = isFly = MCH_Lib.getBlockIdY(this, 3, -3) == 0;
        if (!isFly || this.isFreeLookMode() || this.isGunnerMode || this.getAcInfo().isFloat && this.getWaterDepth() > 0.0) {
            float gmy = 1.0f;
            if (!isFly) {
                Block block;
                gmy = this.getAcInfo().mobilityYawOnGround;
                if (!(this.getAcInfo().canRotOnGround || W_Block.isEqual(block = MCH_Lib.getBlockY(this, 3, -2, false), W_Block.getWater()) || W_Block.isEqual(block, W_Blocks.field_150350_a))) {
                    gmy = 0.0f;
                }
            }
            if (this.moveLeft && !this.moveRight) {
                this.setRotYaw(this.getRotYaw() - 0.6f * gmy * partialTicks);
            }
            if (this.moveRight && !this.moveLeft) {
                this.setRotYaw(this.getRotYaw() + 0.6f * gmy * partialTicks);
            }
        } else if (isFly && !MCH_Config.MouseControlFlightSimMode.prmBool) {
            this.rotationByKey(partialTicks);
            this.setRotRoll(this.getRotRoll() + this.addkeyRotValue * 0.5f * this.getAcInfo().mobilityRoll * partialTicks * 3.3f);
        }
        this.addkeyRotValue = (float)((double)this.addkeyRotValue * (1.0 - (double)(0.1f * partialTicks)));
        if (!isFly && MathHelper.func_76135_e((float)this.getRotPitch()) < 40.0f) {
            this.applyOnGroundPitch(0.97f);
        }
        if (this.getNozzleRotation() > 0.001f) {
            float rot = 1.0f - 0.03f * partialTicks;
            this.setRotPitch(this.getRotPitch() * rot);
            rot = 1.0f - 0.1f * partialTicks;
            this.setRotRoll(this.getRotRoll() * rot);
        }
    }

    protected void onUpdate_Control() {
        if (this.isGunnerMode && !this.canUseFuel()) {
            this.switchGunnerMode(false);
        }
        this.throttleBack = (float)((double)this.throttleBack * 0.8);
        if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L && this.isCanopyClose() && this.canUseWing() && this.canUseFuel() && !this.isDestroyed()) {
            this.onUpdate_ControlNotHovering();
        } else if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
            this.throttleUp = true;
            this.onUpdate_ControlNotHovering();
        } else if (this.getCurrentThrottle() > 0.0) {
            this.addCurrentThrottle(-0.0025 * (double)this.getAcInfo().throttleUpDown);
        } else {
            this.setCurrentThrottle(0.0);
        }
        if (this.getCurrentThrottle() < 0.0) {
            this.setCurrentThrottle(0.0);
        }
        if (this.field_70170_p.field_72995_K) {
            if (!W_Lib.isClientPlayer(this.getRiddenByEntity())) {
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

    protected void onUpdate_ControlNotHovering() {
        if (!this.isGunnerMode) {
            float throttleUpDown = this.getAcInfo().throttleUpDown;
            boolean turn = this.moveLeft && !this.moveRight || !this.moveLeft && this.moveRight;
            boolean localThrottleUp = this.throttleUp;
            if (turn && this.getCurrentThrottle() < (double)this.getAcInfo().pivotTurnThrottle && !localThrottleUp && !this.throttleDown) {
                localThrottleUp = true;
                throttleUpDown *= 2.0f;
            }
            if (localThrottleUp) {
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
            } else if (this.cs_planeAutoThrottleDown && this.getCurrentThrottle() > 0.0) {
                this.addCurrentThrottle(-0.005 * (double)throttleUpDown);
                if (this.getCurrentThrottle() <= 0.0) {
                    this.setCurrentThrottle(0.0);
                }
            }
        }
    }

    protected void onUpdate_Particle() {
        if (this.field_70170_p.field_72995_K) {
            this.onUpdate_ParticleLandingGear();
            this.onUpdate_ParticleNozzle();
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
        if (this.getPlaneInfo() == null) {
            return;
        }
        int rotorNum = this.getPlaneInfo().rotorList.size();
        if (rotorNum < 0) {
            rotorNum = 0;
        }
        if (this.isFirstDamageSmoke) {
            this.prevDamageSmokePos = new Vec3d[rotorNum + 1];
        }
        float yaw = this.getRotYaw();
        float pitch = this.getRotPitch();
        float roll = this.getRotRoll();
        boolean spawnSmoke = true;
        for (int ri = 0; ri < rotorNum; ++ri) {
            int d2;
            if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2 && this.getMaxHP() > 0 && (d2 = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2) / 0.3 * 15.0)) > 0 && this.field_70146_Z.nextInt(d2) > 0) {
                spawnSmoke = false;
            }
            Vec3d rotor_pos = this.getPlaneInfo().rotorList.get((int)ri).pos;
            Vec3d pos = MCH_Lib.RotVec3(rotor_pos, -yaw, -pitch, -roll);
            double x = this.field_70165_t + pos.field_72450_a;
            double y = this.field_70163_u + pos.field_72448_b;
            double z = this.field_70161_v + pos.field_72449_c;
            this.onUpdate_Particle2SpawnSmoke(ri, x, y, z, 1.0f, spawnSmoke);
        }
        spawnSmoke = true;
        if ((double)this.getHP() >= (double)this.getMaxHP() * 0.2 && this.getMaxHP() > 0 && (d = (int)(((double)(this.getHP() / this.getMaxHP()) - 0.2) / 0.3 * 15.0)) > 0 && this.field_70146_Z.nextInt(d) > 0) {
            spawnSmoke = false;
        }
        double px = this.field_70165_t;
        double py = this.field_70163_u;
        double pz = this.field_70161_v;
        if (this.getSeatInfo(0) != null && this.getSeatInfo((int)0).pos != null) {
            Vec3d pos = MCH_Lib.RotVec3(0.0, this.getSeatInfo((int)0).pos.field_72448_b, -2.0, -yaw, -pitch, -roll);
            px += pos.field_72450_a;
            py += pos.field_72448_b;
            pz += pos.field_72449_c;
        }
        this.onUpdate_Particle2SpawnSmoke(rotorNum, px, py, pz, rotorNum == 0 ? 2.0f : 1.0f, spawnSmoke);
        this.isFirstDamageSmoke = false;
    }

    public void onUpdate_Particle2SpawnSmoke(int ri, double x, double y, double z, float size, boolean spawnSmoke) {
        if (this.isFirstDamageSmoke || this.prevDamageSmokePos[ri] == null) {
            this.prevDamageSmokePos[ri] = new Vec3d(x, y, z);
        }
        Vec3d prev = this.prevDamageSmokePos[ri];
        double dx = x - prev.field_72450_a;
        double dy = y - prev.field_72448_b;
        double dz = z - prev.field_72449_c;
        int num = (int)((double)MathHelper.func_76133_a((double)(dx * dx + dy * dy + dz * dz)) / 0.3) + 1;
        for (int i = 0; i < num; ++i) {
            float c = 0.2f + this.field_70146_Z.nextFloat() * 0.3f;
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", prev.field_72450_a + (x - prev.field_72450_a) * (double)i / 3.0, prev.field_72448_b + (y - prev.field_72448_b) * (double)i / 3.0, prev.field_72449_c + (z - prev.field_72449_c) * (double)i / 3.0);
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
        double d = this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y;
        if (d > 0.01) {
            int x = MathHelper.func_76128_c((double)(this.field_70165_t + 0.5));
            int y = MathHelper.func_76128_c((double)(this.field_70163_u - 0.5));
            int z = MathHelper.func_76128_c((double)(this.field_70161_v + 0.5));
            MCH_ParticlesUtil.spawnParticleTileCrack(this.field_70170_p, x, y, z, this.field_70165_t + ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)this.field_70130_N, this.func_174813_aQ().field_72338_b + 0.1, this.field_70161_v + ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)this.field_70130_N, -this.field_70159_w * 4.0, 1.5, -this.field_70179_y * 4.0);
        }
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

    public void onUpdate_ParticleNozzle() {
        if (this.planeInfo == null || !this.planeInfo.haveNozzle()) {
            return;
        }
        if (this.getCurrentThrottle() <= (double)0.1f) {
            return;
        }
        float yaw = this.getRotYaw();
        float pitch = this.getRotPitch();
        float roll = this.getRotRoll();
        Vec3d nozzleRot = MCH_Lib.RotVec3(0.0, 0.0, 1.0, -yaw - 180.0f, pitch - this.getNozzleRotation(), roll);
        for (MCH_AircraftInfo.DrawnPart nozzle : this.planeInfo.nozzles) {
            if (!((double)this.field_70146_Z.nextFloat() <= this.getCurrentThrottle() * 1.5)) continue;
            Vec3d nozzlePos = MCH_Lib.RotVec3(nozzle.pos, -yaw, -pitch, -roll);
            double x = this.field_70165_t + nozzlePos.field_72450_a + nozzleRot.field_72450_a;
            double y = this.field_70163_u + nozzlePos.field_72448_b + nozzleRot.field_72448_b;
            double z = this.field_70161_v + nozzlePos.field_72449_c + nozzleRot.field_72449_c;
            float a = 0.7f;
            if (W_WorldFunc.getBlockId(this.field_70170_p, (int)(x + nozzleRot.field_72450_a * 3.0), (int)(y + nozzleRot.field_72448_b * 3.0), (int)(z + nozzleRot.field_72449_c * 3.0)) != 0) {
                a = 2.0f;
            }
            MCH_ParticleParam prm = new MCH_ParticleParam(this.field_70170_p, "smoke", x, y, z, nozzleRot.field_72450_a + (double)((this.field_70146_Z.nextFloat() - 0.5f) * a), nozzleRot.field_72448_b, nozzleRot.field_72449_c + (double)((this.field_70146_Z.nextFloat() - 0.5f) * a), 5.0f * this.getAcInfo().particlesScale);
            MCH_ParticlesUtil.spawnParticle(prm);
        }
    }

    @Override
    public void destroyAircraft() {
        super.destroyAircraft();
        int inv = 1;
        if (this.getRotRoll() >= 0.0f) {
            if (this.getRotRoll() > 90.0f) {
                inv = -1;
            }
        } else if (this.getRotRoll() > -90.0f) {
            inv = -1;
        }
        this.rotDestroyedRoll = (0.5f + this.field_70146_Z.nextFloat()) * (float)inv;
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
        if (this.isDestroyed()) {
            if (MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
                float roll;
                if (MathHelper.func_76135_e((float)this.getRotPitch()) < 10.0f) {
                    this.setRotPitch(this.getRotPitch() + this.rotDestroyedPitch);
                }
                if ((roll = MathHelper.func_76135_e((float)this.getRotRoll())) < 45.0f || roll > 135.0f) {
                    this.setRotRoll(this.getRotRoll() + this.rotDestroyedRoll);
                }
            } else if (MathHelper.func_76135_e((float)this.getRotPitch()) > 20.0f) {
                this.setRotPitch(this.getRotPitch() * 0.99f);
            }
        }
        if (this.getRiddenByEntity() != null) {
            // empty if block
        }
        this.updateSound();
        this.onUpdate_Particle();
        this.onUpdate_Particle2();
        this.onUpdate_ParticleSplash();
        this.onUpdate_ParticleSandCloud(true);
        this.updateCamera(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    private void onUpdate_Server() {
        float speedLimit;
        double motion;
        Block block;
        Vec3d v;
        double prevMotion = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        double dp = 0.0;
        if (this.canFloatWater()) {
            dp = this.getWaterDepth();
        }
        boolean levelOff = this.isGunnerMode;
        if (dp == 0.0) {
            if (this.isTargetDrone() && this.canUseFuel() && !this.isDestroyed()) {
                Block block2 = MCH_Lib.getBlockY(this, 3, -40, true);
                if (block2 == null || W_Block.isEqual(block2, W_Blocks.field_150350_a)) {
                    this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 1.0f);
                    this.setRotPitch(this.getRotPitch() * 0.95f);
                    if (this.canFoldLandingGear()) {
                        this.foldLandingGear();
                    }
                    levelOff = true;
                } else {
                    block2 = MCH_Lib.getBlockY(this, 3, -5, true);
                    if (block2 == null || W_Block.isEqual(block2, W_Blocks.field_150350_a)) {
                        this.setRotYaw(this.getRotYaw() + this.getAcInfo().autoPilotRot * 2.0f);
                        if (this.getRotPitch() > -20.0f) {
                            this.setRotPitch(this.getRotPitch() - 0.5f);
                        }
                    }
                }
            }
            if (!levelOff) {
                this.field_70181_x += 0.04 + (double)(!this.func_70090_H() ? this.getAcInfo().gravity : this.getAcInfo().gravityInWater);
                this.field_70181_x += -0.047 * (1.0 - this.getCurrentThrottle());
            } else {
                this.field_70181_x *= 0.8;
            }
        } else {
            this.setRotPitch(this.getRotPitch() * 0.8f, "getWaterDepth != 0");
            if (MathHelper.func_76135_e((float)this.getRotRoll()) < 40.0f) {
                this.setRotRoll(this.getRotRoll() * 0.9f);
            }
            if (dp < 1.0) {
                this.field_70181_x -= 1.0E-4;
                this.field_70181_x += 0.007 * this.getCurrentThrottle();
            } else {
                if (this.field_70181_x < 0.0) {
                    this.field_70181_x /= 2.0;
                }
                this.field_70181_x += 0.007;
            }
        }
        float throttle = (float)(this.getCurrentThrottle() / 10.0);
        if (this.getNozzleRotation() > 0.001f) {
            this.setRotPitch(this.getRotPitch() * 0.95f);
            v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - this.getNozzleRotation());
            if (this.getNozzleRotation() >= 90.0f) {
                v = new Vec3d(v.field_72450_a * (double)0.8f, v.field_72448_b, v.field_72449_c * (double)0.8f);
            }
        } else {
            v = MCH_Lib.Rot2Vec3(this.getRotYaw(), this.getRotPitch() - 10.0f);
        }
        if (!levelOff) {
            this.field_70181_x = this.getNozzleRotation() <= 0.01f ? (this.field_70181_x += v.field_72448_b * (double)throttle / 2.0) : (this.field_70181_x += v.field_72448_b * (double)throttle / 8.0);
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
        this.func_70091_d(MoverType.SELF, this.field_70159_w, this.field_70181_x, this.field_70179_y);
        this.field_70181_x *= 0.95;
        this.field_70159_w *= (double)this.getAcInfo().motionFactor;
        this.field_70179_y *= (double)this.getAcInfo().motionFactor;
        this.func_70101_b(this.getRotYaw(), this.getRotPitch());
        this.onUpdate_updateBlock();
        if (this.getRiddenByEntity() != null && this.getRiddenByEntity().field_70128_L) {
            this.unmountEntity();
        }
    }

    public float getMaxSpeed() {
        float f = 0.0f;
        if (this.partWing != null && this.getPlaneInfo().isVariableSweepWing) {
            f = (this.getPlaneInfo().sweepWingSpeed - this.getPlaneInfo().speed) * this.partWing.getFactor();
        } else if (this.partHatch != null && this.getPlaneInfo().isVariableSweepWing) {
            f = (this.getPlaneInfo().sweepWingSpeed - this.getPlaneInfo().speed) * this.partHatch.getFactor();
        }
        return this.getPlaneInfo().speed + f;
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
        return (float)(0.6 + this.getCurrentThrottle() * 0.4);
    }

    @Override
    public String getDefaultSoundName() {
        return "plane";
    }

    @Override
    public void updateParts(int stat) {
        MCH_Parts[] parts;
        super.updateParts(stat);
        if (this.isDestroyed()) {
            return;
        }
        for (MCH_Parts p : parts = new MCH_Parts[]{this.partNozzle, this.partWing}) {
            if (p == null) continue;
            p.updateStatusClient(stat);
            p.update();
        }
        if (!this.field_70170_p.field_72995_K && this.partWing != null && this.getPlaneInfo().isVariableSweepWing && this.partWing.isON() && this.getCurrentThrottle() >= (double)0.2f && (this.getCurrentThrottle() < 0.5 || MCH_Lib.getBlockIdY(this, 1, -10) != 0)) {
            this.partWing.setStatusServer(false);
        }
    }

    @Override
    public float getUnfoldLandingGearThrottle() {
        return 0.7f;
    }

    public boolean canSwitchVtol() {
        if (this.planeInfo == null || !this.planeInfo.isEnableVtol) {
            return false;
        }
        if (this.getModeSwitchCooldown() > 0) {
            return false;
        }
        if (this.getVtolMode() == 1) {
            return false;
        }
        if (MathHelper.func_76135_e((float)this.getRotRoll()) > 30.0f) {
            return false;
        }
        if (this.field_70122_E && this.planeInfo.isDefaultVtol) {
            return false;
        }
        this.setModeSwitchCooldown(20);
        return true;
    }

    public boolean getNozzleStat() {
        return this.partNozzle != null ? this.partNozzle.getStatus() : false;
    }

    @Override
    public int getVtolMode() {
        if (!this.getNozzleStat()) {
            return this.getNozzleRotation() <= 0.005f ? 0 : 1;
        }
        return this.getNozzleRotation() >= 89.995f ? 2 : 1;
    }

    public float getFuleConsumptionFactor() {
        return super.getFuelConsumptionFactor() * (float)(this.getVtolMode() == 2 ? true : true);
    }

    public float getNozzleRotation() {
        return this.partNozzle != null ? this.partNozzle.rotation : 0.0f;
    }

    public float getPrevNozzleRotation() {
        return this.partNozzle != null ? this.partNozzle.prevRotation : 0.0f;
    }

    public void swithVtolMode(boolean mode) {
        if (this.partNozzle != null) {
            if (this.planeInfo.isDefaultVtol && this.field_70122_E && !mode) {
                return;
            }
            if (!this.field_70170_p.field_72995_K) {
                this.partNozzle.setStatusServer(mode);
            }
            if (this.getRiddenByEntity() != null && !this.getRiddenByEntity().field_70128_L) {
                this.getRiddenByEntity().field_70127_C = 0.0f;
                this.getRiddenByEntity().field_70125_A = 0.0f;
            }
        }
    }

    protected MCH_Parts createNozzle(MCP_PlaneInfo info) {
        MCH_Parts nozzle = null;
        if (info.haveNozzle() || info.haveRotor() || info.isEnableVtol) {
            nozzle = new MCH_Parts(this, 1, (DataParameter<Integer>)PART_STAT, "Nozzle");
            nozzle.rotationMax = 90.0f;
            nozzle.rotationInv = 1.5f;
            nozzle.soundStartSwichOn.setPrm("plane_cc", 1.0f, 0.5f);
            nozzle.soundEndSwichOn.setPrm("plane_cc", 1.0f, 0.5f);
            nozzle.soundStartSwichOff.setPrm("plane_cc", 1.0f, 0.5f);
            nozzle.soundEndSwichOff.setPrm("plane_cc", 1.0f, 0.5f);
            nozzle.soundSwitching.setPrm("plane_cv", 1.0f, 0.5f);
            if (info.isDefaultVtol) {
                nozzle.forceSwitch(true);
            }
        }
        return nozzle;
    }

    protected MCH_Parts createWing(MCP_PlaneInfo info) {
        MCH_Parts wing = null;
        if (this.planeInfo.haveWing()) {
            wing = new MCH_Parts(this, 3, (DataParameter<Integer>)PART_STAT, "Wing");
            wing.rotationMax = 90.0f;
            wing.rotationInv = 2.5f;
            wing.soundStartSwichOn.setPrm("plane_cc", 1.0f, 0.5f);
            wing.soundEndSwichOn.setPrm("plane_cc", 1.0f, 0.5f);
            wing.soundStartSwichOff.setPrm("plane_cc", 1.0f, 0.5f);
            wing.soundEndSwichOff.setPrm("plane_cc", 1.0f, 0.5f);
        }
        return wing;
    }

    public boolean canUseWing() {
        if (this.partWing == null) {
            return true;
        }
        if (this.getPlaneInfo().isVariableSweepWing) {
            if (this.getCurrentThrottle() < 0.2) {
                return this.partWing.isOFF();
            }
            return true;
        }
        return this.partWing.isOFF();
    }

    public boolean canFoldWing() {
        if (this.partWing == null || this.getModeSwitchCooldown() > 0) {
            return false;
        }
        if (this.getPlaneInfo().isVariableSweepWing) {
            if (this.field_70122_E || MCH_Lib.getBlockIdY(this, 3, -20) != 0 ? this.getCurrentThrottle() > (double)0.1f : this.getCurrentThrottle() < (double)0.7f) {
                return false;
            }
        } else {
            if (!this.field_70122_E && MCH_Lib.getBlockIdY(this, 3, -3) == 0) {
                return false;
            }
            if (this.getCurrentThrottle() > (double)0.01f) {
                return false;
            }
        }
        return this.partWing.isOFF();
    }

    public boolean canUnfoldWing() {
        if (this.partWing == null || this.getModeSwitchCooldown() > 0) {
            return false;
        }
        return this.partWing.isON();
    }

    public void foldWing(boolean fold) {
        if (this.partWing == null || this.getModeSwitchCooldown() > 0) {
            return;
        }
        this.partWing.setStatusServer(fold);
        this.setModeSwitchCooldown(20);
    }

    public float getWingRotation() {
        return this.partWing != null ? this.partWing.rotation : 0.0f;
    }

    public float getPrevWingRotation() {
        return this.partWing != null ? this.partWing.prevRotation : 0.0f;
    }
}

