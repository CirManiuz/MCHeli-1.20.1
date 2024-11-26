/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraftforge.fml.common.network.IGuiHandler
 *  net.minecraftforge.fml.common.network.NetworkRegistry
 *  net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.wrapper;

import mcheli.__helper.network.MCH_WrapPacketData;
import mcheli.__helper.network.MCH_WrapPacketHandler;
import mcheli.wrapper.W_Network;
import mcheli.wrapper.W_PacketHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public class W_NetworkRegistry {
    public static W_PacketHandler packetHandler;

    public static void registerChannel(W_PacketHandler handler, String name) {
        packetHandler = handler;
        W_Network.INSTANCE.registerMessage((IMessageHandler)new MCH_WrapPacketHandler(), MCH_WrapPacketData.class, 0, Side.SERVER);
        W_Network.INSTANCE.registerMessage((IMessageHandler)new MCH_WrapPacketHandler(), MCH_WrapPacketData.class, 0, Side.CLIENT);
    }

    public static void handlePacket(EntityPlayer player, byte[] data) {
    }

    public static void registerGuiHandler(Object mod, IGuiHandler handler) {
        NetworkRegistry.INSTANCE.registerGuiHandler(mod, handler);
    }
}

