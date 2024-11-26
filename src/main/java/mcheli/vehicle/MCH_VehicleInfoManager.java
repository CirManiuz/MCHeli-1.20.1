/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.item.Item
 */
package mcheli.vehicle;

import javax.annotation.Nullable;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.__helper.info.ContentRegistries;
import mcheli.aircraft.MCH_AircraftInfoManager;
import mcheli.vehicle.MCH_VehicleInfo;
import net.minecraft.item.Item;

public class MCH_VehicleInfoManager
extends MCH_AircraftInfoManager<MCH_VehicleInfo> {
    private static MCH_VehicleInfoManager instance = new MCH_VehicleInfoManager();

    @Nullable
    public static MCH_VehicleInfo get(String name) {
        return ContentRegistries.vehicle().get(name);
    }

    public static MCH_VehicleInfoManager getInstance() {
        return instance;
    }

    @Override
    public MCH_VehicleInfo newInfo(AddonResourceLocation name, String filepath) {
        return new MCH_VehicleInfo(name, filepath);
    }

    @Nullable
    public static MCH_VehicleInfo getFromItem(Item item) {
        return MCH_VehicleInfoManager.getInstance().getAcInfoFromItem(item);
    }

    @Override
    @Nullable
    public MCH_VehicleInfo getAcInfoFromItem(Item item) {
        return ContentRegistries.vehicle().findFirst(info -> info.item == item);
    }

    @Override
    protected boolean contains(String name) {
        return ContentRegistries.vehicle().contains(name);
    }

    @Override
    protected int size() {
        return ContentRegistries.vehicle().size();
    }
}

