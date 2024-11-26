/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.item.ItemStack
 *  net.minecraft.world.World
 */
package mcheli.vehicle;

import javax.annotation.Nullable;
import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_ItemAircraft;
import mcheli.vehicle.MCH_EntityVehicle;
import mcheli.vehicle.MCH_VehicleInfo;
import mcheli.vehicle.MCH_VehicleInfoManager;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class MCH_ItemVehicle
extends MCH_ItemAircraft {
    public MCH_ItemVehicle(int par1) {
        super(par1);
        this.field_77777_bU = 1;
    }

    @Override
    @Nullable
    public MCH_AircraftInfo getAircraftInfo() {
        return MCH_VehicleInfoManager.getFromItem(this);
    }

    @Override
    @Nullable
    public MCH_EntityVehicle createAircraft(World world, double x, double y, double z, ItemStack item) {
        MCH_VehicleInfo info = MCH_VehicleInfoManager.getFromItem(this);
        if (info == null) {
            MCH_Lib.Log(world, "##### MCH_ItemVehicle Vehicle info null %s", this.func_77658_a());
            return null;
        }
        MCH_EntityVehicle vehicle = new MCH_EntityVehicle(world);
        vehicle.func_70107_b(x, y, z);
        vehicle.field_70169_q = x;
        vehicle.field_70167_r = y;
        vehicle.field_70166_s = z;
        vehicle.camera.setPosition(x, y, z);
        vehicle.setTypeName(info.name);
        if (!world.field_72995_K) {
            vehicle.setTextureName(info.getTextureName());
        }
        return vehicle;
    }
}

