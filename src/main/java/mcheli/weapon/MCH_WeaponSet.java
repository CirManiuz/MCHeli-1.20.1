/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.weapon;

import java.util.Random;
import mcheli.MCH_Lib;
import mcheli.mob.MCH_EntityGunner;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.weapon.MCH_EntityCartridge;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import mcheli.wrapper.W_McClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MCH_WeaponSet {
    private static Random rand = new Random();
    private final String name;
    protected MCH_WeaponBase[] weapons;
    private int currentWeaponIndex;
    public float rotationYaw;
    public float rotationPitch;
    public float prevRotationYaw;
    public float prevRotationPitch;
    public float defaultRotationYaw;
    public float rotationTurretYaw;
    public float rotBay;
    public float prevRotBay;
    public Recoil[] recoilBuf;
    protected int numAmmo;
    protected int numRestAllAmmo;
    public int currentHeat;
    public int cooldownSpeed;
    public int countWait;
    public int countReloadWait;
    protected int[] lastUsedCount;
    public int soundWait;
    private int lastUsedOptionParameter1 = 0;
    private int lastUsedOptionParameter2 = 0;
    public float rotBarrelSpd;
    public float rotBarrel;
    public float prevRotBarrel;

    public MCH_WeaponSet(MCH_WeaponBase[] weapon) {
        this.name = weapon[0].name;
        this.weapons = weapon;
        this.currentWeaponIndex = 0;
        this.countWait = 0;
        this.countReloadWait = 0;
        this.lastUsedCount = new int[weapon.length];
        this.rotationYaw = 0.0f;
        this.prevRotationYaw = 0.0f;
        this.rotationPitch = 0.0f;
        this.prevRotationPitch = 0.0f;
        this.setAmmoNum(0);
        this.setRestAllAmmoNum(0);
        this.currentHeat = 0;
        this.soundWait = 0;
        this.cooldownSpeed = 1;
        this.rotBarrelSpd = 0.0f;
        this.rotBarrel = 0.0f;
        this.prevRotBarrel = 0.0f;
        this.recoilBuf = new Recoil[weapon.length];
        for (int i = 0; i < this.recoilBuf.length; ++i) {
            this.recoilBuf[i] = new Recoil(this, weapon[i].getInfo().recoilBufCount, weapon[i].getInfo().recoilBufCountSpeed);
        }
        this.defaultRotationYaw = 0.0f;
    }

    public MCH_WeaponSet(MCH_WeaponBase weapon) {
        this(new MCH_WeaponBase[]{weapon});
    }

    public boolean isEqual(String s) {
        return this.name.equalsIgnoreCase(s);
    }

    public int getAmmoNum() {
        return this.numAmmo;
    }

    public int getAmmoNumMax() {
        return this.getFirstWeapon().getNumAmmoMax();
    }

    public int getRestAllAmmoNum() {
        return this.numRestAllAmmo;
    }

    public int getAllAmmoNum() {
        return this.getFirstWeapon().getAllAmmoNum();
    }

    public void setAmmoNum(int n) {
        this.numAmmo = n;
    }

    public void setRestAllAmmoNum(int n) {
        int debugBefore = this.numRestAllAmmo;
        int m = this.getInfo().maxAmmo - this.getAmmoNum();
        this.numRestAllAmmo = n <= m ? n : m;
        MCH_Lib.DbgLog(this.getFirstWeapon().worldObj, "MCH_WeaponSet.setRestAllAmmoNum:%s %d->%d (%d)", this.getName(), debugBefore, this.numRestAllAmmo, n);
    }

    public void supplyRestAllAmmo() {
        int m = this.getInfo().maxAmmo;
        if (this.getRestAllAmmoNum() + this.getAmmoNum() < m) {
            this.setRestAllAmmoNum(this.getRestAllAmmoNum() + this.getAmmoNum() + this.getInfo().suppliedNum);
        }
    }

    public boolean isInPreparation() {
        return this.countWait < 0 || this.countReloadWait > 0;
    }

    public String getName() {
        MCH_WeaponBase w = this.getCurrentWeapon();
        return w != null ? w.getName() : "";
    }

    public boolean canUse() {
        return this.countWait == 0;
    }

    public boolean isLongDelayWeapon() {
        return this.getInfo().delay > 4;
    }

    public void reload() {
        MCH_WeaponBase crtWpn = this.getCurrentWeapon();
        if (this.getAmmoNumMax() > 0 && this.getAmmoNum() < this.getAmmoNumMax() && crtWpn.getReloadCount() > 0) {
            this.countReloadWait = crtWpn.getReloadCount();
            if (crtWpn.worldObj.field_72995_K) {
                this.setAmmoNum(0);
            }
            if (!crtWpn.worldObj.field_72995_K) {
                this.countReloadWait -= 20;
                if (this.countReloadWait <= 0) {
                    this.countReloadWait = 1;
                }
            }
        }
    }

    public void reloadMag() {
        int restAmmo = this.getRestAllAmmoNum();
        int nAmmo = this.getAmmoNumMax() - this.getAmmoNum();
        if (nAmmo > 0) {
            if (nAmmo > restAmmo) {
                nAmmo = restAmmo;
            }
            this.setAmmoNum(this.getAmmoNum() + nAmmo);
            this.setRestAllAmmoNum(this.getRestAllAmmoNum() - nAmmo);
        }
    }

    public void switchMode() {
        boolean isChanged = false;
        for (MCH_WeaponBase w : this.weapons) {
            if (w == null) continue;
            isChanged = w.switchMode() || isChanged;
        }
        if (isChanged) {
            int cntSwitch = 15;
            if (this.countWait >= -cntSwitch) {
                this.countWait = this.countWait > cntSwitch ? -this.countWait : -cntSwitch;
            }
            if (this.getCurrentWeapon().worldObj.field_72995_K) {
                W_McClient.playSoundClick(1.0f, 1.0f);
            }
        }
    }

    public void onSwitchWeapon(boolean isRemote, boolean isCreative) {
        int cntSwitch = 15;
        if (isRemote) {
            cntSwitch += 10;
        }
        if (this.countWait >= -cntSwitch) {
            this.countWait = this.countWait > cntSwitch ? -this.countWait : -cntSwitch;
        }
        this.currentWeaponIndex = 0;
        if (isCreative) {
            this.setAmmoNum(this.getAmmoNumMax());
        }
    }

    public boolean isUsed(int index) {
        MCH_WeaponBase w = this.getFirstWeapon();
        if (w != null && index < this.lastUsedCount.length) {
            int cnt = this.lastUsedCount[index];
            return w.interval >= 4 && cnt > w.interval / 2 || cnt >= 4;
        }
        return false;
    }

    public void update(Entity shooter, boolean isSelected, boolean isUsed) {
        if (this.getCurrentWeapon().getInfo() == null) {
            return;
        }
        if (this.countReloadWait > 0) {
            --this.countReloadWait;
            if (this.countReloadWait == 0) {
                this.reloadMag();
            }
        }
        for (int i = 0; i < this.lastUsedCount.length; ++i) {
            if (this.lastUsedCount[i] <= 0) continue;
            if (this.lastUsedCount[i] == 4) {
                if (0 != this.getCurrentWeaponIndex() || !this.canUse() || this.getAmmoNum() <= 0 && this.getAllAmmoNum() > 0) continue;
                int n = i;
                this.lastUsedCount[n] = this.lastUsedCount[n] - 1;
                continue;
            }
            int n = i;
            this.lastUsedCount[n] = this.lastUsedCount[n] - 1;
        }
        if (this.currentHeat > 0) {
            if (this.currentHeat < this.getCurrentWeapon().getInfo().maxHeatCount) {
                ++this.cooldownSpeed;
            }
            this.currentHeat -= this.cooldownSpeed / 20 + 1;
            if (this.currentHeat < 0) {
                this.currentHeat = 0;
            }
        }
        if (this.countWait > 0) {
            --this.countWait;
        }
        if (this.countWait < 0) {
            ++this.countWait;
        }
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        if (this.weapons != null && this.weapons.length > 0) {
            for (MCH_WeaponBase w : this.weapons) {
                if (w == null) continue;
                w.update(this.countWait);
            }
        }
        if (this.soundWait > 0) {
            --this.soundWait;
        }
        if (isUsed && this.rotBarrelSpd < 75.0f) {
            this.rotBarrelSpd += (float)(25 + rand.nextInt(3));
            if (this.rotBarrelSpd > 74.0f) {
                this.rotBarrelSpd = 74.0f;
            }
        }
        this.prevRotBarrel = this.rotBarrel;
        this.rotBarrel += this.rotBarrelSpd;
        if (this.rotBarrel >= 360.0f) {
            this.rotBarrel -= 360.0f;
            this.prevRotBarrel -= 360.0f;
        }
        if (this.rotBarrelSpd > 0.0f) {
            this.rotBarrelSpd -= 1.48f;
            if (this.rotBarrelSpd < 0.0f) {
                this.rotBarrelSpd = 0.0f;
            }
        }
    }

    public void updateWeapon(Entity shooter, boolean isUsed, int index) {
        MCH_WeaponBase crtWpn = this.getWeapon(index);
        if (isUsed && shooter.field_70170_p.field_72995_K && crtWpn != null && crtWpn.cartridge != null) {
            Vec3d v = crtWpn.getShotPos(shooter);
            float yaw = shooter.field_70177_z;
            float pitch = shooter.field_70125_A;
            if (!(shooter instanceof MCH_EntityVehicle) || shooter.func_184207_aI()) {
                // empty if block
            }
            MCH_EntityCartridge.spawnCartridge(shooter.field_70170_p, crtWpn.cartridge, shooter.field_70165_t + v.field_72450_a, shooter.field_70163_u + v.field_72448_b, shooter.field_70161_v + v.field_72449_c, shooter.field_70159_w, shooter.field_70181_x, shooter.field_70179_y, yaw + this.rotationYaw, pitch + this.rotationPitch);
        }
        if (index < this.recoilBuf.length) {
            Recoil r = this.recoilBuf[index];
            r.prevRecoilBuf = r.recoilBuf;
            if (isUsed && r.recoilBufCount <= 0) {
                r.recoilBufCount = r.recoilBufCountMax;
            }
            if (r.recoilBufCount > 0) {
                if (r.recoilBufCountMax <= 1) {
                    r.recoilBuf = 1.0f;
                } else if (r.recoilBufCountMax == 2) {
                    r.recoilBuf = r.recoilBufCount == 2 ? 1.0f : 0.6f;
                } else {
                    if (r.recoilBufCount > r.recoilBufCountMax / 2) {
                        r.recoilBufCount -= r.recoilBufCountSpeed;
                    }
                    float rb = r.recoilBufCount / r.recoilBufCountMax;
                    r.recoilBuf = MathHelper.func_76126_a((float)(rb * (float)Math.PI));
                }
                --r.recoilBufCount;
            } else {
                r.recoilBuf = 0.0f;
            }
        }
    }

    public boolean use(MCH_WeaponParam prm) {
        MCH_WeaponBase crtWpn = this.getCurrentWeapon();
        if (crtWpn != null && crtWpn.getInfo() != null) {
            MCH_WeaponInfo info = crtWpn.getInfo();
            if (!(this.getAmmoNumMax() > 0 && this.getAmmoNum() <= 0 || info.maxHeatCount > 0 && this.currentHeat >= info.maxHeatCount)) {
                crtWpn.canPlaySound = this.soundWait == 0;
                prm.rotYaw = prm.entity != null ? prm.entity.field_70177_z : 0.0f;
                prm.rotPitch = prm.entity != null ? prm.entity.field_70125_A : 0.0f;
                prm.rotYaw += this.rotationYaw + crtWpn.fixRotationYaw;
                prm.rotPitch += this.rotationPitch + crtWpn.fixRotationPitch;
                if (info.accuracy > 0.0f) {
                    prm.rotYaw += (rand.nextFloat() - 0.5f) * info.accuracy;
                    prm.rotPitch += (rand.nextFloat() - 0.5f) * info.accuracy;
                }
                prm.rotYaw = MathHelper.func_76142_g((float)prm.rotYaw);
                prm.rotPitch = MathHelper.func_76142_g((float)prm.rotPitch);
                if (crtWpn.use(prm)) {
                    if (info.maxHeatCount > 0) {
                        this.cooldownSpeed = 1;
                        this.currentHeat += crtWpn.heatCount;
                        if (this.currentHeat >= info.maxHeatCount) {
                            this.currentHeat += 30;
                        }
                    }
                    if (info.soundDelay > 0 && this.soundWait == 0) {
                        this.soundWait = info.soundDelay;
                    }
                    this.lastUsedOptionParameter1 = crtWpn.optionParameter1;
                    this.lastUsedOptionParameter2 = crtWpn.optionParameter2;
                    int n = this.lastUsedCount[this.currentWeaponIndex] = crtWpn.interval > 0 ? crtWpn.interval : -crtWpn.interval;
                    if (crtWpn.isCooldownCountReloadTime() && crtWpn.getReloadCount() - 10 > this.lastUsedCount[this.currentWeaponIndex]) {
                        this.lastUsedCount[this.currentWeaponIndex] = crtWpn.getReloadCount() - 10;
                    }
                    this.currentWeaponIndex = (this.currentWeaponIndex + 1) % this.weapons.length;
                    this.countWait = prm.user instanceof EntityPlayer ? crtWpn.interval : crtWpn.delayedInterval;
                    this.countReloadWait = 0;
                    if (this.getAmmoNum() > 0) {
                        this.setAmmoNum(this.getAmmoNum() - 1);
                    }
                    if (this.getAmmoNum() <= 0) {
                        if (prm.isInfinity && this.getRestAllAmmoNum() < this.getAmmoNumMax()) {
                            this.setRestAllAmmoNum(this.getAmmoNumMax());
                        }
                        this.reload();
                        prm.reload = true;
                        if (prm.user instanceof MCH_EntityGunner) {
                            this.countWait += (this.countWait >= 0 ? 1 : -1) * crtWpn.getReloadCount();
                        }
                    }
                    prm.result = true;
                }
            }
        }
        return prm.result;
    }

    public void waitAndReloadByOther(boolean reload) {
        MCH_WeaponBase crtWpn = this.getCurrentWeapon();
        if (crtWpn != null && crtWpn.getInfo() != null) {
            this.countWait = crtWpn.interval;
            this.countReloadWait = 0;
            if (reload && this.getAmmoNumMax() > 0 && crtWpn.getReloadCount() > 0) {
                this.countReloadWait = crtWpn.getReloadCount();
                if (!crtWpn.worldObj.field_72995_K) {
                    this.countReloadWait -= 20;
                    if (this.countReloadWait <= 0) {
                        this.countReloadWait = 1;
                    }
                }
            }
        }
    }

    public int getLastUsedOptionParameter1() {
        return this.lastUsedOptionParameter1;
    }

    public int getLastUsedOptionParameter2() {
        return this.lastUsedOptionParameter2;
    }

    public MCH_WeaponBase getFirstWeapon() {
        return this.getWeapon(0);
    }

    public int getCurrentWeaponIndex() {
        return this.currentWeaponIndex;
    }

    public MCH_WeaponBase getCurrentWeapon() {
        return this.getWeapon(this.currentWeaponIndex);
    }

    public MCH_WeaponBase getWeapon(int idx) {
        if (this.weapons != null && this.weapons.length > 0 && idx < this.weapons.length) {
            return this.weapons[idx];
        }
        return null;
    }

    public int getWeaponNum() {
        return this.weapons != null ? this.weapons.length : 0;
    }

    public MCH_WeaponInfo getInfo() {
        return this.getFirstWeapon().getInfo();
    }

    public double getLandInDistance(MCH_WeaponParam prm) {
        double ret = -1.0;
        MCH_WeaponBase crtWpn = this.getCurrentWeapon();
        if (crtWpn != null && crtWpn.getInfo() != null) {
            prm.rotYaw = prm.entity != null ? prm.entity.field_70177_z : 0.0f;
            prm.rotPitch = prm.entity != null ? prm.entity.field_70125_A : 0.0f;
            prm.rotYaw += this.rotationYaw + crtWpn.fixRotationYaw;
            prm.rotPitch += this.rotationPitch + crtWpn.fixRotationPitch;
            prm.rotYaw = MathHelper.func_76142_g((float)prm.rotYaw);
            prm.rotPitch = MathHelper.func_76142_g((float)prm.rotPitch);
            return crtWpn.getLandInDistance(prm);
        }
        return ret;
    }

    public class Recoil {
        public int recoilBufCount;
        public final int recoilBufCountMax;
        public final int recoilBufCountSpeed;
        public float recoilBuf;
        public float prevRecoilBuf;

        public Recoil(MCH_WeaponSet paramMCH_WeaponSet, int max, int spd) {
            this.recoilBufCountMax = max;
            this.recoilBufCountSpeed = spd;
        }
    }
}

