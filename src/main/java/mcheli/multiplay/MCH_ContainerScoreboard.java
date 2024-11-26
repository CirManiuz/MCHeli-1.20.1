/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Container
 */
package mcheli.multiplay;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class MCH_ContainerScoreboard
extends Container {
    public final EntityPlayer thePlayer;

    public MCH_ContainerScoreboard(EntityPlayer player) {
        this.thePlayer = player;
    }

    public boolean func_75145_c(EntityPlayer player) {
        return true;
    }
}

