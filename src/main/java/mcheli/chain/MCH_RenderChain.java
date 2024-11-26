/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher
 *  net.minecraft.util.ResourceLocation
 *  net.minecraft.util.math.Vec3d
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.chain;

import mcheli.MCH_Lib;
import mcheli.MCH_ModelManager;
import mcheli.chain.MCH_EntityChain;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderChain
extends W_Render<MCH_EntityChain> {
    public static final IRenderFactory<MCH_EntityChain> FACTORY = MCH_RenderChain::new;

    public MCH_RenderChain(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(MCH_EntityChain e, double posX, double posY, double posZ, float par8, float par9) {
        double diff;
        if (!(e instanceof MCH_EntityChain)) {
            return;
        }
        MCH_EntityChain chain = e;
        if (chain.towedEntity == null || chain.towEntity == null) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        GL11.glColor4f((float)0.5f, (float)0.5f, (float)0.5f, (float)1.0f);
        GL11.glTranslated((double)(chain.towedEntity.field_70142_S - TileEntityRendererDispatcher.field_147554_b), (double)(chain.towedEntity.field_70137_T - TileEntityRendererDispatcher.field_147555_c), (double)(chain.towedEntity.field_70136_U - TileEntityRendererDispatcher.field_147552_d));
        this.bindTexture("textures/chain.png");
        double dx = chain.towEntity.field_70142_S - chain.towedEntity.field_70142_S;
        double dy = chain.towEntity.field_70137_T - chain.towedEntity.field_70137_T;
        double dz = chain.towEntity.field_70136_U - chain.towedEntity.field_70136_U;
        double x = dx * (double)0.95f / diff;
        double y = dy * (double)0.95f / diff;
        double z = dz * (double)0.95f / diff;
        for (diff = Math.sqrt(dx * dx + dy * dy + dz * dz); diff > (double)0.95f; diff -= (double)0.95f) {
            GL11.glTranslated((double)x, (double)y, (double)z);
            GL11.glPushMatrix();
            Vec3d v = MCH_Lib.getYawPitchFromVec(x, y, z);
            GL11.glRotatef((float)((float)v.field_72448_b), (float)0.0f, (float)-1.0f, (float)0.0f);
            GL11.glRotatef((float)((float)v.field_72449_c), (float)0.0f, (float)0.0f, (float)1.0f);
            MCH_ModelManager.render("chain");
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityChain entity) {
        return TEX_DEFAULT;
    }
}

