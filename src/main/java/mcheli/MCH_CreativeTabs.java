/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.creativetab.CreativeTabs
 *  net.minecraft.item.Item
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.NonNullList
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import mcheli.aircraft.MCH_AircraftInfo;
import mcheli.aircraft.MCH_ItemAircraft;
import mcheli.wrapper.W_Item;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_CreativeTabs
extends CreativeTabs {
    private List<ItemStack> iconItems = new ArrayList<ItemStack>();
    private ItemStack lastItem = ItemStack.field_190927_a;
    private int currentIconIndex = 0;
    private int switchItemWait = 0;
    private Item fixedItem = null;

    public MCH_CreativeTabs(String label) {
        super(label);
    }

    public void setFixedIconItem(String itemName) {
        if (itemName.indexOf(58) >= 0) {
            this.fixedItem = W_Item.getItemByName(itemName);
        } else {
            this.fixedItem = W_Item.getItemByName("mcheli:" + itemName);
            if (this.fixedItem != null) {
                // empty if block
            }
        }
    }

    public ItemStack func_78016_d() {
        if (this.iconItems.size() <= 0) {
            return ItemStack.field_190927_a;
        }
        this.currentIconIndex = (this.currentIconIndex + 1) % this.iconItems.size();
        return this.iconItems.get(this.currentIconIndex);
    }

    public ItemStack func_151244_d() {
        if (this.fixedItem != null) {
            return new ItemStack(this.fixedItem, 1, 0);
        }
        if (this.switchItemWait > 0) {
            --this.switchItemWait;
        } else {
            this.lastItem = this.func_78016_d();
            this.switchItemWait = 60;
        }
        if (this.lastItem.func_190926_b()) {
            this.lastItem = new ItemStack(W_Item.getItemByName("iron_block"));
        }
        return this.lastItem;
    }

    @SideOnly(value=Side.CLIENT)
    public void func_78018_a(NonNullList<ItemStack> list) {
        super.func_78018_a(list);
        Comparator<ItemStack> cmp = new Comparator<ItemStack>(){

            @Override
            public int compare(ItemStack i1, ItemStack i2) {
                if (i1.func_77973_b() instanceof MCH_ItemAircraft && i2.func_77973_b() instanceof MCH_ItemAircraft) {
                    MCH_AircraftInfo info1 = ((MCH_ItemAircraft)i1.func_77973_b()).getAircraftInfo();
                    MCH_AircraftInfo info2 = ((MCH_ItemAircraft)i2.func_77973_b()).getAircraftInfo();
                    if (info1 != null && info2 != null) {
                        String s1 = info1.category + "." + info1.name;
                        String s2 = info2.category + "." + info2.name;
                        return s1.compareTo(s2);
                    }
                }
                return i1.func_77973_b().func_77658_a().compareTo(i2.func_77973_b().func_77658_a());
            }
        };
        Collections.sort(list, cmp);
    }

    public void addIconItem(Item i) {
        if (i != null) {
            this.iconItems.add(new ItemStack(i));
        }
    }

    public String func_78024_c() {
        return "MC Heli";
    }
}

