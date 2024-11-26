/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.model.ModelBiped
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.inventory.EntityEquipmentSlot
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli;

import javax.annotation.Nullable;
import mcheli.MCH_TEST_ModelBiped;
import mcheli.wrapper.W_ItemArmor;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MCH_ItemArmor
extends W_ItemArmor {
    public static final String HELMET_TEXTURE = "mcheli:textures/helicopters/ah-64.png";
    public static final String CHESTPLATE_TEXTURE = "mcheli:textures/armor/plate.png";
    public static final String LEGGINGS_TEXTURE = "mcheli:textures/armor/leg.png";
    public static final String BOOTS_TEXTURE = "mcheli:textures/armor/boots.png";
    public static MCH_TEST_ModelBiped model = null;

    public MCH_ItemArmor(int par1, int par3, int par4) {
        super(par1, par3, par4);
    }

    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        if (slot == EntityEquipmentSlot.HEAD) {
            return HELMET_TEXTURE;
        }
        if (slot == EntityEquipmentSlot.CHEST) {
            return CHESTPLATE_TEXTURE;
        }
        if (slot == EntityEquipmentSlot.LEGS) {
            return LEGGINGS_TEXTURE;
        }
        if (slot == EntityEquipmentSlot.FEET) {
            return BOOTS_TEXTURE;
        }
        return "none";
    }

    @Nullable
    @SideOnly(value=Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, EntityEquipmentSlot armorSlot, ModelBiped _default) {
        if (model == null) {
            model = new MCH_TEST_ModelBiped();
        }
        if (armorSlot == EntityEquipmentSlot.HEAD) {
            return model;
        }
        return null;
    }
}

