/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.BlockDispenser
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.dispenser.BehaviorDefaultDispenseItem
 *  net.minecraft.dispenser.IBlockSource
 *  net.minecraft.entity.Entity
 *  net.minecraft.init.SoundEvents
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.SoundCategory
 */
package mcheli.throwable;

import mcheli.MCH_Lib;
import mcheli.throwable.MCH_EntityThrowable;
import mcheli.throwable.MCH_ItemThrowable;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.throwable.MCH_ThrowableInfoManager;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.properties.IProperty;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;

public class MCH_ItemThrowableDispenseBehavior
extends BehaviorDefaultDispenseItem {
    public ItemStack func_82487_b(IBlockSource bs, ItemStack itemStack) {
        MCH_ThrowableInfo info;
        EnumFacing enumfacing = (EnumFacing)bs.func_189992_e().func_177229_b((IProperty)BlockDispenser.field_176441_a);
        double x = bs.func_82615_a() + (double)enumfacing.func_82601_c() * 2.0;
        double y = bs.func_82617_b() + (double)enumfacing.func_96559_d() * 2.0;
        double z = bs.func_82616_c() + (double)enumfacing.func_82599_e() * 2.0;
        if (itemStack.func_77973_b() instanceof MCH_ItemThrowable && (info = MCH_ThrowableInfoManager.get(itemStack.func_77973_b())) != null) {
            bs.func_82618_k().func_184134_a(x, y, z, SoundEvents.field_187737_v, SoundCategory.BLOCKS, 0.5f, 0.4f / (bs.func_82618_k().field_73012_v.nextFloat() * 0.4f + 0.8f), false);
            if (!bs.func_82618_k().field_72995_K) {
                MCH_Lib.DbgLog(bs.func_82618_k(), "MCH_ItemThrowableDispenseBehavior.dispenseStack(%s)", info.name);
                MCH_EntityThrowable entity = new MCH_EntityThrowable(bs.func_82618_k(), x, y, z);
                entity.field_70159_w = (float)enumfacing.func_82601_c() * info.dispenseAcceleration;
                entity.field_70181_x = (float)enumfacing.func_96559_d() * info.dispenseAcceleration;
                entity.field_70179_y = (float)enumfacing.func_82599_e() * info.dispenseAcceleration;
                entity.setInfo(info);
                bs.func_82618_k().func_72838_d((Entity)entity);
                itemStack.func_77979_a(1);
            }
        }
        return itemStack;
    }
}

