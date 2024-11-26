/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package mcheli.helicopter;

import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_ItemAircraft;
import mcheli.helicopter.MCH_EntityHeli;
import mcheli.helicopter.MCH_HeliInfo;
import mcheli.helicopter.MCH_HeliInfoManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MCH_ItemHeli
extends MCH_ItemAircraft {
    public MCH_ItemHeli(int par1) {
        super(par1);
        this.field_77777_bU = 1;
    }

    @Override
    @Nullable
    public MCH_AircraftInfo getAircraftInfo() {
        return MCH_HeliInfoManager.getFromItem(this);
    }

    @Override
    @Nullable
    public MCH_EntityHeli createAircraft(World world, double x, double y, double z, ItemStack itemStack) {
        MCH_HeliInfo info = MCH_HeliInfoManager.getFromItem(this);
        if (info == null) {
            MCH_Lib.Log(world, "##### MCH_ItemHeli Heli info null %s", this.func_77658_a());
            return null;
        }
        MCH_EntityHeli heli = new MCH_EntityHeli(world);
        heli.func_70107_b(x, y, z);
        heli.field_70169_q = x;
        heli.field_70167_r = y;
        heli.field_70166_s = z;
        heli.camera.setPosition(x, y, z);
        heli.setTypeName(info.name);
        if (!world.field_72995_K) {
            heli.setTextureName(info.getTextureName());
        }
        return heli;
    }
}

