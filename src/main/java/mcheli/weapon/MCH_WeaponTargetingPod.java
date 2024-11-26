/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.multiplay.MCH_Multiplay;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponTargetingPod
extends MCH_WeaponBase {
    public MCH_WeaponTargetingPod(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.interval = -90;
        if (w.field_72995_K) {
            this.interval -= 10;
        }
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        if (!this.worldObj.field_72995_K) {
            MCH_WeaponInfo info = this.getInfo();
            if ((info.target & 0x40) != 0) {
                if (MCH_Multiplay.markPoint((EntityPlayer)prm.user, prm.posX, prm.posY, prm.posZ)) {
                    this.playSound(prm.user);
                } else {
                    this.playSound(prm.user, "ng");
                }
            } else if (MCH_Multiplay.spotEntity((EntityLivingBase)prm.user, (MCH_EntityAircraft)prm.entity, prm.posX, prm.posY, prm.posZ, info.target, info.length, info.markTime, info.angle)) {
                this.playSound(prm.entity);
            } else {
                this.playSound(prm.entity, "ng");
            }
        }
        return true;
    }
}

