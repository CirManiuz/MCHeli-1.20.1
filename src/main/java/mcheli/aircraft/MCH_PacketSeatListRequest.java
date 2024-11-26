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
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Network;

public class MCH_PacketSeatListRequest
extends MCH_Packet {
    public int entityID_AC = -1;

    @Override
    public int getMessageID() {
        return 0x20001010;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_AC = data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_AC);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void requestSeatList(MCH_EntityAircraft ac) {
        MCH_PacketSeatListRequest s = new MCH_PacketSeatListRequest();
        s.entityID_AC = W_Entity.getEntityId(ac);
        W_Network.sendToServer(s);
    }
}

