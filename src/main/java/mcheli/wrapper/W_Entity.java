/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.crash.CrashReportCategory
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.item.EntityFallingBlock
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.util.DamageSource
 *  net.minecraft.util.math.BlockPos
 *  net.minecraft.world.Explosion
 *  net.minecraft.world.World
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.wrapper;

import javax.annotation.Nullable;
import mcheli.wrapper.W_Block;
import net.minecraft.block.Block;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class W_Entity
extends Entity {
    protected double _renderDistanceWeight = 1.0;

    public W_Entity(World par1World) {
        super(par1World);
    }

    protected void func_70088_a() {
    }

    public static boolean isEntityFallingBlock(Entity entity) {
        return entity instanceof EntityFallingBlock;
    }

    public static int getEntityId(@Nullable Entity entity) {
        return entity != null ? entity.func_145782_y() : -1;
    }

    public static boolean isEqual(@Nullable Entity e1, @Nullable Entity e2) {
        int i2;
        int i1 = W_Entity.getEntityId(e1);
        return i1 == (i2 = W_Entity.getEntityId(e2));
    }

    public EntityItem func_145778_a(Item item, int par2, float par3) {
        return this.func_70099_a(new ItemStack(item, par2, 0), par3);
    }

    public String getEntityName() {
        return super.func_70022_Q();
    }

    public boolean attackEntityFrom(DamageSource par1DamageSource, int par2) {
        return this.func_70097_a(par1DamageSource, par2);
    }

    public boolean func_70097_a(DamageSource par1DamageSource, float par2) {
        return false;
    }

    public static boolean attackEntityFrom(Entity entity, DamageSource ds, float par2) {
        return entity.func_70097_a(ds, par2);
    }

    public void func_85029_a(CrashReportCategory par1CrashReportCategory) {
        super.func_85029_a(par1CrashReportCategory);
    }

    public static float getBlockExplosionResistance(Entity entity, Explosion par1Explosion, World par2World, int par3, int par4, int par5, Block par6Block) {
        if (par6Block != null) {
            try {
                return entity.func_180428_a(par1Explosion, par2World, new BlockPos(par3, par4, par5), par6Block.func_176223_P());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0.0f;
    }

    public static boolean shouldExplodeBlock(Entity entity, Explosion par1Explosion, World par2World, int par3, int par4, int par5, int par6, float par7) {
        return entity.func_174816_a(par1Explosion, par2World, new BlockPos(par3, par4, par5), W_Block.getBlockById(par6).func_176223_P(), par7);
    }

    public static PotionEffect getActivePotionEffect(Entity entity, Potion par1Potion) {
        return entity instanceof EntityLivingBase ? ((EntityLivingBase)entity).func_70660_b(par1Potion) : null;
    }

    public static void removePotionEffectClient(Entity entity, Potion potion) {
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).func_184589_d(potion);
        }
    }

    public static void removePotionEffect(Entity entity, Potion potion) {
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).func_184589_d(potion);
        }
    }

    public static void addPotionEffect(Entity entity, PotionEffect pe) {
        if (entity instanceof EntityLivingBase) {
            ((EntityLivingBase)entity).func_70690_d(pe);
        }
    }

    protected void func_145775_I() {
        super.func_145775_I();
    }

    @SideOnly(value=Side.CLIENT)
    public boolean func_70112_a(double distance) {
        double d0 = this.func_174813_aQ().func_72320_b();
        if (Double.isNaN(d0)) {
            d0 = 1.0;
        }
        return distance < (d0 = d0 * 64.0 * this._renderDistanceWeight) * d0;
    }
}

