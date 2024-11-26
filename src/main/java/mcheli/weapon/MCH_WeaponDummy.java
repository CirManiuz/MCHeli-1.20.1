/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.weapon;

import mcheli.__helper.MCH_Utils;
import mcheli.weapon.MCH_WeaponBase;
import mcheli.weapon.MCH_WeaponInfo;
import mcheli.weapon.MCH_WeaponParam;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MCH_WeaponDummy
extends MCH_WeaponBase {
    static final MCH_WeaponInfo dummy = new MCH_WeaponInfo(MCH_Utils.buildinAddon("none"), "none");

    public int getUseInterval() {
        return 0;
    }

    public MCH_WeaponDummy(World w, Vec3d v, float yaw, float pitch, String nm, MCH_WeaponInfo wi) {
        super(w, v, yaw, pitch, !nm.isEmpty() ? nm : "none", wi != null ? wi : dummy);
    }

    @Override
    public boolean shot(MCH_WeaponParam prm) {
        return false;
    }
}

