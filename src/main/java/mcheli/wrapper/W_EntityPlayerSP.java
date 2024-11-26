/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 */
package mcheli.wrapper;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;

public class W_EntityPlayerSP {
    public static void closeScreen(Entity p) {
        if (p instanceof EntityPlayerSP) {
            ((EntityPlayerSP)p).func_71053_j();
        }
    }
}

