/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.block.Block
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.InventoryCraftResult
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 *  net.minecraft.item.crafting.IRecipe
 */
package mcheli.block;

import javax.annotation.Nullable;
import mcheli.MCH_IRecipeList;
import mcheli.MCH_Lib;
import mcheli.MCH_MOD;
import mcheli.__helper.MCH_Recipes;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_EntityPlayer;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;

public class MCH_DraftingTableGuiContainer
extends Container {
    public final EntityPlayer player;
    public final int posX;
    public final int posY;
    public final int posZ;
    public final int outputSlotIndex;
    private IInventory outputSlot = new InventoryCraftResult();

    public MCH_DraftingTableGuiContainer(EntityPlayer player, int posX, int posY, int posZ) {
        this.player = player;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.func_75146_a(new Slot((IInventory)player.field_71071_by, 9 + x + y * 9, 30 + x * 18, 140 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            this.func_75146_a(new Slot((IInventory)player.field_71071_by, x, 30 + x * 18, 198));
        }
        this.outputSlotIndex = this.field_75153_a.size();
        Slot a = new Slot(this.outputSlot, this.outputSlotIndex, 178, 90){

            public boolean func_75214_a(ItemStack stack) {
                return false;
            }
        };
        this.func_75146_a(a);
        MCH_Lib.DbgLog(player.field_70170_p, "MCH_DraftingTableGuiContainer.MCH_DraftingTableGuiContainer", new Object[0]);
    }

    public void func_75142_b() {
        super.func_75142_b();
    }

    public boolean func_75145_c(EntityPlayer player) {
        Block block = W_WorldFunc.getBlock(player.field_70170_p, this.posX, this.posY, this.posZ);
        if (W_Block.isEqual(block, (Block)MCH_MOD.blockDraftingTable) || W_Block.isEqual(block, (Block)MCH_MOD.blockDraftingTableLit)) {
            return player.func_70092_e((double)this.posX, (double)this.posY, (double)this.posZ) <= 144.0;
        }
        return false;
    }

    public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
        ItemStack itemstack = ItemStack.field_190927_a;
        Slot slot = (Slot)this.field_75151_b.get(slotIndex);
        if (slot != null && slot.func_75216_d()) {
            ItemStack itemstack1 = slot.func_75211_c();
            itemstack = itemstack1.func_77946_l();
            if (slotIndex == this.outputSlotIndex) {
                if (!this.func_75135_a(itemstack1, 0, 36, true)) {
                    return ItemStack.field_190927_a;
                }
            } else {
                return ItemStack.field_190927_a;
            }
            slot.func_75220_a(itemstack1, itemstack);
            if (itemstack1.func_190916_E() == 0) {
                slot.func_75215_d(ItemStack.field_190927_a);
            } else {
                slot.func_75218_e();
            }
            if (itemstack1.func_190916_E() == itemstack.func_190916_E()) {
                return ItemStack.field_190927_a;
            }
            slot.func_190901_a(player, itemstack1);
        }
        return itemstack;
    }

    public void func_75134_a(EntityPlayer player) {
        ItemStack itemstack;
        super.func_75134_a(player);
        if (!player.field_70170_p.field_72995_K && !(itemstack = this.func_75139_a(this.outputSlotIndex).func_75211_c()).func_190926_b()) {
            W_EntityPlayer.dropPlayerItemWithRandomChoice(player, itemstack, false, false);
        }
        MCH_Lib.DbgLog(player.field_70170_p, "MCH_DraftingTableGuiContainer.onContainerClosed", new Object[0]);
    }

    public void createRecipeItem(@Nullable IRecipe recipe) {
        boolean isCreativeMode = this.player.field_71075_bZ.field_75098_d;
        if (this.func_75139_a(this.outputSlotIndex).func_75216_d() && !isCreativeMode) {
            MCH_Lib.DbgLog(this.player.field_70170_p, "MCH_DraftingTableGuiContainer.createRecipeItem:OutputSlot is not empty", new Object[0]);
            return;
        }
        if (recipe == null) {
            MCH_Lib.DbgLog(this.player.field_70170_p, "Error:MCH_DraftingTableGuiContainer.createRecipeItem:recipe is null : ", new Object[0]);
            return;
        }
        boolean result = false;
        if (recipe != null && (isCreativeMode || MCH_Recipes.canCraft(this.player, recipe))) {
            if (!isCreativeMode) {
                MCH_Recipes.consumeInventory(this.player, recipe);
            }
            this.func_75139_a(this.outputSlotIndex).func_75215_d(recipe.func_77571_b().func_77946_l());
            result = true;
        }
        MCH_Lib.DbgLog(this.player.field_70170_p, "MCH_DraftingTableGuiContainer:Result=" + result + ":Recipe=" + recipe.getRegistryName(), new Object[0]);
    }

    public int searchRecipeFromList(MCH_IRecipeList list, ItemStack item) {
        for (int i = 0; i < list.getRecipeListSize(); ++i) {
            if (!list.getRecipe(i).func_77571_b().func_77969_a(item)) continue;
            return i;
        }
        return -1;
    }
}

