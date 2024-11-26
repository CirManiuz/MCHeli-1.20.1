/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.__helper.client.renderer.item;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class BuiltInRangeFinderItemRenderer
implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType) || transformType == ItemCameraTransforms.TransformType.GROUND;
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entity, ItemCameraTransforms.TransformType transformType, float partialTicks) {
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/rangefinder.png");
        boolean flag = true;
        if (IItemModelRenderer.isFirstPerson(transformType)) {
            boolean bl = flag = entity instanceof EntityPlayer && !MCH_ItemRangeFinder.isUsingScope((EntityPlayer)entity);
            if (entity.func_184587_cr() && entity.func_184600_cs() == EnumHand.MAIN_HAND) {
                GL11.glTranslated((double)0.6563f, (double)0.3438f, (double)0.01f);
            }
        }
        if (flag) {
            MCH_ModelManager.render("rangefinder");
        }
        GL11.glPopMatrix();
    }
}

