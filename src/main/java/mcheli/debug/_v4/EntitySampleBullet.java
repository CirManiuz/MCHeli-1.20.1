/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.NBTTagCompound
 *  net.minecraft.util.math.Vec3d
 *  net.minecraft.world.World
 */
package mcheli.debug._v4;

import mcheli.weapon.MCH_EntityRocket;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySampleBullet
extends MCH_EntityRocket {
    private Vec3d firstPos;

    public EntitySampleBullet(World par1World) {
        super(par1World, 0.0, 0.0, 0.0, 2.0, 0.0, 0.0, 0.0f, 0.0f, 4.0);
        this.setName("hydra70");
        this.func_70105_a(1.0f, 1.0f);
        this.explosionPower = 3;
        this.setPower(22);
        this.firstPos = Vec3d.field_186680_a;
        this.accelerationFactor = 1.0;
        this.delayFuse = 100;
    }

    @Override
    public void func_70037_a(NBTTagCompound par1nbtTagCompound) {
        this.field_70159_w = 1.0;
    }

    @Override
    public boolean checkValid() {
        double x = this.field_70165_t - this.firstPos.field_72450_a;
        double z = this.field_70161_v - this.firstPos.field_72449_c;
        return x * x + z * z < 3.38724E7 && this.field_70163_u > -10.0;
    }
}

