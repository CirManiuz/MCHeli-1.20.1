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

import mcheli.weapon.MCH_EntityATMissile;
import mcheli.weapon.MCH_WeaponEntitySeeker;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponATMissile
extends MCH_WeaponEntitySeeker {
    public MCH_WeaponATMissile(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.power = 32;
        this.acceleration = 2.0f;
        this.explosionPower = 4;
        this.interval = -100;
        if (w.field_72995_K) {
            this.interval -= 10;
        }
        this.numMode = 2;
        this.guidanceSystem.canLockOnGround = true;
        this.guidanceSystem.ridableOnly = wi.ridableOnly;
    }

    @Override
    public boolean isCooldownCountReloadTime() {
        return true;
    }

    @Override
    public String getName() {
        String opt = "";
        if (this.getCurrentMode() == 1) {
            opt = " [TA]";
        }
        return super.getName() + opt;
    }

    @Override
    public void update(int countWait) {
        super.update(countWait);
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        if (this.worldObj.field_72995_K) {
            return this.shotClient(prm.entity, prm.user);
        }
        return this.shotServer(prm);
    }

    protected boolean shotClient(Entity entity, Entity user) {
        boolean result = false;
        if (this.guidanceSystem.lock(user) && this.guidanceSystem.lastLockEntity != null) {
            result = true;
            this.optionParameter1 = W_Entity.getEntityId(this.guidanceSystem.lastLockEntity);
        }
        this.optionParameter2 = this.getCurrentMode();
        return result;
    }

    protected boolean shotServer(MCH_WeaponParam prm) {
        Entity tgtEnt = null;
        tgtEnt = prm.user.field_70170_p.func_73045_a(prm.option1);
        if (tgtEnt == null || tgtEnt.field_70128_L) {
            return false;
        }
        float yaw = prm.user.field_70177_z + this.fixRotationYaw;
        float pitch = prm.entity.field_70125_A + this.fixRotationPitch;
        double tX = -MathHelper.func_76126_a((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tZ = MathHelper.func_76134_b((float)(yaw / 180.0f * (float)Math.PI)) * MathHelper.func_76134_b((float)(pitch / 180.0f * (float)Math.PI));
        double tY = -MathHelper.func_76126_a((float)(pitch / 180.0f * (float)Math.PI));
        MCH_EntityATMissile e = new MCH_EntityATMissile(this.worldObj, prm.posX, prm.posY, prm.posZ, tX, tY, tZ, yaw, pitch, this.acceleration);
        e.setName(this.name);
        e.setParameterFromWeapon(this, prm.entity, prm.user);
        e.setTargetEntity(tgtEnt);
        e.guidanceType = prm.option2;
        this.worldObj.func_72838_d((Entity)e);
        this.playSound(prm.entity);
        return true;
    }
}

