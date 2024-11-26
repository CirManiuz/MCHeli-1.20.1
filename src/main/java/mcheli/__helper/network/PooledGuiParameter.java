/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 */
package mcheli.__helper.network;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class PooledGuiParameter {
    private static Entity clientEntity;
    private static Entity serverEntity;

    public static void setEntity(EntityPlayer player, @Nullable Entity target) {
        if (player.field_70170_p.field_72995_K) {
            clientEntity = target;
        } else {
            serverEntity = target;
        }
    }

    @Nullable
    public static Entity getEntity(EntityPlayer player) {
        return player.field_70170_p.field_72995_K ? clientEntity : serverEntity;
    }

    public static void resetEntity(EntityPlayer player) {
        PooledGuiParameter.setEntity(player, null);
    }
}

