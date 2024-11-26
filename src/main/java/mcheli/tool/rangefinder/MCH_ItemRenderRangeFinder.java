/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.tool.rangefinder;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client._IItemRenderer;
import mcheli.tool.rangefinder.MCH_ItemRangeFinder;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemRenderRangeFinder
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/rangefinder.png");
        float size = 1.0f;
        switch (type) {
            case ENTITY: {
                size = 2.2f;
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glRotatef((float)-130.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)70.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)5.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslatef((float)0.0f, (float)0.0f, (float)-0.0f);
                MCH_ModelManager.render("rangefinder");
                break;
            }
            case EQUIPPED: {
                size = 2.2f;
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glRotatef((float)-130.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)70.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)5.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                if (Minecraft.func_71410_x().field_71439_g.func_184612_cw() > 0) {
                    GL11.glTranslatef((float)0.4f, (float)-0.35f, (float)-0.3f);
                } else {
                    GL11.glTranslatef((float)0.2f, (float)-0.35f, (float)-0.3f);
                }
                MCH_ModelManager.render("rangefinder");
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                if (MCH_ItemRangeFinder.isUsingScope((EntityPlayer)Minecraft.func_71410_x().field_71439_g)) break;
                size = 2.2f;
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glRotatef((float)-210.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)-10.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)-10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslatef((float)0.06f, (float)0.53f, (float)-0.1f);
                MCH_ModelManager.render("rangefinder");
            }
        }
        GL11.glPopMatrix();
    }
}

