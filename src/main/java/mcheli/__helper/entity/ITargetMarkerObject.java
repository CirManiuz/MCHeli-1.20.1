/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.entity.Entity
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.entity;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface ITargetMarkerObject {
    public double getX();

    public double getY();

    public double getZ();

    @Nullable
    default public Entity getEntity() {
        return null;
    }

    public static ITargetMarkerObject fromEntity(Entity target) {
        return new EntityWrapper(target);
    }

    @SideOnly(value=Side.CLIENT)
    public static class EntityWrapper
    implements ITargetMarkerObject {
        private final Entity target;

        public EntityWrapper(Entity entity) {
            this.target = entity;
        }

        @Override
        public double getX() {
            return this.target.field_70165_t;
        }

        @Override
        public double getY() {
            return this.target.field_70163_u;
        }

        @Override
        public double getZ() {
            return this.target.field_70161_v;
        }

        @Override
        public Entity getEntity() {
            return this.target;
        }
    }
}

