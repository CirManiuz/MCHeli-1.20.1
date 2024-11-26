/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.__helper.client.renderer.item;

import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.gltd.MCH_RenderGLTD;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class BuiltInGLTDItemRenderer
implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType) || transformType == ItemCameraTransforms.TransformType.GROUND;
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entityLivingBase, ItemCameraTransforms.TransformType transformType, float partialTicks) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        W_McClient.MOD_bindTexture("textures/gltd.png");
        GL11.glEnable((int)32826);
        GL11.glEnable((int)2903);
        MCH_RenderGLTD.model.renderAll();
        GL11.glDisable((int)32826);
        GL11.glPopMatrix();
    }
}

