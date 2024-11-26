/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.InventoryCrafting
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemCoal
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.IRecipe
 *  net.minecraft.world.World
 *  net.minecraftforge.registries.IForgeRegistryEntry$Impl
 */
package mcheli;

import mcheli.MCH_MOD;
import mcheli.aircraft.MCH_ItemFuel;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemCoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class MCH_RecipeFuel
extends IForgeRegistryEntry.Impl<IRecipe>
implements IRecipe {
    public boolean func_77569_a(InventoryCrafting inv, World var2) {
        int jcnt = 0;
        int ccnt = 0;
        for (int i = 0; i < inv.func_70302_i_(); ++i) {
            ItemStack is = inv.func_70301_a(i);
            if (is.func_190926_b()) continue;
            if (is.func_77973_b() instanceof MCH_ItemFuel) {
                if (is.func_77960_j() == 0) {
                    return false;
                }
                if (++jcnt <= 1) continue;
                return false;
            }
            if (is.func_77973_b() instanceof ItemCoal && is.func_190916_E() > 0) {
                ++ccnt;
                continue;
            }
            return false;
        }
        return jcnt == 1 && ccnt > 0;
    }

    public ItemStack func_77572_b(InventoryCrafting inv) {
        ItemStack is;
        int i;
        ItemStack output = new ItemStack((Item)MCH_MOD.itemFuel);
        for (i = 0; i < inv.func_70302_i_(); ++i) {
            is = inv.func_70301_a(i);
            if (is.func_190926_b() || !(is.func_77973_b() instanceof MCH_ItemFuel)) continue;
            output.func_77964_b(is.func_77960_j());
            break;
        }
        for (i = 0; i < inv.func_70302_i_(); ++i) {
            is = inv.func_70301_a(i);
            if (is.func_190926_b() || !(is.func_77973_b() instanceof ItemCoal)) continue;
            int sp = 100;
            if (is.func_77960_j() == 1) {
                sp = 75;
            }
            if (output.func_77960_j() > sp) {
                output.func_77964_b(output.func_77960_j() - sp);
                continue;
            }
            output.func_77964_b(0);
        }
        return output;
    }

    public boolean func_194133_a(int width, int height) {
        return width >= 3 && height >= 3;
    }

    public ItemStack func_77571_b() {
        return ItemStack.field_190927_a;
    }
}

