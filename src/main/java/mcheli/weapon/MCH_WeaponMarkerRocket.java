/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.MCH_Lib;
import mcheli.weapon.MCH_EntityMarkerRocket;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponMarkerRocket
extends MCH_WeaponBase {
    public MCH_WeaponMarkerRocket(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.acceleration = 3.0f;
        this.explosionPower = 0;
        this.power = 0;
        this.interval = 60;
        if (w.field_72995_K) {
            this.interval += 10;
        }
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        if (!this.worldObj.field_72995_K) {
            this.playSound(prm.entity);
            Vec3d v = MCH_Lib.RotVec3(0.0, 0.0, 1.0, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
            MCH_EntityMarkerRocket e = new MCH_EntityMarkerRocket(this.worldObj, prm.posX, prm.posY, prm.posZ, v.field_72450_a, v.field_72448_b, v.field_72449_c, prm.rotYaw, prm.rotPitch, this.acceleration);
            e.setName(this.name);
            e.setParameterFromWeapon(this, prm.entity, prm.user);
            e.setMarkerStatus(1);
            this.worldObj.func_72838_d((Entity)e);
        } else {
            this.optionParameter1 = this.getCurrentMode();
        }
        return true;
    }
}

