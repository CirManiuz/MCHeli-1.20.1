/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemCoal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.ActionResult
 *  net.minecraft.util.EnumActionResult
 *  net.minecraft.util.EnumHand
 *  net.minecraft.util.NonNullList
 *  net.minecraft.world.World
 */
package mcheli.aircraft;

import mcheli.wrapper.W_Item;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class MCH_ItemFuel
extends W_Item {
    public MCH_ItemFuel(int itemID) {
        super(itemID);
        this.func_77656_e(600);
        this.func_77625_d(1);
        this.setNoRepair();
        this.func_77664_n();
    }

    public ActionResult<ItemStack> func_77659_a(World world, EntityPlayer player, EnumHand handIn) {
        ItemStack stack = player.func_184586_b(handIn);
        if (!world.field_72995_K && stack.func_77951_h() && !player.field_71075_bZ.field_75098_d) {
            this.refuel(stack, player, 1);
            this.refuel(stack, player, 0);
            return new ActionResult(EnumActionResult.SUCCESS, (Object)stack);
        }
        return new ActionResult(EnumActionResult.PASS, (Object)stack);
    }

    private void refuel(ItemStack stack, EntityPlayer player, int coalType) {
        NonNullList list = player.field_71071_by.field_70462_a;
        for (int i = 0; i < list.size(); ++i) {
            ItemStack is = (ItemStack)list.get(i);
            if (is.func_190926_b() || !(is.func_77973_b() instanceof ItemCoal) || is.func_77960_j() != coalType) continue;
            for (int j = 0; is.func_190916_E() > 0 && stack.func_77951_h() && j < 64; ++j) {
                int damage = stack.func_77960_j() - (coalType == 1 ? 75 : 100);
                if (damage < 0) {
                    damage = 0;
                }
                stack.func_77964_b(damage);
                is.func_190918_g(1);
            }
            if (is.func_190916_E() > 0) continue;
            list.set(i, ItemStack.field_190927_a);
        }
    }
}

