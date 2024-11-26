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
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;

public class MCH_PacketNotifyInfoReloaded
extends MCH_Packet {
    public int type = -1;

    @Override
    public int getMessageID() {
        return 536875063;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.type = data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.type);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendRealodAc() {
        MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
        s.type = 0;
        W_Network.sendToServer(s);
    }

    public static void sendRealodAllWeapon() {
        MCH_PacketNotifyInfoReloaded s = new MCH_PacketNotifyInfoReloaded();
        s.type = 1;
        W_Network.sendToServer(s);
    }
}

