/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityATMissile
extends MCH_EntityBaseBullet {
    public int guidanceType = 0;

    public MCH_EntityATMissile(World par1World) {
        super(par1World);
    }

    public MCH_EntityATMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.getInfo() != null && !this.getInfo().disableSmoke && this.field_70173_aa >= this.getInfo().trajectoryParticleStartTick) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0f * this.getInfo().smokeSize * 0.5f);
        }
        if (!this.field_70170_p.field_72995_K) {
            if (this.shootingEntity != null && this.targetEntity != null && !this.targetEntity.field_70128_L) {
                if (this.usingFlareOfTarget(this.targetEntity)) {
                    this.func_70106_y();
                    return;
                }
                this.onUpdateMotion();
            } else {
                this.func_70106_y();
            }
        }
        double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
        this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
        double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0 / Math.PI));
    }

    public void onUpdateMotion() {
        double x = this.targetEntity.field_70165_t - this.field_70165_t;
        double y = this.targetEntity.field_70163_u - this.field_70163_u;
        double z = this.targetEntity.field_70161_v - this.field_70161_v;
        double d = x * x + y * y + z * z;
        if (d > 2250000.0 || this.targetEntity.field_70128_L) {
            this.func_70106_y();
        } else if (this.getInfo().proximityFuseDist >= 0.1f && d < (double)this.getInfo().proximityFuseDist) {
            RayTraceResult mop = new RayTraceResult(this.targetEntity);
            mop.field_72308_g = null;
            this.onImpact(mop, 1.0f);
        } else {
            float af;
            int rigidityTime = this.getInfo().rigidityTime;
            float f = af = this.getCountOnUpdate() < rigidityTime + this.getInfo().trajectoryParticleStartTick ? 0.5f : 1.0f;
            if (this.getCountOnUpdate() > rigidityTime) {
                if (this.guidanceType == 1) {
                    if (this.getCountOnUpdate() <= rigidityTime + 20) {
                        this.guidanceToTarget(this.targetEntity.field_70165_t, this.shootingEntity.field_70163_u + 150.0, this.targetEntity.field_70161_v, af);
                    } else if (this.getCountOnUpdate() <= rigidityTime + 30) {
                        this.guidanceToTarget(this.targetEntity.field_70165_t, this.shootingEntity.field_70163_u, this.targetEntity.field_70161_v, af);
                    } else {
                        if (this.getCountOnUpdate() == rigidityTime + 35) {
                            this.setPower((int)((float)this.getPower() * 1.2f));
                            if (this.explosionPower > 0) {
                                ++this.explosionPower;
                            }
                        }
                        this.guidanceToTarget(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u, this.targetEntity.field_70161_v, af);
                    }
                } else {
                    d = MathHelper.func_76133_a((double)d);
                    this.field_70159_w = x * this.acceleration / d * (double)af;
                    this.field_70181_x = y * this.acceleration / d * (double)af;
                    this.field_70179_y = z * this.acceleration / d * (double)af;
                }
            }
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.ATMissile;
    }
}

