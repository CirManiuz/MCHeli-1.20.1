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

public class MCH_PacketIndRotation
extends MCH_Packet {
    public int entityID_Ac = -1;
    public float yaw = 0.0f;
    public float pitch = 0.0f;
    public float roll = 0.0f;
    public boolean rollRev = false;

    @Override
    public int getMessageID() {
        return 536875062;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID_Ac = data.readInt();
            this.yaw = data.readFloat();
            this.pitch = data.readFloat();
            this.roll = data.readFloat();
            this.rollRev = data.readByte() != 0;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID_Ac);
            dos.writeFloat(this.yaw);
            dos.writeFloat(this.pitch);
            dos.writeFloat(this.roll);
            dos.writeByte(this.rollRev ? 1 : 0);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(MCH_EntityAircraft ac) {
        if (ac == null) {
            return;
        }
        MCH_PacketIndRotation s = new MCH_PacketIndRotation();
        s.entityID_Ac = W_Entity.getEntityId(ac);
        s.yaw = ac.getRotYaw();
        s.pitch = ac.getRotPitch();
        s.roll = ac.getRotRoll();
        s.rollRev = ac.aircraftRollRev;
        W_Network.sendToServer(s);
    }
}

