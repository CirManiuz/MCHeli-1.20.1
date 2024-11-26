/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.tool;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client._IItemRenderer;
import mcheli.tool.MCH_ItemWrench;
import mcheli.wrapper.W_McClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_ItemRenderWrench
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/wrench.png");
        float size = 1.0f;
        switch (type) {
            case ENTITY: {
                size = 2.2f;
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glRotatef((float)-130.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)-40.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glTranslatef((float)0.1f, (float)0.5f, (float)-0.1f);
                break;
            }
            case EQUIPPED: {
                EntityPlayer player;
                int useFrame = MCH_ItemWrench.getUseAnimCount(item) - 8;
                if (useFrame < 0) {
                    useFrame = -useFrame;
                }
                size = 2.2f;
                if (data.length >= 2 && data[1] instanceof EntityPlayer && (player = (EntityPlayer)data[1]).func_184605_cv() > 0) {
                    float x = 0.8567f;
                    float y = -0.0298f;
                    float z = 0.0f;
                    GL11.glTranslatef((float)(-x), (float)(-y), (float)(-z));
                    GL11.glRotatef((float)(useFrame + 20), (float)1.0f, (float)0.0f, (float)0.0f);
                    GL11.glTranslatef((float)x, (float)y, (float)z);
                }
                GL11.glScalef((float)size, (float)size, (float)size);
                GL11.glRotatef((float)-200.0f, (float)0.0f, (float)1.0f, (float)0.0f);
                GL11.glRotatef((float)-60.0f, (float)1.0f, (float)0.0f, (float)0.0f);
                GL11.glRotatef((float)0.0f, (float)0.0f, (float)0.0f, (float)1.0f);
                GL11.glTranslatef((float)-0.2f, (float)0.5f, (float)-0.1f);
            }
        }
        MCH_ModelManager.render("wrench");
        GL11.glPopMatrix();
    }
}

