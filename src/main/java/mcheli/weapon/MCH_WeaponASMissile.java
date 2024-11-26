/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.RayTraceResult
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.MCH_Lib;
import mcheli.weapon.MCH_EntityASMissile;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import mcheli.wrapper.W_MovingObjectPosition;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponASMissile
extends MCH_WeaponBase {
    public MCH_WeaponASMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.acceleration = 3.0f;
        this.explosionPower = 9;
        this.power = 40;
        this.interval = 65186;
        if (w.field_72995_K) {
            this.interval -= 10;
        }
    }

    @Override
    public boolean isCooldownCountReloadTime() {
        return true;
    }

    @Override
    public void update(int countWait) {
        super.update(countWait);
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        float yaw = prm.user.field_70177_z;
        float pitch = prm.user.field_70125_A;
        double tX = -MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tZ = MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tY = -MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI));
        double dist = MathHelper.func_76133_a((double)(tX * tX + tY * tY + tZ * tZ));
        if (this.worldObj.field_72995_K) {
            tX = tX * 200.0 / dist;
            tY = tY * 200.0 / dist;
            tZ = tZ * 200.0 / dist;
        } else {
            tX = tX * 250.0 / dist;
            tY = tY * 250.0 / dist;
            tZ = tZ * 250.0 / dist;
        }
        Vec3d src = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.field_70165_t, prm.entity.field_70163_u + 1.62, prm.entity.field_70161_v);
        Vec3d dst = W_WorldFunc.getWorldVec3(this.worldObj, prm.entity.field_70165_t + tX, prm.entity.field_70163_u + 1.62 + tY, prm.entity.field_70161_v + tZ);
        RayTraceResult m = W_WorldFunc.clip(this.worldObj, src, dst);
        if (m != null && W_MovingObjectPosition.isHitTypeTile(m) && !MCH_Lib.isBlockInWater(this.worldObj, m.func_178782_a().func_177958_n(), m.func_178782_a().func_177956_o(), m.func_178782_a().func_177952_p())) {
            if (!this.worldObj.field_72995_K) {
                MCH_EntityASMissile e = new MCH_EntityASMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, this.acceleration);
                e.setName(this.name);
                e.setParameterFromWeapon(this, prm.entity, prm.user);
                e.targetPosX = m.field_72307_f.field_72450_a;
                e.targetPosY = m.field_72307_f.field_72448_b;
                e.targetPosZ = m.field_72307_f.field_72449_c;
                this.worldObj.func_72838_d((Entity)e);
                this.playSound(prm.entity);
            }
            return true;
        }
        return false;
    }
}

