/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.RayTraceResult$Type
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import java.util.Random;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.weapon.MCH_Cartridge;
import mcheli.weapon.MCH_IEntityLockChecker;
import mcheli.weapon.MCH_SightType;
import mcheli.weapon.MCH_WeaponGuidanceSystem;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import mcheli.wrapper.W_McClient;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class MCH_WeaponBase {
    protected static final Random rand = new Random();
    public final World worldObj;
    public final Vec3d position;
    public final float fixRotationYaw;
    public final float fixRotationPitch;
    public final String name;
    public final MCH_WeaponInfo weaponInfo;
    public String displayName;
    public int power;
    public float acceleration;
    public int explosionPower;
    public int explosionPowerInWater;
    public int interval;
    public int delayedInterval;
    public int numMode;
    public int lockTime;
    public int piercing;
    public int heatCount;
    public MCH_Cartridge cartridge;
    public boolean onTurret;
    public MCH_EntityAircraft aircraft;
    public int tick;
    public int optionParameter1;
    public int optionParameter2;
    private int currentMode;
    public boolean canPlaySound;

    public MCH_WeaponBase(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        this.worldObj = w;
        this.position = v;
        this.fixRotationYaw = yaw;
        this.fixRotationPitch = pitch;
        this.name = nm;
        this.weaponInfo = wi;
        this.displayName = wi != null ? wi.displayName : "";
        this.power = 0;
        this.acceleration = 0.0f;
        this.explosionPower = 0;
        this.explosionPowerInWater = 0;
        this.interval = 1;
        this.numMode = 0;
        this.lockTime = 0;
        this.heatCount = 0;
        this.cartridge = null;
        this.tick = 0;
        this.optionParameter1 = 0;
        this.optionParameter2 = 0;
        this.setCurrentMode(0);
        this.canPlaySound = true;
    }

    public MCH_WeaponInfo getInfo() {
        return this.weaponInfo;
    }

    public String getName() {
        return this.displayName;
    }

    public abstract boolean shot(MCH_WeaponParam var1);

    public void setLockChecker(MCH_IEntityLockChecker checker) {
    }

    public void setLockCountMax(int n) {
    }

    public int getLockCount() {
        return 0;
    }

    public int getLockCountMax() {
        return 0;
    }

    public final int getNumAmmoMax() {
        return this.getInfo().round;
    }

    public int getCurrentMode() {
        return this.getInfo() != null && this.getInfo().fixMode > 0 ? this.getInfo().fixMode : this.currentMode;
    }

    public void setCurrentMode(int currentMode) {
        this.currentMode = currentMode;
    }

    public final int getAllAmmoNum() {
        return this.getInfo().maxAmmo;
    }

    public final int getReloadCount() {
        return this.getInfo().reloadTime;
    }

    public final MCH_SightType getSightType() {
        return this.getInfo().sight;
    }

    public MCH_WeaponGuidanceSystem getGuidanceSystem() {
        return null;
    }

    public void update(int countWait) {
        if (countWait != 0) {
            ++this.tick;
        }
    }

    public boolean isCooldownCountReloadTime() {
        return false;
    }

    public void modifyCommonParameters() {
        this.modifyParameters();
    }

    public void modifyParameters() {
    }

    public boolean switchMode() {
        if (this.getInfo() != null && this.getInfo().fixMode > 0) {
            return false;
        }
        int beforeMode = this.getCurrentMode();
        if (this.numMode > 0) {
            this.setCurrentMode((this.getCurrentMode() + 1) % this.numMode);
        } else {
            this.setCurrentMode(0);
        }
        if (beforeMode != this.getCurrentMode()) {
            this.onSwitchMode();
        }
        return beforeMode != this.getCurrentMode();
    }

    public void onSwitchMode() {
    }

    public boolean use(MCH_WeaponParam prm) {
        Vec3d v = this.getShotPos(prm.entity);
        prm.posX += v.field_72450_a;
        prm.posY += v.field_72448_b;
        prm.posZ += v.field_72449_c;
        if (this.shot(prm)) {
            this.tick = 0;
            return true;
        }
        return false;
    }

    public Vec3d getShotPos(Entity entity) {
        if (entity instanceof MCH_EntityAircraft && this.onTurret) {
            return ((MCH_EntityAircraft)entity).calcOnTurretPos(this.position);
        }
        Vec3d v = new Vec3d(this.position.field_72450_a, this.position.field_72448_b, this.position.field_72449_c);
        float roll = entity instanceof MCH_EntityAircraft ? ((MCH_EntityAircraft)entity).getRotRoll() : 0.0f;
        return MCH_Lib.RotVec3(v, -entity.field_70177_z, -entity.field_70125_A, -roll);
    }

    public void playSound(Entity e) {
        this.playSound(e, this.getInfo().soundFileName);
    }

    public void playSound(Entity e, String snd) {
        if (!e.field_70170_p.field_72995_K && this.canPlaySound && this.getInfo() != null) {
            float prnd = this.getInfo().soundPitchRandom;
            W_WorldFunc.MOD_playSoundEffect(this.worldObj, e.field_70165_t, e.field_70163_u, e.field_70161_v, snd, this.getInfo().soundVolume, this.getInfo().soundPitch * (1.0f - prnd) + rand.nextFloat() * prnd);
        }
    }

    public void playSoundClient(Entity e, float volume, float pitch) {
        if (e.field_70170_p.field_72995_K && this.getInfo() != null) {
            W_McClient.MOD_playSoundFX(this.getInfo().soundFileName, volume, pitch);
        }
    }

    public double getLandInDistance(MCH_WeaponParam prm) {
        if (this.weaponInfo == null) {
            return -1.0;
        }
        if (this.weaponInfo.gravity >= 0.0f) {
            return -1.0;
        }
        Vec3d v = MCH_Lib.RotVec3(0.0, 0.0, 1.0, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
        double s = Math.sqrt(v.field_72450_a * v.field_72450_a + v.field_72448_b * v.field_72448_b + v.field_72449_c * v.field_72449_c);
        double acc = this.acceleration < 4.0f ? (double)this.acceleration : 4.0;
        double accFac = (double)this.acceleration / acc;
        double my = v.field_72448_b * (double)this.acceleration / s;
        if (my <= 0.0) {
            return -1.0;
        }
        double mx = v.field_72450_a * (double)this.acceleration / s;
        double mz = v.field_72449_c * (double)this.acceleration / s;
        double ls = my / (double)this.weaponInfo.gravity;
        double gravity = (double)this.weaponInfo.gravity * accFac;
        if (ls < -12.0) {
            double f = ls / -12.0;
            mx *= f;
            my *= f;
            mz *= f;
            gravity *= f * f * 0.95;
        }
        double spx = prm.posX;
        double spy = prm.posY + 3.0;
        double spz = prm.posZ;
        for (int i = 0; i < 50; ++i) {
            Vec3d vs = new Vec3d(spx, spy, spz);
            Vec3d ve = new Vec3d(spx + mx, spy + my, spz + mz);
            RayTraceResult mop = this.worldObj.func_72933_a(vs, ve);
            if (mop != null && mop.field_72313_a == RayTraceResult.Type.BLOCK) {
                double dx = (double)mop.func_178782_a().func_177958_n() - prm.posX;
                double dz = (double)mop.func_178782_a().func_177952_p() - prm.posZ;
                return Math.sqrt(dx * dx + dz * dz);
            }
            spx += mx;
            spy += (my += gravity);
            spz += mz;
            if (!(spy < prm.posY)) continue;
            double dx = spx - prm.posX;
            double dz = spz - prm.posZ;
            return Math.sqrt(dx * dx + dz * dz);
        }
        return -1.0;
    }
}

