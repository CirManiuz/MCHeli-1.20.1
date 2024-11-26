/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package mcheli.wrapper;

import com.google.common.io.ByteArrayDataInput;
import mcheli.wrapper.IPacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class W_PacketHandler
implements IPacketHandler {
    public void onPacket(ByteArrayDataInput data, EntityPlayer player, MessageContext ctx) {
    }
}

