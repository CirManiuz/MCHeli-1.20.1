/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.gltd;

import mcheli.__helper.client._IItemRenderer;
import mcheli.gltd.MCH_RenderGLTD;
import mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemGLTDRender
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON || type == ItemRenderType.ENTITY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        W_McClient.MOD_bindTexture("textures/gltd.png");
        switch (type) {
            case ENTITY: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable((int)32826);
                break;
            }
            case EQUIPPED: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glTranslatef((float)0.0f, (float)0.005f, (float)-0.165f);
                GL11.glRotatef((float)-10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)-10.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable((int)32826);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                GL11.glEnable((int)32826);
                GL11.glEnable((int)2903);
                GL11.glTranslatef((float)0.3f, (float)0.5f, (float)-0.5f);
                GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
                GL11.glRotatef((float)10.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glRotatef((float)50.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)-10.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                MCH_RenderGLTD.model.renderAll();
                GL11.glDisable((int)32826);
            }
        }
        GL11.glPopMatrix();
    }
}

