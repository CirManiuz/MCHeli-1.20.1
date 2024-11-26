/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.lweapon;

import mcheli.MCH_Config;
import mcheli.MCH_ModelManager;
import mcheli.__helper.client._IItemRenderer;
import mcheli.lweapon.MCH_ItemLightWeaponBase;
import mcheli.wrapper.W_Lib;
import mcheli.wrapper.W_McClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemLightWeaponRender
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;
    }

    public boolean useCurrentWeapon() {
        return false;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        boolean isRender = false;
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.EQUIPPED) {
            EntityPlayer player;
            isRender = true;
            if (data[1] instanceof EntityPlayer && MCH_ItemLightWeaponBase.isHeld(player = (EntityPlayer)data[1]) && W_Lib.isFirstPerson() && W_Lib.isClientPlayer((Entity)player)) {
                isRender = false;
            }
        }
        if (isRender) {
            MCH_ItemLightWeaponRender.renderItem(item, (Entity)W_Lib.castEntityLivingBase(data[1]), type == ItemRenderType.EQUIPPED_FIRST_PERSON);
        }
    }

    @SideOnly(value=Side.CLIENT)
    public static void renderItem(ItemStack pitem, Entity entity, boolean isFirstPerson) {
        if (pitem == null || pitem.func_77973_b() == null) {
            return;
        }
        String name = MCH_ItemLightWeaponBase.getName(pitem);
        GL11.glEnable((int)32826);
        GL11.glEnable((int)2903);
        GL11.glPushMatrix();
        if (MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel((int)7425);
        }
        GL11.glEnable((int)2884);
        W_McClient.MOD_bindTexture("textures/lweapon/" + name + ".png");
        if (isFirstPerson) {
            GL11.glTranslatef((float)0.0f, (float)0.005f, (float)-0.165f);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            GL11.glRotatef((float)-10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)90.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)-50.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        } else {
            GL11.glTranslatef((float)0.3f, (float)0.3f, (float)0.0f);
            GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
            GL11.glRotatef((float)20.0f, (float)0.0f, (float)0.0f, (float)1.0f);
            GL11.glRotatef((float)10.0f, (float)0.0f, (float)1.0f, (float)0.0f);
            GL11.glRotatef((float)15.0f, (float)1.0f, (float)0.0f, (float)0.0f);
        }
        MCH_ModelManager.render("lweapons", name);
        GL11.glShadeModel((int)7424);
        GL11.glPopMatrix();
        GL11.glDisable((int)32826);
    }
}

