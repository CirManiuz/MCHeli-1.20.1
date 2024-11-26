/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli.tank;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.aircraft.MCH_PacketPlayerControlBase;

public class MCH_TankPacketPlayerControl
extends MCH_PacketPlayerControlBase {
    public byte switchVtol = (byte)-1;

    @Override
    public int getMessageID() {
        return 0x20100010;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        super.readData(data);
        try {
            this.switchVtol = data.readByte();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        super.writeData(dos);
        try {
            dos.writeByte(this.switchVtol);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

