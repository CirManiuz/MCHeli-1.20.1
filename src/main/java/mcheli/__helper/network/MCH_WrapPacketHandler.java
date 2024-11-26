/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.io.ByteArrayDataInput
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.common.network.simpleimpl.MessageContext
 */
package mcheli.__helper.network;

import com.google.common.io.ByteArrayDataInput;
import mcheli.MCH_Lib;
import mcheli.__helper.network.MCH_WrapPacketData;
import mcheli.wrapper.W_NetworkRegistry;
import mcheli.wrapper.W_PacketDummy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MCH_WrapPacketHandler
implements IMessageHandler<MCH_WrapPacketData, W_PacketDummy> {
    public W_PacketDummy onMessage(MCH_WrapPacketData message, MessageContext ctx) {
        try {
            ByteArrayDataInput data = message.createData();
            if (ctx.side.isClient()) {
                if (MCH_Lib.getClientPlayer() != null) {
                    W_NetworkRegistry.packetHandler.onPacket(data, (EntityPlayer)MCH_Lib.getClientPlayer(), ctx);
                }
            } else {
                W_NetworkRegistry.packetHandler.onPacket(data, (EntityPlayer)ctx.getServerHandler().field_147369_b, ctx);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

