/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 */
package mcheli.wrapper;

import javax.annotation.Nullable;
import mcheli.MCH_MOD;
import mcheli.wrapper.W_Entity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public class W_Lib {
    public static boolean isEntityLivingBase(Entity entity) {
        return entity instanceof EntityLivingBase;
    }

    public static EntityLivingBase castEntityLivingBase(Object entity) {
        return (EntityLivingBase)entity;
    }

    public static Class<EntityLivingBase> getEntityLivingBaseClass() {
        return EntityLivingBase.class;
    }

    public static double getEntityMoveDist(@Nullable Entity entity) {
        if (entity == null) {
            return 0.0;
        }
        return entity instanceof EntityLivingBase ? (double)((EntityLivingBase)entity).field_191988_bg : 0.0;
    }

    public static boolean isClientPlayer(@Nullable Entity entity) {
        if (entity instanceof EntityPlayer && entity.field_70170_p.field_72995_K) {
            return W_Entity.isEqual(MCH_MOD.proxy.getClientPlayer(), entity);
        }
        return false;
    }

    public static boolean isFirstPerson() {
        return MCH_MOD.proxy.isFirstPerson();
    }
}

