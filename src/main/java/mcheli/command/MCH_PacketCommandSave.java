/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 */
package mcheli.command;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;

public class MCH_PacketCommandSave
extends MCH_Packet {
    public String str = "";

    @Override
    public int getMessageID() {
        return 536873729;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.str = data.readUTF();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeUTF(this.str);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(String cmd) {
        MCH_PacketCommandSave s = new MCH_PacketCommandSave();
        s.str = cmd;
        W_Network.sendToServer(s);
    }
}

