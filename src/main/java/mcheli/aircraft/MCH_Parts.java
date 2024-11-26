/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.network.datasync.DataParameter
 *  net.minecraft.network.datasync.EntityDataManager
 */
package mcheli.aircraft;

import mcheli.MCH_Lib;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;

public class MCH_Parts {
    public final Entity parent;
    public final EntityDataManager dataManager;
    public final int shift;
    public final DataParameter<Integer> dataKey;
    public final String partName;
    public float prevRotation = 0.0f;
    public float rotation = 0.0f;
    public float rotationMax = 90.0f;
    public float rotationInv = 1.0f;
    public Sound soundStartSwichOn = new Sound(this);
    public Sound soundEndSwichOn = new Sound(this);
    public Sound soundSwitching = new Sound(this);
    public Sound soundStartSwichOff = new Sound(this);
    public Sound soundEndSwichOff = new Sound(this);
    private boolean status = false;

    public MCH_Parts(Entity parent, int shiftBit, DataParameter<Integer> dataKey, String name) {
        this.parent = parent;
        this.dataManager = parent.func_184212_Q();
        this.shift = shiftBit;
        this.dataKey = dataKey;
        this.status = (this.getDataWatcherValue() & 1 << this.shift) != 0;
        this.partName = name;
    }

    public int getDataWatcherValue() {
        return (Integer)this.dataManager.func_187225_a(this.dataKey);
    }

    public void setStatusServer(boolean stat) {
        this.setStatusServer(stat, true);
    }

    public void setStatusServer(boolean stat, boolean playSound) {
        if (!this.parent.field_70170_p.field_72995_K && this.getStatus() != stat) {
            MCH_Lib.DbgLog(false, "setStatusServer(ID=%d %s :%s -> %s)", this.shift, this.partName, this.getStatus() ? "ON" : "OFF", stat ? "ON" : "OFF");
            this.updateDataWatcher(stat);
            this.playSound(this.soundSwitching);
            if (!stat) {
                this.playSound(this.soundStartSwichOff);
            } else {
                this.playSound(this.soundStartSwichOn);
            }
            this.update();
        }
    }

    protected void updateDataWatcher(boolean stat) {
        int currentStatus = this.getDataWatcherValue();
        int mask = 1 << this.shift;
        if (!stat) {
            this.dataManager.func_187227_b(this.dataKey, (Object)(currentStatus & ~mask));
        } else {
            this.dataManager.func_187227_b(this.dataKey, (Object)(currentStatus | mask));
        }
        this.status = stat;
    }

    public boolean getStatus() {
        return this.status;
    }

    public boolean isOFF() {
        return !this.status && this.rotation <= 0.02f;
    }

    public boolean isON() {
        return this.status && this.rotation >= this.rotationMax - 0.02f;
    }

    public void updateStatusClient(int statFromDataWatcher) {
        if (this.parent.field_70170_p.field_72995_K) {
            this.status = (statFromDataWatcher & 1 << this.shift) != 0;
        }
    }

    public void update() {
        this.prevRotation = this.rotation;
        if (this.getStatus()) {
            if (this.rotation < this.rotationMax) {
                this.rotation += this.rotationInv;
                if (this.rotation >= this.rotationMax) {
                    this.playSound(this.soundEndSwichOn);
                }
            }
        } else if (this.rotation > 0.0f) {
            this.rotation -= this.rotationInv;
            if (this.rotation <= 0.0f) {
                this.playSound(this.soundEndSwichOff);
            }
        }
    }

    public void forceSwitch(boolean onoff) {
        this.updateDataWatcher(onoff);
        this.rotation = this.prevRotation = this.rotationMax;
    }

    public float getFactor() {
        if (this.rotationMax > 0.0f) {
            return this.rotation / this.rotationMax;
        }
        return 0.0f;
    }

    public void playSound(Sound snd) {
        if (!snd.name.isEmpty() && !this.parent.field_70170_p.field_72995_K) {
            W_WorldFunc.MOD_playSoundAtEntity(this.parent, snd.name, snd.volume, snd.pitch);
        }
    }

    public class Sound {
        public String name = "";
        public float volume = 1.0f;
        public float pitch = 1.0f;

        public Sound(MCH_Parts paramMCH_Parts) {
        }

        public void setPrm(String n, float v, float p) {
            this.name = n;
            this.volume = v;
            this.pitch = p;
        }
    }
}

