/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.block.Block
 *  net.minecraft.client.renderer.entity.RenderManager
 *  org.lwjgl.opengl.GL11
 */
package mcheli.weapon;

import mcheli.MCH_Color;
import mcheli.weapon.MCH_BulletModel;
import mcheli.weapon.MCH_EntityBaseBullet;
import mcheli.wrapper.W_Block;
import mcheli.wrapper.W_Entity;
import mcheli.wrapper.W_Render;
import mcheli.wrapper.W_WorldFunc;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import org.lwjgl.opengl.GL11;

public abstract class MCH_RenderBulletBase<T extends W_Entity>
extends W_Render<T> {
    protected MCH_RenderBulletBase(RenderManager renderManager) {
        super(renderManager);
    }

    public void doRender(T e, double var2, double var4, double var6, float var8, float var9) {
        if (e instanceof MCH_EntityBaseBullet && ((MCH_EntityBaseBullet)((Object)e)).getInfo() != null) {
            MCH_Color c = ((MCH_EntityBaseBullet)((Object)e)).getInfo().color;
            for (int y = 0; y < 3; ++y) {
                Block b = W_WorldFunc.getBlock(((W_Entity)((Object)e)).field_70170_p, (int)(((W_Entity)((Object)e)).field_70165_t + 0.5), (int)(((W_Entity)((Object)e)).field_70163_u + 1.5 - (double)y), (int)(((W_Entity)((Object)e)).field_70161_v + 0.5));
                if (b == null || b != W_Block.getWater()) continue;
                c = ((MCH_EntityBaseBullet)((Object)e)).getInfo().colorInWater;
                break;
            }
            GL11.glColor4f((float)c.r, (float)c.g, (float)c.b, (float)c.a);
        } else {
            GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        }
        GL11.glAlphaFunc((int)516, (float)0.001f);
        GL11.glEnable((int)2884);
        GL11.glEnable((int)3042);
        int srcBlend = GL11.glGetInteger((int)3041);
        int dstBlend = GL11.glGetInteger((int)3040);
        GL11.glBlendFunc((int)770, (int)771);
        this.renderBullet(e, var2, var4, var6, var8, var9);
        GL11.glColor4f((float)0.75f, (float)0.75f, (float)0.75f, (float)1.0f);
        GL11.glBlendFunc((int)srcBlend, (int)dstBlend);
        GL11.glDisable((int)3042);
    }

    public void renderModel(MCH_EntityBaseBullet e) {
        MCH_BulletModel model = e.getBulletModel();
        if (model != null) {
            this.bindTexture("textures/bullets/" + model.name + ".png");
            model.model.renderAll();
        }
    }

    public abstract void renderBullet(T var1, double var2, double var4, double var6, float var8, float var9);
}

