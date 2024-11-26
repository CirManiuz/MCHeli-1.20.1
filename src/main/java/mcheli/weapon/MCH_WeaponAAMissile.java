/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.MathHelper
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_EntityAAMissile;
import mcheli.weapon.MCH_WeaponEntitySeeker;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponAAMissile
extends MCH_WeaponEntitySeeker {
    public MCH_WeaponAAMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.power = 12;
        this.acceleration = 2.5f;
        this.explosionPower = 4;
        this.interval = 5;
        if (w.field_72995_K) {
            this.interval += 5;
        }
        this.guidanceSystem.canLockInAir = true;
        this.guidanceSystem.ridableOnly = wi.ridableOnly;
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
        boolean result = false;
        if (!this.worldObj.field_72995_K) {
            Entity tgtEnt = prm.user.field_70170_p.func_73045_a(prm.option1);
            if (tgtEnt != null && !tgtEnt.field_70128_L) {
                this.playSound(prm.entity);
                float yaw = prm.entity.field_70177_z + this.fixRotationYaw;
                float pitch = prm.entity.field_70125_A + this.fixRotationPitch;
                double tX = -MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
                double tZ = MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
                double tY = -MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI));
                MCH_EntityAAMissile e = new MCH_EntityAAMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, this.acceleration);
                e.setName(this.name);
                e.setParameterFromWeapon(this, prm.entity, prm.user);
                e.setTargetEntity(tgtEnt);
                this.worldObj.func_72838_d((Entity)e);
                result = true;
            }
        } else if (this.guidanceSystem.lock(prm.user) && this.guidanceSystem.lastLockEntity != null) {
            result = true;
            this.optionParameter1 = W_Entity.getEntityId(this.guidanceSystem.lastLockEntity);
        }
        return result;
    }
}

