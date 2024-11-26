/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GlStateManager
 *  net.minecraft.client.renderer.block.model.ItemCameraTransforms$TransformType
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.item.ItemStack
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.__helper.client.renderer.item;

import mcheli.MCH_ModelManager;
import mcheli.__helper.client.renderer.item.IItemModelRenderer;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class BuiltInDraftingTableItemRenderer
implements IItemModelRenderer {
    @Override
    public boolean shouldRenderer(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        return true;
    }

    @Override
    public void renderItem(ItemStack itemStack, EntityLivingBase entityLivingBase, ItemCameraTransforms.TransformType transformType, float partialTicks) {
        GlStateManager.func_179094_E();
        W_McClient.MOD_bindTexture("textures/blocks/drafting_table.png");
        GlStateManager.func_179091_B();
        switch (transformType) {
            case GROUND: {
                GL11.glTranslatef((float)0.0f, (float)0.5f, (float)0.0f);
                GL11.glScalef((float)1.5f, (float)1.5f, (float)1.5f);
                break;
            }
            case GUI: 
            case FIXED: {
                GlStateManager.func_179109_b((float)0.0f, (float)-0.5f, (float)0.0f);
                GlStateManager.func_179152_a((float)0.75f, (float)0.75f, (float)0.75f);
                break;
            }
            case THIRD_PERSON_LEFT_HAND: 
            case THIRD_PERSON_RIGHT_HAND: {
                GL11.glTranslatef((float)0.0f, (float)0.0f, (float)0.5f);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                break;
            }
            case FIRST_PERSON_LEFT_HAND: 
            case FIRST_PERSON_RIGHT_HAND: {
                GL11.glTranslatef((float)0.75f, (float)0.0f, (float)0.0f);
                GL11.glScalef((float)1.0f, (float)1.0f, (float)1.0f);
                GL11.glRotatef((float)90.0f, (float)0.0f, (float)-1.0f, (float)0.0f);
                break;
            }
        }
        MCH_ModelManager.render("blocks", "drafting_table");
        GlStateManager.func_179121_F();
        GlStateManager.func_179131_c((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        GlStateManager.func_179147_l();
    }
}

