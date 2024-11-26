/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.player.EntityPlayer
 */
package mcheli.multiplay;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.player.EntityPlayer;

public class MCH_PacketIndClient
extends MCH_Packet {
    public int CmdID = -1;
    public String CmdStr;

    @Override
    public int getMessageID() {
        return 0x10000A10;
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

    public static void send(EntityPlayer player, int cmd_id, String str) {
        if (cmd_id <= 0) {
            return;
        }
        MCH_PacketIndClient s = new MCH_PacketIndClient();
        s.CmdID = cmd_id;
        s.CmdStr = str;
        W_Network.sendToPlayer(s, player);
    }
}

