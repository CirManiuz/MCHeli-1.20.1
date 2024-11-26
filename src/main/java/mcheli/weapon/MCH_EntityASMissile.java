/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class MCH_EntityASMissile
extends MCH_EntityBaseBullet {
    public double targetPosX;
    public double targetPosY;
    public double targetPosZ;

    public MCH_EntityASMissile(World par1World) {
        super(par1World);
        this.targetPosX = 0.0;
        this.targetPosY = 0.0;
        this.targetPosZ = 0.0;
    }

    public MCH_EntityASMissile(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public float getGravity() {
        if (this.getBomblet() == 1) {
            return -0.03f;
        }
        return super.getGravity();
    }

    @Override
    public float getGravityInWater() {
        if (this.getBomblet() == 1) {
            return -0.03f;
        }
        return super.getGravityInWater();
    }

    @Override
    public void func_70071_h_() {
        Block block;
        super.func_70071_h_();
        if (this.getInfo() != null && !this.getInfo().disableSmoke && this.getBomblet() == 0) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 10.0f * this.getInfo().smokeSize * 0.5f);
        }
        if (this.getInfo() != null && !this.field_70170_p.field_72995_K && this.isBomblet != 1 && (block = W_WorldFunc.getBlock(this.field_70170_p, (int)this.targetPosX, (int)this.targetPosY, (int)this.targetPosZ)) != null && block.func_149703_v()) {
            double dist = this.func_70011_f(this.targetPosX, this.targetPosY, this.targetPosZ);
            if (dist < (double)this.getInfo().proximityFuseDist) {
                if (this.getInfo().bomblet > 0) {
                    for (int i = 0; i < this.getInfo().bomblet; ++i) {
                        this.sprinkleBomblet();
                    }
                } else {
                    RayTraceResult mop = new RayTraceResult((Entity)this);
                    this.onImpact(mop, 1.0f);
                }
                this.func_70106_y();
            } else if ((double)this.getGravity() == 0.0) {
                double up = 0.0;
                if (this.getCountOnUpdate() < 10) {
                    up = 20.0;
                }
                double x = this.targetPosX - this.field_70165_t;
                double y = this.targetPosY + up - this.field_70163_u;
                double z = this.targetPosZ - this.field_70161_v;
                double d = MathHelper.func_76133_a((double)(x * x + y * y + z * z));
                this.field_70159_w = x * this.acceleration / d;
                this.field_70181_x = y * this.acceleration / d;
                this.field_70179_y = z * this.acceleration / d;
            } else {
                double x = this.targetPosX - this.field_70165_t;
                double y = this.targetPosY - this.field_70163_u;
                double z = this.targetPosZ - this.field_70161_v;
                double d = MathHelper.func_76133_a((double)(x * x + (y *= 0.3) * y + z * z));
                this.field_70159_w = x * this.acceleration / d;
                this.field_70179_y = z * this.acceleration / d;
            }
        }
        double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
        this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
        double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
        this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0 / Math.PI));
        this.onUpdateBomblet();
    }

    @Override
    public void sprinkleBomblet() {
        if (!this.field_70170_p.field_72995_K) {
            MCH_EntityASMissile e = new MCH_EntityASMissile(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70159_w, this.field_70181_x, this.field_70179_y, this.field_70146_Z.nextInt(360), 0.0f, this.acceleration);
            e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
            e.setName(this.func_70005_c_());
            float RANDOM = this.getInfo().bombletDiff;
            e.field_70159_w = this.field_70159_w * 0.5 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.field_70181_x = this.field_70181_x * 0.5 / 2.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM / 2.0f);
            e.field_70179_y = this.field_70179_y * 0.5 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.setBomblet();
            this.field_70170_p.func_72838_d((Entity)e);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.ASMissile;
    }
}

