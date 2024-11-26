/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;

public class MCH_PacketIndOpenScreen
extends MCH_Packet {
    public int guiID = -1;

    @Override
    public int getMessageID() {
        return 0x20000820;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.guiID = data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.guiID);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(int gui_id) {
        if (gui_id < 0) {
            return;
        }
        MCH_PacketIndOpenScreen s = new MCH_PacketIndOpenScreen();
        s.guiID = gui_id;
        W_Network.sendToServer(s);
    }
}

