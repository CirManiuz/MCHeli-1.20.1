/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 */
package mcheli;

import com.google.common.io.ByteArrayDataInput;
import java.io.DataOutputStream;
import java.io.IOException;
import mcheli.MCH_Packet;
import mcheli.wrapper.W_Network;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class MCH_PacketNotifyLock
extends MCH_Packet {
    public int entityID = -1;

    @Override
    public int getMessageID() {
        return 0x20000C00;
    }

    @Override
    public void readData(ByteArrayDataInput data) {
        try {
            this.entityID = data.readInt();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void writeData(DataOutputStream dos) {
        try {
            dos.writeInt(this.entityID);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void send(Entity target) {
        if (target != null) {
            MCH_PacketNotifyLock s = new MCH_PacketNotifyLock();
            s.entityID = target.func_145782_y();
            W_Network.sendToServer(s);
        }
    }

    public static void sendToPlayer(EntityPlayer entity) {
        if (entity instanceof EntityPlayerMP) {
            MCH_PacketNotifyLock s = new MCH_PacketNotifyLock();
            W_Network.sendToPlayer(s, entity);
        }
    }
}

