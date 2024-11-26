/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.inventory.Container
 *  net.minecraft.inventory.IInventory
 *  net.minecraft.inventory.Slot
 *  net.minecraft.item.ItemStack
 */
package mcheli.aircraft;

import mcheli.MCH_Lib;
import mcheli.aircraft.MCH_AircraftInventory;
import mcheli.aircraft.MCH_EntityAircraft;
import mcheli.aircraft.MCH_ItemFuel;
import mcheli.parachute.MCH_ItemParachute;
import mcheli.uav.MCH_EntityUavStation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MCH_AircraftGuiContainer
extends Container {
    public final EntityPlayer player;
    public final MCH_EntityAircraft aircraft;

    public MCH_AircraftGuiContainer(EntityPlayer player, MCH_EntityAircraft ac) {
        this.player = player;
        this.aircraft = ac;
        MCH_AircraftInventory iv = this.aircraft.getGuiInventory();
        this.func_75146_a(new Slot((IInventory)iv, 0, 10, 30));
        this.func_75146_a(new Slot((IInventory)iv, 1, 10, 48));
        this.func_75146_a(new Slot((IInventory)iv, 2, 10, 66));
        int num = this.aircraft.getNumEjectionSeat();
        for (int i = 0; i < num; ++i) {
            this.func_75146_a(new Slot((IInventory)iv, 3 + i, 10 + 18 * i, 105));
        }
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.func_75146_a(new Slot((IInventory)player.field_71071_by, 9 + x + y * 9, 25 + x * 18, 135 + y * 18));
            }
        }
        for (int x = 0; x < 9; ++x) {
            this.func_75146_a(new Slot((IInventory)player.field_71071_by, x, 25 + x * 18, 195));
        }
    }

    public int getInventoryStartIndex() {
        if (this.aircraft == null) {
            return 3;
        }
        return 3 + this.aircraft.getNumEjectionSeat();
    }

    public void func_75142_b() {
        super.func_75142_b();
    }

    public boolean func_75145_c(EntityPlayer player) {
        MCH_EntityUavStation us;
        if (this.aircraft.getGuiInventory().func_70300_a(player)) {
            return true;
        }
        if (this.aircraft.isUAV() && (us = this.aircraft.getUavStation()) != null) {
            double x = us.field_70165_t + (double)us.posUavX;
            double z = us.field_70161_v + (double)us.posUavZ;
            if (this.aircraft.field_70165_t < x + 10.0 && this.aircraft.field_70165_t > x - 10.0 && this.aircraft.field_70161_v < z + 10.0 && this.aircraft.field_70161_v > z - 10.0) {
                return true;
            }
        }
        return false;
    }

    public ItemStack func_82846_b(EntityPlayer player, int slotIndex) {
        block6: {
            ItemStack itemStack;
            Slot slot;
            MCH_AircraftInventory iv;
            block7: {
                block5: {
                    iv = this.aircraft.getGuiInventory();
                    slot = (Slot)this.field_75151_b.get(slotIndex);
                    if (slot == null) {
                        return null;
                    }
                    itemStack = slot.func_75211_c();
                    MCH_Lib.DbgLog(player.field_70170_p, "transferStackInSlot : %d :" + itemStack, slotIndex);
                    if (itemStack.func_190926_b()) {
                        return ItemStack.field_190927_a;
                    }
                    if (slotIndex >= this.getInventoryStartIndex()) break block5;
                    for (int i = this.getInventoryStartIndex(); i < this.field_75151_b.size(); ++i) {
                        Slot playerSlot = (Slot)this.field_75151_b.get(i);
                        if (!playerSlot.func_75211_c().func_190926_b()) continue;
                        playerSlot.func_75215_d(itemStack);
                        slot.func_75215_d(ItemStack.field_190927_a);
                        return itemStack;
                    }
                    break block6;
                }
                if (!(itemStack.func_77973_b() instanceof MCH_ItemFuel)) break block7;
                for (int i = 0; i < 3; ++i) {
                    if (!iv.getFuelSlotItemStack(i).func_190926_b()) continue;
                    iv.func_70299_a(0 + i, itemStack);
                    slot.func_75215_d(ItemStack.field_190927_a);
                    return itemStack;
                }
                break block6;
            }
            if (!(itemStack.func_77973_b() instanceof MCH_ItemParachute)) break block6;
            int num = this.aircraft.getNumEjectionSeat();
            for (int i = 0; i < num; ++i) {
                if (!iv.getParachuteSlotItemStack(i).func_190926_b()) continue;
                iv.func_70299_a(3 + i, itemStack);
                slot.func_75215_d(ItemStack.field_190927_a);
                return itemStack;
            }
        }
        return ItemStack.field_190927_a;
    }

    public void func_75134_a(EntityPlayer player) {
        super.func_75134_a(player);
        if (!player.field_70170_p.field_72995_K) {
            ItemStack is;
            int i;
            MCH_AircraftInventory iv = this.aircraft.getGuiInventory();
            for (i = 0; i < 3; ++i) {
                is = iv.getFuelSlotItemStack(i);
                if (is.func_190926_b() || is.func_77973_b() instanceof MCH_ItemFuel) continue;
                this.dropPlayerItem(player, 0 + i);
            }
            for (i = 0; i < 2; ++i) {
                is = iv.getParachuteSlotItemStack(i);
                if (is.func_190926_b() || is.func_77973_b() instanceof MCH_ItemParachute) continue;
                this.dropPlayerItem(player, 3 + i);
            }
        }
    }

    public void dropPlayerItem(EntityPlayer player, int slotID) {
        ItemStack itemstack;
        if (!player.field_70170_p.field_72995_K && !(itemstack = this.aircraft.getGuiInventory().func_70304_b(slotID)).func_190926_b()) {
            player.func_71019_a(itemstack, false);
        }
    }
}

