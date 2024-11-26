/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.Item
 */
package mcheli.plane;

import java.util.ArrayList;
import java.util.List;
import mcheli.MCH_Config;
import mcheli.MCH_MOD;
import mcheli.__helper.addon.AddonResourceLocation;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.plane.MCP_ItemPlane;
import net.minecraft.item.Item;

public class MCP_PlaneInfo
extends MCH_AircraftInfo {
    public MCP_ItemPlane item = null;
    public List<DrawnPart> nozzles = new ArrayList<DrawnPart>();
    public List<Rotor> rotorList = new ArrayList<Rotor>();
    public List<Wing> wingList = new ArrayList<Wing>();
    public boolean isEnableVtol = false;
    public boolean isDefaultVtol;
    public float vtolYaw = 0.3f;
    public float vtolPitch = 0.2f;
    public boolean isEnableAutoPilot = false;
    public boolean isVariableSweepWing = false;
    public float sweepWingSpeed = this.speed;

    @Override
    public Item getItem() {
        return this.item;
    }

    public MCP_PlaneInfo(AddonResourceLocation location, String path) {
        super(location, path);
    }

    @Override
    public float getDefaultRotorSpeed() {
        return 47.94f;
    }

    public boolean haveNozzle() {
        return this.nozzles.size() > 0;
    }

    public boolean haveRotor() {
        return this.rotorList.size() > 0;
    }

    public boolean haveWing() {
        return this.wingList.size() > 0;
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
            return "plane";
        }
        if (seatId == 1) {
            return "plane";
        }
        return "gunner";
    }

    @Override
    public boolean validate() throws Exception {
        if (this.haveHatch() && this.haveWing()) {
            this.wingList.clear();
            this.hatchList.clear();
        }
        this.speed = (float)((double)this.speed * MCH_Config.AllPlaneSpeed.prmDouble);
        this.sweepWingSpeed = (float)((double)this.sweepWingSpeed * MCH_Config.AllPlaneSpeed.prmDouble);
        return super.validate();
    }

    @Override
    public void loadItemData(String item, String data) {
        super.loadItemData(item, data);
        if (item.compareTo("addpartrotor") == 0) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length >= 6) {
                float m = s.length >= 7 ? this.toFloat(s[6], -180.0f, 180.0f) / 90.0f : 1.0f;
                Rotor e = new Rotor(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), m, "rotor" + this.rotorList.size());
                this.rotorList.add(e);
            }
        } else if (item.compareTo("addblade") == 0) {
            String[] s;
            Rotor r;
            int idx = this.rotorList.size() - 1;
            Rotor rotor = r = this.rotorList.size() > 0 ? this.rotorList.get(idx) : null;
            if (r != null && (s = data.split("\\s*,\\s*")).length == 8) {
                Blade b = new Blade(this, this.toInt(s[0]), this.toInt(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), this.toFloat(s[7]), "blade" + idx);
                r.blades.add(b);
            }
        } else if (item.compareTo("addpartwing") == 0) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length == 7) {
                Wing n = new Wing(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), "wing" + this.wingList.size());
                this.wingList.add(n);
            }
        } else if (item.equalsIgnoreCase("AddPartPylon")) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length >= 7 && this.wingList.size() > 0) {
                Wing w = this.wingList.get(this.wingList.size() - 1);
                if (w.pylonList == null) {
                    w.pylonList = new ArrayList<Pylon>();
                }
                Pylon n = new Pylon(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), this.toFloat(s[6]), w.modelName + "_pylon" + w.pylonList.size());
                w.pylonList.add(n);
            }
        } else if (item.compareTo("addpartnozzle") == 0) {
            String[] s = data.split("\\s*,\\s*");
            if (s.length == 6) {
                DrawnPart n = new DrawnPart(this, this.toFloat(s[0]), this.toFloat(s[1]), this.toFloat(s[2]), this.toFloat(s[3]), this.toFloat(s[4]), this.toFloat(s[5]), "nozzle" + this.nozzles.size());
                this.nozzles.add(n);
            }
        } else if (item.compareTo("variablesweepwing") == 0) {
            this.isVariableSweepWing = this.toBool(data);
        } else if (item.compareTo("sweepwingspeed") == 0) {
            this.sweepWingSpeed = this.toFloat(data, 0.0f, 5.0f);
        } else if (item.compareTo("enablevtol") == 0) {
            this.isEnableVtol = this.toBool(data);
        } else if (item.compareTo("defaultvtol") == 0) {
            this.isDefaultVtol = this.toBool(data);
        } else if (item.compareTo("vtolyaw") == 0) {
            this.vtolYaw = this.toFloat(data, 0.0f, 1.0f);
        } else if (item.compareTo("vtolpitch") == 0) {
            this.vtolPitch = this.toFloat(data, 0.01f, 1.0f);
        } else if (item.compareTo("enableautopilot") == 0) {
            this.isEnableAutoPilot = this.toBool(data);
        }
    }

    @Override
    public String getDirectoryName() {
        return "planes";
    }

    @Override
    public String getKindName() {
        return "plane";
    }

    @Override
    public void onPostReload() {
        MCH_MOD.proxy.registerModelsPlane(this, true);
    }

    public class Wing
    extends DrawnPart {
        public final float maxRotFactor;
        public final float maxRot;
        public List<Pylon> pylonList;

        public Wing(MCP_PlaneInfo paramMCP_PlaneInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0f;
            this.pylonList = null;
        }
    }

    public class Rotor
    extends DrawnPart {
        public List<Blade> blades;
        public final float maxRotFactor;

        public Rotor(MCP_PlaneInfo paramMCP_PlaneInfo, float x, float y, float z, float rx, float ry, float rz, float mrf, String model) {
            super(paramMCP_PlaneInfo, x, y, z, rx, ry, rz, model);
            this.blades = new ArrayList<Blade>();
            this.maxRotFactor = mrf;
        }
    }

    public class Pylon
    extends DrawnPart {
        public final float maxRotFactor;
        public final float maxRot;

        public Pylon(MCP_PlaneInfo paramMCP_PlaneInfo, float px, float py, float pz, float rx, float ry, float rz, float mr, String name) {
            super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
            this.maxRot = mr;
            this.maxRotFactor = this.maxRot / 90.0f;
        }
    }

    public class Blade
    extends DrawnPart {
        public final int numBlade;
        public final int rotBlade;

        public Blade(MCP_PlaneInfo paramMCP_PlaneInfo, int num, int r, float px, float py, float pz, float rx, float ry, float rz, String name) {
            super(paramMCP_PlaneInfo, px, py, pz, rx, ry, rz, name);
            this.numBlade = num;
            this.rotBlade = r;
        }
    }
}

