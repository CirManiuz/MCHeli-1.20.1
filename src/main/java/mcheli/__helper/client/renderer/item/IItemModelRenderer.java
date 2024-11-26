/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 */
package mcheli.__helper.client.renderer.item;

import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(value=Side.CLIENT)
public interface IItemModelRenderer {
    public boolean shouldRenderer(ItemStack var1, ItemCameraTransforms.TransformType var2);

    public void renderItem(ItemStack var1, @Nullable EntityLivingBase var2, ItemCameraTransforms.TransformType var3, float var4);

    public static boolean isFirstPerson(ItemCameraTransforms.TransformType type) {
        return type == ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND || type == ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND;
    }

    public static boolean isThirdPerson(ItemCameraTransforms.TransformType type) {
        return type == ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND || type == ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
    }
}

