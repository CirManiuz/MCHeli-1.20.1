/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemArmor
 *  net.minecraft.item.ItemArmor$ArmorMaterial
 */
package mcheli.wrapper;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

public class W_ItemArmor
extends ItemArmor {
    public W_ItemArmor(int par1, int par3, int par4) {
        super(ItemArmor.ArmorMaterial.LEATHER, par3, EntityEquipmentSlot.values()[par4]);
    }
}

