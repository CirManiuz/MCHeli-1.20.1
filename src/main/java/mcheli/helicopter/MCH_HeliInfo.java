/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package mcheli.helicopter;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_MOD;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.helicopter.MCH_ItemHeli;
import net.minecraft.item.Item;

public class MCH_HeliInfo
extends MCH_AircraftInfo {
    public MCH_ItemHeli item = null;
    public boolean isEnableFoldBlade;
    public List<Rotor> rotorList;

    public MCH_HeliInfo(AddonResourceLocation location, String path) {
        super(location, path);
        this.isEnableGunnerMode = false;
        this.isEnableFoldBlade = false;
        this.rotorList = new ArrayList<Rotor>();
        this.minRotationPitch = -20.0f;
        this.maxRotationPitch = 20.0f;
    }

    @Override
    public boolean validate() throws Exception {
        this.speed = (float)((double)this.speed * MCH_Config.AllHeliSpeed.prmDouble);
        return super.validate();
    }

    @Override
    public float getDefaultSoundRange() {
        return 80.0f;
    }

    @Override
    public float getDefaultRotorSpeed() {
        return 79.99f;
    }

    @Override
    public int getDefaultMaxZoom() {
        return 8;
    }

    @Override
    public Item getItem() {
        return this.item;
    }

    @Override
    public String getDefaultHudName(int seatId) {
        if (seatId <= 0) {
            return "heli";
        }
        if (seatId == 1) {
            return "heli_gnr";
        }
        return "gunner";
    }

    @Override
    public void loadItemData(String item, String data) {
        String[] s;
        super.loadItemData(item, data);
        if (item.compareTo("enablefoldblade") == 0) {
            this.isEnableFoldBlade = this.toBool(data);
        } else if (!(item.compareTo("addrotor") != 0 && item.compareTo("addrotorold") != 0 || (s = data.split("\\s*,\\s*")).length != 8 && s.length != 9)) {
            boolean cfb = s.length == 9 && this.toBool(s[8]);
            Rotor e = new Rotor(this, this.toInt(s[0]), this.toInt(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), this.toFloat(s[7]), "blade" + this.rotorList.size(), cfb, item.compareTo("addrotorold") == 0);
            this.rotorList.add(e);
        }
    }

    @Override
    public String getDirectoryName() {
        return "helicopters";
    }

    @Override
    public String getKindName() {
        return "helicopter";
    }

    @Override
    public void onPostReload() {
        MCH_MOD.proxy.registerModelsHeli(this, true);
    }

    public class Rotor
    extends DrawnPart {
        public final int bladeNum;
        public final int bladeRot;
        public final boolean haveFoldFunc;
        public final boolean oldRenderMethod;

        public Rotor(MCH_HeliInfo paramMCH_HeliInfo, int b, int br, float x, float y, float z, float rx, float ry, float rz, String model, boolean hf, boolean old) {
            super(paramMCH_HeliInfo, x, y, z, rx, ry, rz, model);
            this.bladeNum = b;
            this.bladeRot = br;
            this.haveFoldFunc = hf;
            this.oldRenderMethod = old;
        }
    }
}

