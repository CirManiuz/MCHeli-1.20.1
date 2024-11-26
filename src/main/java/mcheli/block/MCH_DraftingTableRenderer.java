/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.RenderHelper
 *  net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer
 *  net.minecraft.item.ItemStack
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.block;

import mcheli.MCH_Config;
import mcheli.MCH_ModelManager;
import mcheli.block.MCH_DraftingTableTileEntity;
import mcheli.wrapper.W_McClient;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

public class MCH_DraftingTableRenderer
extends TileEntitySpecialRenderer<MCH_DraftingTableTileEntity> {
    @SideOnly(value=Side.CLIENT)
    private static DraftingTableStackRenderer stackRenderer;

    @SideOnly(value=Side.CLIENT)
    public static DraftingTableStackRenderer getStackRenderer() {
        if (stackRenderer == null) {
            stackRenderer = new DraftingTableStackRenderer();
        }
        return stackRenderer;
    }

    public void render(MCH_DraftingTableTileEntity tile, double posX, double posY, double posZ, float partialTicks, int destroyStage, float alpha) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        GL11.glTranslated((double)(posX + 0.5), (double)posY, (double)(posZ + 0.5));
        float yaw = this.getYawAngle(tile);
        GL11.glRotatef((float)yaw, (float)0.0f, (float)1.0f, (float)0.0f);
        RenderHelper.func_74519_b();
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        GL11.glEnable((int)3042);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        if (MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel((int)7425);
        }
        W_McClient.MOD_bindTexture("textures/blocks/drafting_table.png");
        MCH_ModelManager.render("blocks", "drafting_table");
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
        GL11.glPopMatrix();
    }

    private float getYawAngle(MCH_DraftingTableTileEntity tile) {
        if (tile.func_145830_o()) {
            return (float)(-tile.func_145832_p()) * 45.0f + 180.0f;
        }
        return 0.0f;
    }

    @SideOnly(value=Side.CLIENT)
    private static class DraftingTableStackRenderer
    extends TileEntityItemStackRenderer {
        private MCH_DraftingTableTileEntity draftingTable = new MCH_DraftingTableTileEntity();

        private DraftingTableStackRenderer() {
        }

        public void func_192838_a(ItemStack p_192838_1_, float partialTicks) {
            TileEntityRendererDispatcher.field_147556_a.func_192855_a((TileEntity)this.draftingTable, 0.0, 0.0, 0.0, partialTicks, 0.0f);
        }
    }
}

