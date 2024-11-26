/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponSmoke
extends MCH_WeaponBase {
    public MCH_WeaponSmoke(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, nm, wi);
        this.power = 0;
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        return false;
    }
}

