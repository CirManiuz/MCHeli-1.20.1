/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class MCH_EntityTorpedo
extends MCH_EntityBaseBullet {
    public double targetPosX;
    public double targetPosY;
    public double targetPosZ;
    public double accelerationInWater = 2.0;

    public MCH_EntityTorpedo(World par1World) {
        super(par1World);
        this.targetPosX = 0.0;
        this.targetPosY = 0.0;
        this.targetPosZ = 0.0;
    }

    public MCH_EntityTorpedo(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (this.getInfo() != null && this.getInfo().isGuidedTorpedo) {
            this.onUpdateGuided();
        } else {
            this.onUpdateNoGuided();
        }
        if (this.func_70090_H() && this.getInfo() != null && !this.getInfo().disableSmoke) {
            this.spawnParticle(this.getInfo().trajectoryParticleName, 3, 5.0f * this.getInfo().smokeSize * 0.5f);
        }
    }

    private void onUpdateNoGuided() {
        if (!this.field_70170_p.field_72995_K && this.func_70090_H()) {
            this.field_70181_x *= (double)0.8f;
            if (this.acceleration < this.accelerationInWater) {
                this.acceleration += 0.1;
            } else if (this.acceleration > this.accelerationInWater + (double)0.2f) {
                this.acceleration -= 0.1;
            }
            double x = this.field_70159_w;
            double y = this.field_70181_x;
            double z = this.field_70179_y;
            double d = MathHelper.func_76133_a((double)(x * x + y * y + z * z));
            this.field_70159_w = x * this.acceleration / d;
            this.field_70181_x = y * this.acceleration / d;
            this.field_70179_y = z * this.acceleration / d;
        }
        if (this.func_70090_H()) {
            double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
            this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
        }
    }

    private void onUpdateGuided() {
        if (!this.field_70170_p.field_72995_K && this.func_70090_H()) {
            if (this.acceleration < this.accelerationInWater) {
                this.acceleration += 0.1;
            } else if (this.acceleration > this.accelerationInWater + (double)0.2f) {
                this.acceleration -= 0.1;
            }
            double x = this.targetPosX - this.field_70165_t;
            double y = this.targetPosY - this.field_70163_u;
            double z = this.targetPosZ - this.field_70161_v;
            double d = MathHelper.func_76133_a((double)(x * x + y * y + z * z));
            this.field_70159_w = x * this.acceleration / d;
            this.field_70181_x = y * this.acceleration / d;
            this.field_70179_y = z * this.acceleration / d;
        }
        if (this.func_70090_H()) {
            double a = (float)Math.atan2(this.field_70179_y, this.field_70159_w);
            this.field_70177_z = (float)(a * 180.0 / Math.PI) - 90.0f;
            double r = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70179_y * this.field_70179_y);
            this.field_70125_A = -((float)(Math.atan2(this.field_70181_x, r) * 180.0 / Math.PI));
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Torpedo;
    }
}

