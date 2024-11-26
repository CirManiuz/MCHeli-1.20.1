/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.AxisAlignedBB
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_Lib;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_DefaultBulletModels;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_EntityBullet
extends MCH_EntityBaseBullet {
    public MCH_EntityBullet(World par1World) {
        super(par1World);
    }

    public MCH_EntityBullet(World par1World, double pX, double pY, double pZ, double targetX, double targetY, double targetZ, float yaw, float pitch, double acceleration) {
        super(par1World, pX, pY, pZ, targetX, targetY, targetZ, yaw, pitch, acceleration);
    }

    @Override
    public void func_70071_h_() {
        float pDist;
        super.func_70071_h_();
        if (!this.field_70128_L && !this.field_70170_p.field_72995_K && this.getCountOnUpdate() > 1 && this.getInfo() != null && this.explosionPower > 0 && (double)(pDist = this.getInfo().proximityFuseDist) > 0.1) {
            float rng = (pDist += 1.0f) + MathHelper.func_76135_e((float)this.getInfo().acceleration);
            List list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72314_b((double)rng, (double)rng, (double)rng));
            for (int i = 0; i < list.size(); ++i) {
                Entity entity1 = (Entity)list.get(i);
                if (!this.canBeCollidedEntity(entity1) || !(entity1.func_70068_e((Entity)this) < (double)(pDist * pDist))) continue;
                MCH_Lib.DbgLog(this.field_70170_p, "MCH_EntityBullet.onUpdate:proximityFuse:" + entity1, new Object[0]);
                this.field_70165_t = (entity1.field_70165_t + this.field_70165_t) / 2.0;
                this.field_70163_u = (entity1.field_70163_u + this.field_70163_u) / 2.0;
                this.field_70161_v = (entity1.field_70161_v + this.field_70161_v) / 2.0;
                RayTraceResult mop = W_MovingObjectPosition.newMOP((int)this.field_70165_t, (int)this.field_70163_u, (int)this.field_70161_v, 0, W_WorldFunc.getWorldVec3EntityPos(this), false);
                this.onImpact(mop, 1.0f);
                break;
            }
        }
    }

    @Override
    protected void onUpdateCollided() {
        double mx = this.field_70159_w * this.accelerationFactor;
        double my = this.field_70181_x * this.accelerationFactor;
        double mz = this.field_70179_y * this.accelerationFactor;
        float damageFactor = 1.0f;
        RayTraceResult m = null;
        for (int i = 0; i < 5; ++i) {
            Block block;
            Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
            Vec3d vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + mx, this.field_70163_u + my, this.field_70161_v + mz);
            m = W_WorldFunc.clip(this.field_70170_p, vec3, vec31);
            boolean continueClip = false;
            if (this.shootingEntity != null && W_MovingObjectPosition.isHitTypeTile(m) && MCH_Config.bulletBreakableBlocks.contains(block = W_WorldFunc.getBlock(this.field_70170_p, m.func_178782_a()))) {
                W_WorldFunc.destroyBlock(this.field_70170_p, m.func_178782_a(), true);
                continueClip = true;
            }
            if (!continueClip) break;
        }
        Vec3d vec3 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v);
        Vec3d vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, this.field_70165_t + mx, this.field_70163_u + my, this.field_70161_v + mz);
        if (this.getInfo().delayFuse > 0) {
            if (m != null) {
                this.boundBullet(m.field_178784_b);
                if (this.delayFuse == 0) {
                    this.delayFuse = this.getInfo().delayFuse;
                }
            }
            return;
        }
        if (m != null) {
            vec31 = W_WorldFunc.getWorldVec3(this.field_70170_p, m.field_72307_f.field_72450_a, m.field_72307_f.field_72448_b, m.field_72307_f.field_72449_c);
        }
        Entity entity = null;
        List list = this.field_70170_p.func_72839_b((Entity)this, this.func_174813_aQ().func_72321_a(mx, my, mz).func_72314_b(21.0, 21.0, 21.0));
        double d0 = 0.0;
        for (int j = 0; j < list.size(); ++j) {
            double d1;
            Entity entity1 = (Entity)list.get(j);
            if (!this.canBeCollidedEntity(entity1)) continue;
            float f = 0.3f;
            AxisAlignedBB axisalignedbb = entity1.func_174813_aQ().func_72314_b((double)f, (double)f, (double)f);
            RayTraceResult m1 = axisalignedbb.func_72327_a(vec3, vec31);
            if (m1 == null || !((d1 = vec3.func_72438_d(m1.field_72307_f)) < d0) && d0 != 0.0) continue;
            entity = entity1;
            d0 = d1;
        }
        if (entity != null) {
            m = new RayTraceResult(entity);
        }
        if (m != null) {
            this.onImpact(m, damageFactor);
        }
    }

    @Override
    public MCH_BulletModel getDefaultBulletModel() {
        return MCH_DefaultBulletModels.Bullet;
    }
}

