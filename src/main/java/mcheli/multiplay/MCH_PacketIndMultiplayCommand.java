/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;

public class MCH_PacketIndMultiplayCommand
extends MCH_Packet {
    public int CmdID = -1;
    public String CmdStr;

    @Override
    public int getMessageID() {
        return 0x20000880;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.CmdID = data.readInt();
            this.CmdStr = data.readUTF();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.CmdID);
            dos.writeUTF(this.CmdStr);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(int cmd_id, String str) {
        if (cmd_id <= 0) {
            return;
        }
        MCH_PacketIndMultiplayCommand s = new MCH_PacketIndMultiplayCommand();
        s.CmdID = cmd_id;
        s.CmdStr = str;
        W_Network.sendToServer(s);
    }
}

