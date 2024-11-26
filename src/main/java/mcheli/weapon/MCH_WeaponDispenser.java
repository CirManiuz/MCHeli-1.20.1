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
import mcheli.weapon.MCH_EntityDispensedItem;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponDispenser
extends MCH_WeaponBase {
    public MCH_WeaponDispenser(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.acceleration = 0.5f;
        this.explosionPower = 0;
        this.power = 0;
        this.interval = -90;
        if (w.field_72995_K) {
            this.interval -= 10;
        }
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        if (!this.worldObj.field_72995_K) {
            this.playSound(prm.entity);
            Vec3d v = MCH_Lib.RotVec3(0.0, 0.0, 1.0, -prm.rotYaw, -prm.rotPitch, -prm.rotRoll);
            MCH_EntityDispensedItem e = new MCH_EntityDispensedItem(this.worldObj, prm.posX, prm.posY, prm.posZ, v.field_72450_a, v.field_72448_b, v.field_72449_c, prm.rotYaw, prm.rotPitch, this.acceleration);
            e.setName(this.name);
            e.setParameterFromWeapon(this, prm.entity, prm.user);
            e.field_70159_w = prm.entity.field_70159_w + e.field_70159_w * 0.5;
            e.field_70181_x = prm.entity.field_70181_x + e.field_70181_x * 0.5;
            e.field_70179_y = prm.entity.field_70179_y + e.field_70179_y * 0.5;
            e.field_70165_t += e.field_70159_w * 0.5;
            e.field_70163_u += e.field_70181_x * 0.5;
            e.field_70161_v += e.field_70179_y * 0.5;
            this.worldObj.func_72838_d((Entity)e);
        }
        return true;
    }
}

