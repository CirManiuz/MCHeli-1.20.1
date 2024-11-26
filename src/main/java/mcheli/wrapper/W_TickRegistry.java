/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.relauncher.Side
 */
package mcheli.wrapper;

import mcheli.wrapper.W_TickHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

public class W_TickRegistry {
    public static void registerTickHandler(W_TickHandler handler, Side side) {
        MinecraftForge.EVENT_BUS.register((Object)handler);
    }
}

