/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.__helper.client.renderer.item;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.tool.MCH_ItemWrench;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class BuiltInWrenchItemRenderer
implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType);
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entity, ItemCameraTransforms.TransformType transformType, float partialTicks) {
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/wrench.png");
        if (IItemModelRenderer.isFirstPerson(transformType) && entity.func_184587_cr() && entity.func_184600_cs() == EnumHand.MAIN_HAND) {
            float f = MCH_ItemWrench.getUseAnimSmooth(itemStack, partialTicks);
            GL11.glRotatef((float)65.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)(f + 20.0f), (float)1.0f, (float)0.0f, (float)0.0f);
        }
        MCH_ModelManager.render("wrench");
        GL11.glPopMatrix();
    }
}

