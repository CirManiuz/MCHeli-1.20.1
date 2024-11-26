/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityAAMissile
extends MCH_EntityBaseBullet {
    public MCH_EntityAAMissile(World par1World) {
        super(par1World);
        this.targetEntity = null;
    }

    public MCH_EntityAAMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.getCountOnUpdate() > 4 && this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 7.0f * this.getInfo().smokeSize * 0.5f);
        }
        if (!this.field_70170_p.field_72995_K && this.getInfo() != null) {
            if (this.shootingEntity != null && this.targetEntity != null && !this.targetEntity.field_70128_L) {
                double x = this.field_70165_t - this.targetEntity.field_70165_t;
                double y = this.field_70163_u - this.targetEntity.field_70163_u;
                double z = this.field_70161_v - this.targetEntity.field_70161_v;
                double d = x * x + y * y + z * z;
                if (d > 3422500.0) {
                    this.func_70106_y();
                } else if (this.getCountOnUpdate() > this.getInfo().rigidityTime) {
                    if (this.usingFlareOfTarget(this.targetEntity)) {
                        this.func_70106_y();
                        return;
                    }
                    if (this.getInfo().proximityFuseDist >= 0.1f && d < (double)this.getInfo().proximityFuseDist) {
                        RayTraceResult mop = new RayTraceResult(this.targetEntity);
                        this.field_70165_t = (this.targetEntity.field_70165_t + this.field_70165_t) / 2.0;
                        this.field_70163_u = (this.targetEntity.field_70163_u + this.field_70163_u) / 2.0;
                        this.field_70161_v = (this.targetEntity.field_70161_v + this.field_70161_v) / 2.0;
                        this.onImpact(mop, 1.0f);
                    } else {
                        this.guidanceToTarget(this.targetEntity.field_70165_t, this.targetEntity.field_70163_u, this.targetEntity.field_70161_v);
                    }
                }
            } else {
                this.func_70106_y();
            }
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.AAMissile;
    }
}

