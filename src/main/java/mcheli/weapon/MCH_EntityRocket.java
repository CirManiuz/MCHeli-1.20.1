/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

public class MCH_EntityRocket
extends MCH_EntityBaseBullet {
    public MCH_EntityRocket(World par1World) {
        super(par1World);
    }

    public MCH_EntityRocket(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        this.onUpdateBomblet();
        if (this.isBomblet <= 0 && this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0f * this.getInfo().smokeSize * 0.5f);
        }
    }

    @Override
    public void sprinkleBomblet() {
        if (!this.field_70170_p.field_72995_K) {
            MCH_EntityRocket e = new MCH_EntityRocket(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70159_w, this.field_70181_x, this.field_70179_y, this.field_70177_z, this.field_70125_A, this.acceleration);
            e.setName(this.func_70005_c_());
            e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
            float MOTION = this.getInfo().bombletDiff;
            e.field_70159_w += ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)MOTION;
            e.field_70181_x += ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)MOTION;
            e.field_70179_y += ((double)this.field_70146_Z.nextFloat() - 0.5) * (double)MOTION;
            e.setBomblet();
            this.field_70170_p.func_72838_d((Entity)e);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Rocket;
    }
}

