/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli.aircraft;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Config;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_EntityRenderer;
import mcheli.wrapper.W_Network;

public class MCH_PacketNotifyClientSetting
extends MCH_Packet {
    public boolean dismountAll = true;
    public boolean heliAutoThrottleDown;
    public boolean planeAutoThrottleDown;
    public boolean tankAutoThrottleDown;
    public boolean shaderSupport = false;

    @Override
    public int getMessageID() {
        return 536875072;
    }

    @Override
    public void readData(ByteArrayDataInput di) {
        try {
            byte data = 0;
            data = di.readByte();
            this.dismountAll = this.getBit(data, 0);
            this.heliAutoThrottleDown = this.getBit(data, 1);
            this.planeAutoThrottleDown = this.getBit(data, 2);
            this.tankAutoThrottleDown = this.getBit(data, 3);
            this.shaderSupport = this.getBit(data, 4);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            byte data = 0;
            data = this.setBit(data, 0, this.dismountAll);
            data = this.setBit(data, 1, this.heliAutoThrottleDown);
            data = this.setBit(data, 2, this.planeAutoThrottleDown);
            data = this.setBit(data, 3, this.tankAutoThrottleDown);
            data = this.setBit(data, 4, this.shaderSupport);
            dos.writeByte(data);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send() {
        MCH_PacketNotifyClientSetting s = new MCH_PacketNotifyClientSetting();
        s.dismountAll = MCH_Config.DismountAll.prmBool;
        s.heliAutoThrottleDown = MCH_Config.AutoThrottleDownHeli.prmBool;
        s.planeAutoThrottleDown = MCH_Config.AutoThrottleDownPlane.prmBool;
        s.tankAutoThrottleDown = MCH_Config.AutoThrottleDownTank.prmBool;
        s.shaderSupport = W_EntityRenderer.isShaderSupport();
        W_Network.sendToServer(s);
    }
}

