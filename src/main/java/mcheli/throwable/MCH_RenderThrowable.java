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
package mcheli.throwable;

import mcheli.throwable.MCH_EntityThrowable;
import mcheli.throwable.MCH_ThrowableInfo;
import mcheli.wrapper.W_Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(value=Side.CLIENT)
public class MCH_RenderThrowable
extends W_Render<MCH_EntityThrowable> {
    public static final IRenderFactory<MCH_EntityThrowable> FACTORY = MCH_RenderThrowable::new;

    public MCH_RenderThrowable(RenderManager renderManager) {
        super(renderManager);
        this.field_76989_e = 0.0f;
    }

    public void doRender(MCH_EntityThrowable entity, double posX, double posY, double posZ, float par8, float tickTime) {
        MCH_EntityThrowable throwable = entity;
        MCH_ThrowableInfo info = throwable.getInfo();
        if (info == null) {
            return;
        }
        GL11.glPushMatrix();
        GL11.glTranslated((double)posX, (double)posY, (double)posZ);
        GL11.glRotatef((float)entity.field_70177_z, (float)0.0f, (float)-1.0f, (float)0.0f);
        GL11.glRotatef((float)entity.field_70125_A, (float)1.0f, (float)0.0f, (float)0.0f);
        this.setCommonRenderParam(true, entity.func_70070_b());
        if (info.model != null) {
            this.bindTexture("textures/throwable/" + info.name + ".png");
            info.model.renderAll();
        }
        this.restoreCommonRenderParam();
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(MCH_EntityThrowable entity) {
        return TEX_DEFAULT;
    }
}

