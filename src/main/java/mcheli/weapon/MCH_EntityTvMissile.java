/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MCH_EntityTvMissile
extends MCH_EntityBaseBullet {
    public boolean isSpawnParticle = true;

    public MCH_EntityTvMissile(World par1World) {
        super(par1World);
    }

    public MCH_EntityTvMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.isSpawnParticle && this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0f * this.getInfo().smokeSize * 0.5f);
        }
        if (this.shootingEntity != null) {
            double x = this.field_70165_t - this.shootingEntity.field_70165_t;
            double y = this.field_70163_u - this.shootingEntity.field_70163_u;
            double z = this.field_70161_v - this.shootingEntity.field_70161_v;
            if (x * x + y * y + z * z > 1440000.0) {
                this.func_70106_y();
            }
            if (!this.field_70170_p.field_72995_K && !this.field_70128_L) {
                this.onUpdateMotion();
            }
        } else if (!this.field_70170_p.field_72995_K) {
            this.func_70106_y();
        }
    }

    public void onUpdateMotion() {
        MCH_EntityAircraft ac;
        Entity e = this.shootingEntity;
        if (e != null && !e.field_70128_L && (ac = MCH_EntityAircraft.getAircraft_RiddenOrControl(e)) != null && ac.getTVMissile() == this) {
            float yaw = e.field_70177_z;
            float pitch = e.field_70125_A;
            double tX = -MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
            double tZ = MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
            double tY = -MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI));
            this.setMotion(tX, tY, tZ);
            this.func_70101_b(yaw, pitch);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.ATMissile;
    }
}

