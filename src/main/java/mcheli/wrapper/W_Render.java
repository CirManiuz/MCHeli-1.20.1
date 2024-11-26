/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.renderer.GLAllocation
 *  net.minecraft.client.renderer.OpenGlHelper
 *  net.minecraft.client.renderer.entity.Render
 *  net.minecraft.client.renderer.entity.RenderManager
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.ResourceLocation
 *  org.lwjgl.opengl.GL11
 */
package mcheli.wrapper;

import java.nio.FloatBuffer;
import mcheli.MCH_Config;
import mcheli.wrapper.W_MOD;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public abstract class W_Render<T extends Entity>
extends Render<T> {
    private static FloatBuffer colorBuffer = GLAllocation.func_74529_h((int)16);
    protected static final ResourceLocation TEX_DEFAULT = new ResourceLocation(W_MOD.DOMAIN, "textures/default.png");
    public int srcBlend;
    public int dstBlend;

    protected W_Render(RenderManager renderManager) {
        super(renderManager);
    }

    protected void bindTexture(String path) {
        super.func_110776_a(new ResourceLocation(W_MOD.DOMAIN, path));
    }

    protected ResourceLocation func_110775_a(T entity) {
        return TEX_DEFAULT;
    }

    public static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        return colorBuffer;
    }

    public void setCommonRenderParam(boolean smoothShading, int lighting) {
        if (smoothShading && MCH_Config.SmoothShading.prmBool) {
            GL11.glShadeModel((int)7425);
        }
        GL11.glAlphaFunc((int)516, (float)0.001f);
        GL11.glEnable((int)2884);
        int j = lighting % 65536;
        int k = lighting / 65536;
        OpenGlHelper.func_77475_a((int)OpenGlHelper.field_77476_b, (float)((float)j / 1.0f), (float)((float)k / 1.0f));
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        GL11.glEnable((int)3042);
        this.srcBlend = GL11.glGetInteger((int)3041);
        this.dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
    }

    public void restoreCommonRenderParam() {
        GL11.glBlendFunc((int)this.srcBlend, (int)this.dstBlend);
        GL11.glDisable((int)3042);
        GL11.glShadeModel((int)7424);
    }
}

