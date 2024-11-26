/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.util.EnumHand
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.__helper.client.renderer.item;

import mcheli.MCH_Config;
import mcheli.MCH_ModelManager;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.lweapon.MCH_ItemLightWeaponBase;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class BuiltInLightWeaponItemRenderer
implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        return IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType);
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entityLivingBase, ItemCameraTransforms.TransformType transformType, float partialTicks) {
        boolean isRender = false;
        if (IItemModelRenderer.isFirstPerson(transformType) || IItemModelRenderer.isThirdPerson(transformType)) {
            EntityPlayer player;
            isRender = true;
            if (entityLivingBase instanceof EntityPlayer && MCH_ItemLightWeaponBase.isHeld(player = (EntityPlayer)entityLivingBase) && W_Lib.isFirstPerson() && W_Lib.isClientPlayer((Entity)player)) {
                isRender = false;
            }
        }
        if (isRender) {
            this.renderItem(itemStack, IItemModelRenderer.isFirstPerson(transformType), entityLivingBase);
        }
    }

    private void renderItem(ItemStack itemStack, boolean isFirstPerson, EntityLivingBase entity) {
        String name = MCH_ItemLightWeaponBase.getName(itemStack);
        GL11.glEnable((int)32826);
        GL11.glEnable((int)2903);
        GL11.glPushMatrix();
        if (MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel((int)7425);
        }
        GL11.glEnable((int)2884);
        W_McClient.MOD_bindTexture("textures/lweapon/" + name + ".png");
        if (isFirstPerson && entity.func_184587_cr() && entity.func_184600_cs() == EnumHand.MAIN_HAND) {
            GL11.glTranslated((double)0.13f, (double)0.27f, (double)0.01f);
        }
        MCH_ModelManager.render("lweapons", name);
        GL11.glShadeModel((int)7424);
        GL11.glPopMatrix();
        GL11.glDisable((int)32826);
    }
}

