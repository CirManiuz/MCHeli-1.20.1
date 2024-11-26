/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.entity.player.EntityPlayerMP
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.NetworkRegistry$TargetPoint
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessage
 *  net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
 */
package mcheli.wrapper;

import mcheli.__helper.network.MCH_WrapPacketData;
import mcheli.wrapper.W_PacketBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class W_Network {
    static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("MCHeli_CH");

    public static void sendToServer(W_PacketBase pkt) {
        INSTANCE.sendToServer((IMessage)new MCH_WrapPacketData(pkt));
    }

    public static void sendToPlayer(W_PacketBase pkt, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            INSTANCE.sendTo((IMessage)new MCH_WrapPacketData(pkt), (EntityPlayerMP)player);
        }
    }

    public static void sendToAllAround(W_PacketBase pkt, Entity sender, double renge) {
        NetworkRegistry.TargetPoint t = new NetworkRegistry.TargetPoint(sender.field_71093_bK, sender.field_70165_t, sender.field_70163_u, sender.field_70161_v, renge);
        INSTANCE.sendToAllAround((IMessage)new MCH_WrapPacketData(pkt), t);
    }

    public static void sendToAllPlayers(W_PacketBase pkt) {
        INSTANCE.sendToAll((IMessage)new MCH_WrapPacketData(pkt));
    }
}

