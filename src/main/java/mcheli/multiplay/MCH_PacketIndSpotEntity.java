/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.EntityLivingBase
 */
package mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.EntityLivingBase;

public class MCH_PacketIndSpotEntity
extends MCH_Packet {
    public int targetFilter = -1;

    @Override
    public int getMessageID() {
        return 0x20000900;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.targetFilter = data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.targetFilter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void send(EntityLivingBase spoter, int targetFilter) {
        MCH_PacketIndSpotEntity s = new MCH_PacketIndSpotEntity();
        s.targetFilter = targetFilter;
        W_Network.sendToServer(s);
    }
}

