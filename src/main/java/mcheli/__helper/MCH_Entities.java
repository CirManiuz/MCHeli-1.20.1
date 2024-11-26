/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.common.registry.EntityRegistry
 */
package mcheli.__helper;

import mcheli.__helper.MCH_Utils;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class MCH_Entities {
    public static <T extends Entity> void register(Class<T> entityClass, String entityName, int id, Object mod, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
        EntityRegistry.registerModEntity((ResourceLocation)MCH_Utils.suffix(entityName), entityClass, (String)entityName, (int)id, (Object)mod, (int)trackingRange, (int)updateFrequency, (boolean)sendsVelocityUpdates);
    }
}

