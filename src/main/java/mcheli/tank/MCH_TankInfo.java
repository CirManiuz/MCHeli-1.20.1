/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 *  net.minecraft.util.math.Vec3d
 */
package mcheli.tank;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_MOD;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.tank.MCH_ItemTank;
import net.minecraft.item.Item;
import net.minecraft.util.math.Vec3d;

public class MCH_TankInfo
extends MCH_AircraftInfo {
    public MCH_ItemTank item = null;
    public int weightType = 0;
    public float weightedCenterZ = 0.0f;

    @Override
    public Item getItem() {
        return this.item;
    }

    public MCH_TankInfo(AddonResourceLocation location, String path) {
        super(location, path);
    }

    @Override
    public List<Wheel> getDefaultWheelList() {
        ArrayList<Wheel> list = new ArrayList<Wheel>();
        list.add(new Wheel(this, new Vec3d(1.5, -0.24, 2.0)));
        list.add(new Wheel(this, new Vec3d(1.5, -0.24, -2.0)));
        return list;
    }

    @Override
    public float getDefaultSoundRange() {
        return 50.0f;
    }

    @Override
    public float getDefaultRotorSpeed() {
        return 47.94f;
    }

    private float getDefaultStepHeight() {
        return 0.6f;
    }

    @Override
    public float getMaxSpeed() {
        return 1.8f;
    }

    @Override
    public int getDefaultMaxZoom() {
        return 8;
    }

    @Override
    public String getDefaultHudName(int seatId) {
        if (seatId <= 0) {
            return "tank";
        }
        if (seatId == 1) {
            return "tank";
        }
        return "gunner";
    }

    @Override
    public boolean validate() throws Exception {
        this.speed = (float)((double)this.speed * MCH_Config.AllTankSpeed.prmDouble);
        return super.validate();
    }

    @Override
    public void loadItemData(String item, String data) {
        super.loadItemData(item, data);
        if (item.equalsIgnoreCase("WeightType")) {
            this.weightType = (data = data.toLowerCase()).equals("car") ? 1 : (data.equals("tank") ? 2 : 0);
        } else if (item.equalsIgnoreCase("WeightedCenterZ")) {
            this.weightedCenterZ = this.toFloat(data, -1000.0f, 1000.0f);
        }
    }

    @Override
    public String getDirectoryName() {
        return "tanks";
    }

    @Override
    public String getKindName() {
        return "tank";
    }

    @Override
    public void onPostReload() {
        MCH_MOD.proxy.registerModelsTank(this, true);
    }
}

