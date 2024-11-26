/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.item.ItemStack
 *  org.lwjgl.opengl.GL11
 */
package mcheli.block;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client._IItemRenderer;
import mcheli.wrapper.W_McClient;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

@Deprecated
public class MCH_DraftingTableItemRender
implements _IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        switch (type) {
            case ENTITY: 
            case EQUIPPED: 
            case EQUIPPED_FIRST_PERSON: 
            case INVENTORY: {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object ... data) {
        GL11.glPushMatrix();
        W_McClient.MOD_bindTexture("textures/blocks/drafting_table.png");
        GL11.glEnable((int)32826);
        switch (type) {
            case ENTITY: {
                GL11.glTranslatef((float)0.0f, (float)0.5f, (float)0.0f);
                GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
                break;
            }
            case INVENTORY: {
                GL11.glTranslatef((float)0.0f, (float)-0.5f, (float)0.0f);
                GL11.glScalef((float)0.75f, (float)0.75f, (float)0.75f);
                break;
            }
            case EQUIPPED: {
                GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.5f);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                GL11.glTranslatef((float)0.75f, (float)0.0f, (float)0.0f);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)-1.0f, (float)0.0f);
            }
        }
        MCH_ModelManager.render("blocks", "drafting_table");
        GL11.glPopMatrix();
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GL11.glEnable((int)3042);
    }
}

