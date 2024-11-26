/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import java.util.List;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_Lib;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_EntityBomb
extends MCH_EntityBaseBullet {
    public MCH_EntityBomb(World par1World) {
        super(par1World);
    }

    public MCH_EntityBomb(World par1World, double posX, double posY, double posZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, posX, posY, posZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        super.func_70071_h_();
        if (!this.field_70170_p.field_72995_K && this.getInfo() != null) {
            List list;
            float dist;
            this.field_70159_w *= 0.999;
            this.field_70179_y *= 0.999;
            if (this.func_70090_H()) {
                this.field_70159_w *= (double)this.getInfo().velocityInWater;
                this.field_70181_x *= (double)this.getInfo().velocityInWater;
                this.field_70179_y *= (double)this.getInfo().velocityInWater;
            }
            if ((dist = this.getInfo().proximityFuseDist) > 0.1f && this.getCountOnUpdate() % 10 == 0 && (list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72314_b((double)dist, (double)dist, (double)dist))) != null) {
                for (int i = 0; i < list.size(); ++i) {
                    Entity entity = (Entity)list.get(i);
                    if (!W_Lib.isEntityLivingBase(entity) || !this.canBeCollidedEntity(entity)) continue;
                    RayTraceResult m = new RayTraceResult(new Vec3d(this.field_70165_t, this.field_70163_u, this.field_70161_v), EnumFacing.DOWN, new BlockPos(this.field_70165_t + 0.5, this.field_70163_u + 0.5, this.field_70161_v + 0.5));
                    this.onImpact(m, 1.0f);
                    break;
                }
            }
        }
        this.onUpdateBomblet();
    }

    @Override
    public void sprinkleBomblet() {
        if (!this.field_70170_p.field_72995_K) {
            MCH_EntityBomb e = new MCH_EntityBomb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70159_w, this.field_70181_x, this.field_70179_y, this.field_70146_Z.nextInt(360), 0.0f, this.acceleration);
            e.setParameterFromWeapon(this, this.shootingAircraft, this.shootingEntity);
            e.setName(this.func_70005_c_());
            float RANDOM = this.getInfo().bombletDiff;
            e.field_70159_w = this.field_70159_w * 1.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.field_70181_x = this.field_70181_x * 1.0 / 2.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM / 2.0f);
            e.field_70179_y = this.field_70179_y * 1.0 + (double)((this.field_70146_Z.nextFloat() - 0.5f) * RANDOM);
            e.setBomblet();
            this.field_70170_p.func_72838_d((Entity)e);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Bomb;
    }
}

