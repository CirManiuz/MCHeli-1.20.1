/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.util.ResourceLocation
 *  net.minecraftforge.fml.client.registry.IRenderFactory
 *  net.minecraftforge.fml.relauncher.Side
 *  net.minecraftforge.fml.relauncher.SideOnly
 *  org.lwjgl.opengl.GL11
 */
package mcheli.flare;

import mcheli.flare.MCH_EntityFlare;
import mcheli.flare.MCH_ModelFlare;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderFlare
extends W_Render<MCH_EntityFlare> {
    public static final IRenderFactory<MCH_EntityFlare> FACTORY = MCH_RenderFlare::new;
    protected MCH_ModelFlare model = new MCH_ModelFlare();

    public MCH_RenderFlare(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(MCH_EntityFlare entity, double posX, double posY, double posZ, float yaw, float partialTickTime) {
        GL11.glPushMatrix();
        GL11.glEnable((int)2884);
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)(-entity.field_70177_z), (float)0.0f, (float)1.0f, (float)0.0f);
        GL11.glRotatef((float)entity.field_70125_A, (float)1.0f, (float)0.0f, (float)0.0f);
        GL11.glRotatef((float)45.0f, (float)0.0f, (float)0.0f, (float)1.0f);
        GL11.glColor4f((float)1.0f, (float)1.0f, (float)0.5f, (float)1.0f);
        this.bindTexture("textures/flare.png");
        this.model.renderModel(0.0, 0.0, 0.0625f);
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityFlare entity) {
        return TEX_DEFAULT;
    }
}

